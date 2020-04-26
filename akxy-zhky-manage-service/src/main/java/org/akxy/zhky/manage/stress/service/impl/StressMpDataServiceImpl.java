package org.akxy.zhky.manage.stress.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.akxy.zhky.manage.config.service.IConfigService;
import org.akxy.zhky.manage.dao.mapper.StressMpDataMapper;
import org.akxy.zhky.manage.pojo.Config;
import org.akxy.zhky.manage.pojo.IndexTable;
import org.akxy.zhky.manage.pojo.StressDataExample;
import org.akxy.zhky.manage.pojo.StressMpData;
import org.akxy.zhky.manage.stress.service.IStressMpDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: StressServiceImpl.java
 * @Description:
 * @date: 2018年6月11日
 */
@Service
public class StressMpDataServiceImpl implements IStressMpDataService{
	@Autowired
	private IConfigService iConfigService;
	@Autowired
	private StressMpDataMapper stressMapper;

	@Override
	public List<StressMpData> getStressMpData(Long frequency, List<Integer> mpIds, String stime, String etime) throws ParseException{
		List<StressMpData> list = new ArrayList<>();

		Map<String,Object> param = new HashMap<>();
		param.put("mpIds", mpIds);
		param.put("stime", stime);
		param.put("etime", etime);
		param.put("frequency", frequency);

		list = stressMapper.getHeatMapData(param);
		return list;
	}

	@Override
	public List<String> getStressMpDataTableColumns(int areaId, List<Integer> mpIds, String stime, String etime)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<String> times = new ArrayList<>();

		StressDataExample example = new StressDataExample();
		org.akxy.zhky.manage.pojo.StressDataExample.Criteria criteria = example.createCriteria();

		criteria.andMpidIn(mpIds);
		criteria.andAcquisitionTimeBetween(sdf.parse(stime),sdf.parse(etime));
		criteria.andValueNotEqualTo((float) -10000);

		example.setOrderByClause("acquisition_time");

		times = stressMapper.getUserColumnByExample(example);

