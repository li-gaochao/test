package org.akxy.zhky.manage.footage.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.akxy.zhky.manage.config.service.IConfigService;
import org.akxy.zhky.manage.dao.mapper.FootageMapper;
import org.akxy.zhky.manage.dao.mapper.TunnelMapper;
import org.akxy.zhky.manage.footage.service.IFootageService;
import org.akxy.zhky.manage.pojo.Footage;
import org.akxy.zhky.manage.pojo.FootageExample;
import org.akxy.zhky.manage.pojo.FootageExample.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: FootageServiceImpl.java
 * @Description:进尺管理
 * @date: 2018年6月12日
 */
@Service
public class FootageServiceImpl implements IFootageService{
	@Autowired
	private TunnelMapper tunnelMapper;
	@Autowired
	private FootageMapper footageMapper;
	@Autowired
	private IConfigService iConfigService;
	@Override
	public List<Footage> getAllFootage(int tunnelId) {
		FootageExample example = new FootageExample();
		Criteria criteria = example.createCriteria();
		criteria.andTunnelIdEqualTo(tunnelId);
		example.setOrderByClause("create_time desc");

		return footageMapper.selectByExample(example);
	}

	@Override
	public Integer addFootage(Footage footage) {
		footageMapper.insert(footage);
		return footageMapper.getInsertId(footage.getTunnelId());
	}
	@Override
	public boolean deleteFootage(int id) {
		return footageMapper.deleteByPrimaryKey(id)>0?true:false;
	}

	@Override
	public Footage getFootageByTimeAndId(String time, int tunnelId) throws ParseException {

		return footageMapper.selectByDayTimeAndTunnelId(time, tunnelId);
	}

	@Override
	public Double getMonthTotalByTunnelIdAndMonth(String monthTime, String dayTime, int tunnelId) throws ParseException {
		String monthEndTime = "";//月初时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		int monthEnd = iConfigService.findConfigWithType("footage", "monthstart", 0)==null?30:iConfigService.findConfigWithType("footage", "monthend", 0).getIntValue();
		int queryDay = Integer.valueOf(dayTime.substring(8,10));
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sdf.parse(dayTime));
		calendar.set(Calendar.DAY_OF_MONTH, monthEnd);
		if(queryDay > monthEnd){
			monthEndTime = sdf.format(calendar.getTime());
		}else{
			calendar.add(Calendar.MONTH, -1);
			monthEndTime = sdf.format(calendar.getTime());
		}

		dayTime = dayTime.substring(0,10);
		//当日累计进尺
		Double thisDayFootageAllValue = footageMapper.selectMonthTotalByMonthAndTunnelId(dayTime, tunnelId)!=null?footageMapper.selectMonthTotalByMonthAndTunnelId(dayTime, tunnelId):0;
		Double startDayFootageAllValue = footageMapper.selectMonthTotalByMonthAndTunnelId(monthEndTime, tunnelId)!=null?footageMapper.selectMonthTotalByMonthAndTunnelId(monthEndTime,tunnelId):0;

		return new BigDecimal(thisDayFootageAllValue - startDayFootageAllValue).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	@Override
	public Map<String, Object> getReportFootage(String time, int upTunnelId, int belowTunnelId) throws ParseException {
		Footage upTunnel = this.getFootageByTimeAndId(time, upTunnelId);
		Footage belowTunnel = this.getFootageByTimeAndId(time, belowTunnelId);
		//获取上下巷道的本月累计进尺
		String monthTime = time.substring(0, 7);
		Double upMonthTotal = this.getMonthTotalByTunnelIdAndMonth(monthTime, time, upTunnelId);
		Double belowMonthTotal = this.getMonthTotalByTunnelIdAndMonth(monthTime,time, belowTunnelId);
		Map<String,Object> footageMap = new LinkedHashMap<>();
		//今日进尺
		footageMap.put("dayUp",upTunnel!=null?upTunnel.getValue():0);
		footageMap.put("dayBelow", belowTunnel!=null?belowTunnel.getValue():0);
		double dayAve = 0;
		if(upTunnel!=null && belowTunnel!=null){
			dayAve = new BigDecimal((upTunnel.getValue() + belowTunnel.getValue())/2).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		footageMap.put("dayAve", dayAve!=0?dayAve:0);

		//本月进尺
		footageMap.put("monthUp",upMonthTotal);
		footageMap.put("monthBelow", belowMonthTotal);
		double monthAve = 0;
		if(upMonthTotal!=null && belowMonthTotal!=null){
			monthAve = new BigDecimal((upMonthTotal + belowMonthTotal)/2).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		footageMap.put("monthAve", monthAve!=0?monthAve:0);
		//累计进尺
		footageMap.put("totalUp",upTunnel!=null?upTunnel.getAllValue():0);
		footageMap.put("totalBelow", belowTunnel!=null?belowTunnel.getAllValue():0);
		double totalAve = 0;
		if(upTunnel!=null && belowTunnel!=null){
			totalAve = new BigDecimal((upTunnel.getAllValue() + belowTunnel.getAllValue())/2).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		footageMap.put("totalAve", totalAve!=0?totalAve:0);

		footageMap.put("upName", tunnelMapper.selectByPrimaryKey(upTunnelId).getName());
		footageMap.put("downName", tunnelMapper.selectByPrimaryKey(belowTunnelId).getName());

		return footageMap;
	}

	@Override
	public Double getALLfootageByTunnelId(int tunnelId) {
		return footageMapper.selectAllFootageById(tunnelId);
	}

	@Override
	public Integer isFootageExist(int tunnelId, String time) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		FootageExample example = new FootageExample();
		Criteria criteria = example.createCriteria();
		criteria.andTunnelIdEqualTo(tunnelId);
		criteria.andCreateTimeEqualTo(sdf.parse(time));

		return footageMapper.countByExample(example);
	}
}
