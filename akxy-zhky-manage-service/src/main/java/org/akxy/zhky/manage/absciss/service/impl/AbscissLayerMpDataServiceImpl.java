package org.akxy.zhky.manage.absciss.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.akxy.zhky.manage.absciss.service.IAbscissLayerMpDataService;
import org.akxy.zhky.manage.config.service.IConfigService;
import org.akxy.zhky.manage.dao.mapper.AbscissLayerMpDataMapper;
import org.akxy.zhky.manage.dao.mapper.AbscissLayerMpMapper;
import org.akxy.zhky.manage.footage.service.IFootageService;
import org.akxy.zhky.manage.pojo.AbscissLayerDataExample;
import org.akxy.zhky.manage.pojo.AbscissLayerMp;
import org.akxy.zhky.manage.pojo.AbscissLayerMpData;
import org.akxy.zhky.manage.pojo.Config;
import org.akxy.zhky.manage.pojo.Footage;
import org.akxy.zhky.manage.pojo.IndexTable;
import org.akxy.zhky.manage.pojo.Tunnel;
import org.akxy.zhky.manage.tunnel.service.ITunnelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: AbscissServiceImpl.java
 * @Description:
 * @date: 2018年6月8日
 */
@Service
public class AbscissLayerMpDataServiceImpl implements IAbscissLayerMpDataService{
	@Autowired
	private IConfigService iConfigService;
	@Autowired
	private ITunnelService iTunnelService;
	@Autowired
	private IFootageService iFootageService;
	@Autowired
	private AbscissLayerMpMapper abscissLayerMpMapper;
	@Autowired
	private AbscissLayerMpDataMapper abscisslayerMapper;

	@Override
	public List<AbscissLayerMpData> getAbscissLayerMpData(int areaId, List<Integer> mpIds, String stime, String etime) throws ParseException {
		//保存查询到的顶板离层数据倒list
		List<AbscissLayerMpData> list = new ArrayList<>();
		//构造查询条件
		Map<String,Object> param = new HashMap<>();
		param.put("mpIds", mpIds);
		param.put("stime", stime);
		param.put("etime", etime);

		list = abscisslayerMapper.getLineMapData(param);
		return list;
	}

	@Override
	public List<AbscissLayerMpData> getAbscissLayerMpDataWithTimeList(int areaId, List<Integer> mpIds, List<String> times)
			throws ParseException {
		List<Date> queryTimes = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(String string:times){
			queryTimes.add(sdf.parse(string));
		}
		List<AbscissLayerMpData> resultList = new ArrayList<>();

		Map<String,Object> param = new HashMap<>();
		param.put("mpIds", mpIds);
		param.put("queryTimes", queryTimes);
		resultList = abscisslayerMapper.getUserColumnByParam(param);
		return resultList;
	}

	@Override
	public List<String> getAbscissLayerMpDataTableColumns(int areaId, List<Integer> mpIds, String stime, String etime)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<String> times = new ArrayList<>();

		AbscissLayerDataExample example = new AbscissLayerDataExample();
		org.akxy.zhky.manage.pojo.AbscissLayerDataExample.Criteria criteria = example.createCriteria();

		criteria.andMpidIn(mpIds);
		criteria.andAcquisitionTimeBetween(sdf.parse(stime),sdf.parse(etime));
		criteria.andValue1NotEqualTo((float)-10000);
		criteria.andValue2NotEqualTo((float)-10000);