		return times;
	}

	@Override
	public List<StressMpData> getStressMpDataWithTimeList(int areaId, List<Integer> mpIds, List<String> times) throws ParseException {
		List<Date> queryTimes = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(String string:times){
			queryTimes.add(sdf.parse(string));
		}
		List<StressMpData> resultList = new ArrayList<>();

		Map<String,Object> param = new HashMap<>();
		param.put("mpIds", mpIds);
		param.put("queryTimes", queryTimes);

		resultList = stressMapper.getUserColumnByParam(param);

		return resultList;
	}

	@Override
	public List<StressMpData> getStressLineData(List<Integer> mpIds, String stime, String etime){
		List<StressMpData> list = new ArrayList<>();

		Map<String,Object> param = new HashMap<>();
		param.put("mpIds", mpIds);
		param.put("stime", stime);
		param.put("etime", etime);

		list = stressMapper.getLineData(param);
		return list;
	}

	@Override
	public List<IndexTable> getWarning(int areaId, String stime ,String etime, List<Integer> mpIds , int startrow , int pagesize) {
		Map<String,Object> param = new HashMap<>();
		param.put("areaId", areaId);
		param.put("stime", stime);
		param.put("etime", etime);
		param.put("mpIds", mpIds);
		param.put("startrow", startrow);
		param.put("pagesize", pagesize);
		return stressMapper.getWarnList(param);
	}

	@Override
	public List<Map<String, Object>> getReportData(String stime, String etime , List<Integer> mpIds) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Map<String, Object>> result = new ArrayList<>();
		//昨天的开始时间
		String lasttime = "";
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(sdf.parse(stime));
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			lasttime = sdf.format(calendar.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		for(Integer mpid:mpIds){
			Map<String,Object> param = new HashMap<>();
			param.put("lasttime", lasttime);
			param.put("stime", stime);
			param.put("etime", etime);
			param.put("mpid", mpid);

			result.add(stressMapper.getReportData(param));
		}

		return result;
	}

	@Override
	public String getReportMaxValueTime(String stime,String etime, List<Integer> mpIds) {
		Map<String,Object> param = new HashMap<>();
		param.put("stime", stime);
		param.put("etime", etime);
		param.put("mpIds", mpIds);
		String result = stressMapper.getReportMaxValueTime(param);
		return result;
	}

	@Override
	public List<Map<String, Object>> getReportGraphData(String time, List<Integer> mpIds) {
		Map<String,Object> param = new HashMap<>();
		param.put("time", time);
		param.put("mpIds", mpIds);
		return stressMapper.getReportGraphData(param);
	}

	@Override
	public int getDataCount(int areaId, String stime, String etime, List<Integer> mpIds) {

		Map<String,Object> param = new HashMap<>();
		param.put("stime", stime);
		param.put("etime", etime);
		param.put("mpIds", mpIds);

		return stressMapper.getWarnCount(param);
	}

	@Override
	public List<StressMpData> getAllWarnData(int mpId, String startTime, String endTime) {
		Map<String,Object> param = new HashMap<>();
		param.put("stressMpId", mpId);
		param.put("startTime", startTime);
		param.put("endTime", endTime);
		return stressMapper.getAllWarnData(param);
	}


	@Override
	public Float getAreaDistanceByMpIdAndDayTime(int mpId, String dayTime) {
		return stressMapper.getAreaDistanceByMpIdAndDayTime(mpId, dayTime);
	}

	@Override
	public Float getMaxValueByMpIdAndDayTime(int mpId, String dayTime) {
		return stressMapper.getMaxValueByMpIdAndDayTime(mpId, dayTime);
	}

	@Override
	public Map<String,Object> getValueByMpIdAndTime(int mpId, String time) {
		return stressMapper.getValueByMpIdAndTime(mpId, time);
	}


	@Override
	public List<Map<String, Object>> getStressLineDataNew(int mpId, String sTime, String eTime) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Map<String,Object> minmaxTime = stressMapper.getMinMaxTime(mpId, sTime, eTime);
		String sDateTime = minmaxTime!=null?minmaxTime.get("stime").toString():null;
		String eDateTime = minmaxTime!=null?minmaxTime.get("etime").toString():null;

		if(minmaxTime!=null) {

			int dataNumber = Integer.valueOf(iConfigService.findConfigWithType("line", "separate" ,0).getStrValue().split("#")[0]);

			long sTimeStamp = sdf.parse(sDateTime).getTime() / 1000;
			long eTimeStamp = sdf.parse(eDateTime).getTime() / 1000;
			float timeSpanTemp = (eTimeStamp - sTimeStamp)/60;
			timeSpanTemp = timeSpanTemp/dataNumber;
			int timeSpan = new BigDecimal(timeSpanTemp).setScale(0,BigDecimal.ROUND_UP).intValue() * 60;

			return stressMapper.getStressLineDataProcedure(sDateTime, eDateTime, mpId, timeSpan);
		}else {
			return null;
		}
	}


	@Override
	public List<Map<String, Object>> getStressLineBaseData(int lineType, int mpId, String stime, String etime) throws ParseException {
		Config samplemarkConfig = iConfigService.findConfigWithType("line", "samplemark", 0);
		String[] samplemark = samplemarkConfig!=null?samplemarkConfig.getStrValue().split("#"):new String[] {"180#10800#64800"};
		int spm1 = Integer.valueOf(samplemark[0]);
		int spm2 = Integer.valueOf(samplemark[1]);
		int spm3 = Integer.valueOf(samplemark[2]);

		if(lineType == 1) {
			return stressMapper.getStressBaseData(stime, etime, mpId);
		}else {
			//判断取base/hour/halfday/day表
			int hourData = stressMapper.getStressHourDataCount(stime, etime, mpId);
			if(hourData < spm1) {
				return stressMapper.getStressBaseData(stime, etime, mpId);
			}else if(hourData>=spm1&&hourData<spm2){
				return stressMapper.getStressHourData(stime, etime, mpId);
			}else if(hourData>=spm2&&hourData<spm3) {
				return stressMapper.getStressHalfDayData(stime, etime, mpId);
			}else {
				return stressMapper.getStressOneDayData(stime, etime, mpId);
			}
		}
	}
}