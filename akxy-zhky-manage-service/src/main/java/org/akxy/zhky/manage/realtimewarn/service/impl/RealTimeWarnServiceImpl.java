package org.akxy.zhky.manage.realtimewarn.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.akxy.zhky.manage.area.service.IAreaService;
import org.akxy.zhky.manage.battery.service.IBatteryService;
import org.akxy.zhky.manage.dao.mapper.RealtimeWarnMapper;
import org.akxy.zhky.manage.indextable.service.IBaseTableService;
import org.akxy.zhky.manage.pojo.Area;
import org.akxy.zhky.manage.pojo.IndexTable;
import org.akxy.zhky.manage.pojo.RealtimeWarn;
import org.akxy.zhky.manage.pojo.RealtimeWarnExample;
import org.akxy.zhky.manage.pojo.RealtimeWarnExample.Criteria;
import org.akxy.zhky.manage.pojo.Tunnel;
import org.akxy.zhky.manage.realtimewarn.service.IRealTimeWarnService;
import org.akxy.zhky.manage.tunnel.service.ITunnelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @date: 2019年11月25日
 */
@Service
public class RealTimeWarnServiceImpl implements IRealTimeWarnService {

	@Autowired
	private IAreaService iAreaService;
	@Autowired
	private ITunnelService iTunnelService;
	@Autowired
	private IBatteryService iBatteryService;
	@Autowired
	private IBaseTableService iBaseTableService;
	@Autowired
	private RealtimeWarnMapper realtimeWarnMapper;

	Logger logger = LoggerFactory.getLogger(RealTimeWarnServiceImpl.class);

	@Override
	public RealtimeWarn getWarnByTypeMpIdNotCompleteNotDeal(int systemType, String warnType, int mpId,boolean complete) {
		RealtimeWarnExample example = new RealtimeWarnExample();
		Criteria criteria = example.createCriteria();
		criteria.andSystemTypeEqualTo(systemType);
		criteria.andWarnTypeEqualTo(warnType);
		criteria.andMpIdEqualTo(mpId);
		criteria.andCompleteEqualTo(complete);
		criteria.andIsDealEqualTo(false);
		List<RealtimeWarn> list = realtimeWarnMapper.selectByExample(example);

		return list!=null&&list.size()>0?list.get(0):null;
	}