		example.setOrderByClause("acquisition_time");
		//传入一个起始页数和查询的数据数
		times = abscisslayerMapper.getUserColumnByExample(example);
		return times;
	}

	@Override
	public List<IndexTable> getWarning(int areaId, String stime, String etime, List<Integer> mpIds) {
		Map<String,Object> param = new HashMap<>();
		param.put("areaId", areaId);
		param.put("stime", stime);
		param.put("etime", etime);
		param.put("mpIds", mpIds);
		return abscisslayerMapper.getWarnList(param);
	}

	@Override
	public List<Map<String, Object>> getLongestAndLatestMpData(int areaId, String stime, String etime,
			List<Integer> mpIds) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Map<String,Object>> res = new ArrayList<>();
		Map<String,Object> param;
		AbscissLayerMpData blmpLongest, almpLongest , almpLatest;
		float sAvalue , sBvalue;
		float longestValue , latestValue;
		String strLocation = "";
		Tunnel tunnel = null;
		for(int mpId:mpIds){
			Map<String,Object> map = new LinkedHashMap<>();

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sdf.parse(stime));
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			String btime = sdf.format(calendar.getTime());

			param = new HashMap<>();
			param.put("mpId", mpId);
			param.put("stime", btime);
			param.put("etime", stime);
			//第一个开始时间的
			blmpLongest = abscisslayerMapper.getLongestDataByMpId(param);

			param = new HashMap<>();
			param.put("mpId", mpId);
			param.put("stime", stime);
			param.put("etime", etime);
			//开始时间的
			almpLongest = abscisslayerMapper.getLongestDataByMpId(param);
			//结束时间的
			almpLatest = abscisslayerMapper.getLatestDataByMpId(param);

			if(blmpLongest!=null&&almpLongest!=null){

				sAvalue = new BigDecimal(Math.abs(almpLongest.getValue1().doubleValue() - blmpLongest.getValue1().doubleValue())).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
				sBvalue = new BigDecimal(Math.abs(almpLongest.getValue2().doubleValue() - blmpLongest.getValue2().doubleValue())).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();

				longestValue = new BigDecimal(Math.abs(sAvalue - sBvalue)).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();

				latestValue = new BigDecimal(Math.abs(almpLatest.getValue1().doubleValue() - almpLatest.getValue2().doubleValue())).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();

				//map.put("mpId", mpId);
				AbscissLayerMp aMp = abscissLayerMpMapper.selectByPrimaryKey(mpId);

				map.put("mpId", aMp.getName());

				tunnel = iTunnelService.findTunnelById(almpLongest.getLocation());

				Footage footage = iFootageService.getFootageByTimeAndId(stime, tunnel.getId());
				Double dayFootage = footage!=null?footage.getAllValue():0;


				Double areaDistance = almpLongest.getDistance() - dayFootage;

				strLocation = new BigDecimal(areaDistance).setScale(0, BigDecimal.ROUND_HALF_UP).toString();

				map.put("tunnel", almpLongest.getLocation());
				map.put("location", strLocation);
				map.put("aDepth", almpLongest.getaDepth());
				map.put("bDepth", almpLongest.getbDepth());
				map.put("stimeA", sAvalue);
				map.put("stimeB", sBvalue);
				map.put("stimeDV", longestValue);
				map.put("etimeA", almpLatest.getValue1());
				map.put("etimeB", almpLatest.getValue2());
				map.put("etimeDV", latestValue);

				map.put("levelA", aMp.getYellowWarmValue() + "#" + aMp.getRedWarmValue());
				map.put("levelB", aMp.getbYellowWarmValue() + "#" + aMp.getbRedWarmValue());

				StringBuilder aWarn = new StringBuilder("绿色");
				StringBuilder bWarn = new StringBuilder("绿色");

				if(almpLatest.getValue1().compareTo(new BigDecimal( aMp.getYellowWarmValue())) >= 0
						&&
						almpLatest.getValue1().compareTo(new BigDecimal( aMp.getRedWarmValue())) < 0
						){
					aWarn = new StringBuilder("黄色");
				}else if(almpLatest.getValue1().compareTo(new BigDecimal( aMp.getRedWarmValue())) >= 0){
					aWarn = new StringBuilder("红色");
				}

				if(almpLatest.getValue2().compareTo(new BigDecimal( aMp.getbYellowWarmValue())) >= 0
						&&
						almpLatest.getValue2().compareTo(new BigDecimal( aMp.getbRedWarmValue())) < 0
						){
					bWarn = new StringBuilder("黄色");
				}else if(almpLatest.getValue2().compareTo(new BigDecimal( aMp.getbRedWarmValue())) >= 0){
					bWarn = new StringBuilder("红色");
				}
				StringBuilder add = aWarn.append(bWarn);
				if(add.indexOf("绿") >= 0){
					map.put("warnLevel", "绿色");
				}
				if(add.indexOf("黄") >= 0){
					map.put("warnLevel", "黄色");
				}
				if(add.indexOf("红") >= 0){
					map.put("warnLevel", "红色");
				}

				res.add(map);
			}
		}
		return res;
	}

	@Override
	public List<AbscissLayerMpData> getAbscissLayerMpDataByMpId(Long frequency, List<Integer> mpList, String stime, String etime) throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Map<String,Object> param = new HashMap<>();
		param.put("mpIds", mpList);
		param.put("stime", sdf.parse(stime));
		param.put("etime", sdf.parse(etime));
		param.put("frequency", frequency);

		List<AbscissLayerMpData> result = abscisslayerMapper.getHeatMapData(param);
		return result;
	}

	@Override
	public List<AbscissLayerMpData> getAdepthAllWarnData(int mpId, String startTime, String endTime) {
		Map<String,Object> param = new HashMap<>();
		param.put("mpId", mpId);
		param.put("stime", startTime);
		param.put("etime", endTime);
		List<AbscissLayerMpData> aDepth = abscisslayerMapper.getAdepthAllWarnList(param);
		return aDepth;
	}

	@Override
	public List<AbscissLayerMpData> getBdepthAllWarnData(int mpId, String startTime, String endTime) {
		Map<String,Object> param = new HashMap<>();
		param.put("mpId", mpId);
		param.put("stime", startTime);
		param.put("etime", endTime);
		List<AbscissLayerMpData> bDepth = abscisslayerMapper.getBdepthAllWarnList(param);
		return bDepth;
	}


	@Override
	public List<Map<String, Object>> getAbscissLineBaseData(int lineType, int mpId, String stime, String etime) throws ParseException {
		Config samplemarkConfig = iConfigService.findConfigWithType("line", "samplemark", 0);
		String[] samplemark = samplemarkConfig!=null?samplemarkConfig.getStrValue().split("#"):new String[] {"180#10800#64800"};
		int spm1 = Integer.valueOf(samplemark[0]);
		int spm2 = Integer.valueOf(samplemark[1]);
		int spm3 = Integer.valueOf(samplemark[2]);

		if(lineType == 1) {
			return abscisslayerMapper.getAbscissBaseData(stime, etime, mpId);
		}else {
			//判断取base/hour/halfday/day表
			int hourData = abscisslayerMapper.getAbscissHourDataCount(stime, etime, mpId);
			if(hourData < spm1) {
				return abscisslayerMapper.getAbscissBaseData(stime, etime, mpId);
			}else if(hourData>=spm1&&hourData<spm2){
				return abscisslayerMapper.getAbscissHourData(stime, etime, mpId);
			}else if(hourData>=spm2&&hourData<spm3) {
				return abscisslayerMapper.getAbscissHalfDayData(stime, etime, mpId);
			}else {
				return abscisslayerMapper.getAbscissOneDayData(stime, etime, mpId);
			}
		}
	}
}