	@Override
	public void update(RealtimeWarn record) {
		realtimeWarnMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public void insert(RealtimeWarn record) {
		realtimeWarnMapper.insert(record);
	}

	@Override
	public List<RealtimeWarn> getAll(String warnType,Boolean isDeal) {
		RealtimeWarnExample example = new RealtimeWarnExample();
		Criteria criteria = example.createCriteria();
		if(warnType != null){
			criteria.andWarnTypeEqualTo(warnType);
		}
		if(isDeal != null){
			criteria.andIsDealEqualTo(isDeal);
		}

		example.setOrderByClause("start_time desc");

		List<RealtimeWarn> res = realtimeWarnMapper.selectByExample(example);
		res.removeIf(
				realWarn -> (realWarn.getEndTime().getTime() - realWarn.getStartTime().getTime()) < 300000);
		return res;
	}

	@Override
	public void statistics(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//		logger.info("开始处理预警");
		//读取top表，处理低电量数据
		List<IndexTable> indexTable = iBaseTableService.getAllIndexTable();
		for(IndexTable s:indexTable){
			//先把电量正常的置为complete状态
			if(1 == Integer.valueOf(s.getDeviceLevel())){
				RealtimeWarn rtw = this.getWarnByTypeMpIdNotCompleteNotDeal(s.getType(), "电量",s.getMpId(),false);
				if(rtw!=null){
					//					logger.info("由异常转为正常");
					Date startTime = rtw.getStartTime();
					Date endTime = s.getAcquisitionTime();
					String during = getDatePoor(startTime,endTime);
					rtw.setEndTime(endTime);
					rtw.setDurTime(during);
					rtw.setComplete(true);
					this.update(rtw);
				}
			}
			boolean isBatteryWarn = iBatteryService.isBatteryWarn(s.getType(), s.getMpId(), sdf.format(s.getAcquisitionTime()));
			//低电量的
			if(2 == Integer.valueOf(s.getDeviceLevel()) && isBatteryWarn){
				//如果已存在complete为false的，就更新;否则就插入
				RealtimeWarn rtw = this.getWarnByTypeMpIdNotCompleteNotDeal(s.getType(), "电量",s.getMpId(),false);
				if(rtw!=null){
					//					logger.info("更新异常测点");
					//更新
					Date startTime = rtw.getStartTime();
					Date endTime = s.getAcquisitionTime();

					String during = getDatePoor(startTime,endTime);

					rtw.setEndTime(endTime);
					rtw.setDurTime(during);

					this.update(rtw);
				}else{

					if(!isExist(s.getMpId(),s.getType(),"电量",s.getAcquisitionTime())){
						//插入
						//						logger.info("插入异常测点");
						Area area = iAreaService.getAreaById(s.getAreaId());
						Tunnel tunnel = iTunnelService.findTunnelById(Integer.valueOf(s.getLocation().toString()));
						String systemName = "钻孔应力";
						switch (s.getType()) {
						case 1:
							systemName = "钻孔应力";
							break;
						case 2:
							systemName = "综采支架";
							break;
						case 3:
							systemName = "单体支柱";
							break;
						case 4:
							systemName = "锚杆索";
							break;
						case 5:
							systemName = "顶板离层";
							break;
						case 6:
							systemName = "巷道变形";
							break;
						default:
							break;
						}
						String eventInfo = area.getAreaName() + ";" + tunnel.getName() + ";" + systemName + ";" + s.getMpName();
						RealtimeWarn insertVo = new RealtimeWarn();
						insertVo.setMpId(s.getMpId());
						insertVo.setDeviceId(s.getDeviceId());
						insertVo.setSystemType(s.getType());
						insertVo.setWarnType("电量");
						insertVo.setStartTime(s.getAcquisitionTime());
						insertVo.setEndTime(s.getAcquisitionTime());
						insertVo.setDurTime("0");
						insertVo.setValue((double)s.getBattery());
						insertVo.setUnit("v");
						insertVo.setEventInfo(eventInfo);
						insertVo.setWarnLevel(s.getDeviceStatus());
						insertVo.setComplete(false);
						insertVo.setX(s.getX());
						insertVo.setY(s.getY());
						insertVo.setPosition(s.getX() + ";" + s.getY());
						insertVo.setIsDeal(false);
						this.insert(insertVo);
					}
				}
			}
		}
		//		logger.info("预警处理结束");
	}


	@Override
	public boolean isExist(int mpId, int systemType, String warnType, Date startTime) {
		RealtimeWarnExample example = new RealtimeWarnExample();
		Criteria criteria = example.createCriteria();
		criteria.andMpIdEqualTo(mpId);
		criteria.andSystemTypeEqualTo(systemType);
		criteria.andWarnTypeEqualTo(warnType);
		criteria.andStartTimeEqualTo(startTime);

		List<RealtimeWarn> list = realtimeWarnMapper.selectByExample(example);

		return list!=null&&list.size()>0?true:false;
	}

	/*是否存在未处理报警*/
	@Override
	public boolean isExistWarn() {
		RealtimeWarnExample example = new RealtimeWarnExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDealEqualTo(false);
		List<RealtimeWarn> list = realtimeWarnMapper.selectByExample(example);
		list.removeIf(
				realWarn -> (realWarn.getEndTime().getTime() - realWarn.getStartTime().getTime()) < 300000);
		return list!=null&&list.size()>0?true:false;
	}


	public static String getDatePoor( Date startDate, Date endDate) {

		long nd = 1000 * 24 * 60 * 60;
		long nh = 1000 * 60 * 60;
		long nm = 1000 * 60;
		long ns = 1000;
		// 获得两个时间的毫秒时间差异
		long diff = endDate.getTime() - startDate.getTime();
		// 计算差多少天
		long day = diff / nd;
		// 计算差多少小时
		long hour = diff % nd / nh;
		// 计算差多少分钟
		long min = diff % nd % nh / nm;
		// 计算差多少秒
		long sec = diff % nd % nh % nm / ns;
		if(day<=0){
			if(hour<=0){
				return min + "分钟" + sec + "秒";
			}else{
				return hour + "小时" + min + "分钟" + sec + "秒";
			}
		}else{
			return day + "天" + hour + "小时" + min + "分钟" + sec + "秒";
		}
	}
}
