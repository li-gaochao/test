package org.akxy.zhky.manage.singlesupport.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.akxy.zhky.manage.config.service.IConfigService;
import org.akxy.zhky.manage.dao.mapper.SingleSupportMpDataMapper;
import org.akxy.zhky.manage.pojo.Config;
import org.akxy.zhky.manage.pojo.IndexTable;
import org.akxy.zhky.manage.pojo.SingleSupportDataExample;
import org.akxy.zhky.manage.pojo.SingleSupportMpData;
import org.akxy.zhky.manage.singlesupport.service.ISingleSupportMpDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: SingleSupportServiceImpl.java
 * @Description:
 * @date: 2018年6月11日
 */
@Service
public class SingleSupportMpDataServiceImpl implements ISingleSupportMpDataService{
	@Autowired
	private IConfigService iConfigService;
	@Autowired
	private SingleSupportMpDataMapper singleSupportMapper;
	@Override
	public List<SingleSupportMpData> getSingleSupportMpData(Long frequency, List<Integer> mpIds, String stime, String etime,Float lowEnergy, Float heighEnergy) throws ParseException{
		List<SingleSupportMpData> list = new ArrayList<>();

		Map<String,Object> param = new HashMap<>();
		param.put("mpIds", mpIds);
		param.put("stime", stime);
		param.put("etime", etime);
		param.put("lowEnergy", lowEnergy);
		param.put("heighEnergy", heighEnergy);
		param.put("frequency", frequency);

		list = singleSupportMapper.getHeatMapData(param);
		return list;
	}

	@Override
	public List<String> getSingleSupportMpDataTableColumns(int areaId,List<Integer> mpIds,String stime,String etime, Float lowEnergy,Float heighEnergy)
			throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<String> times = new ArrayList<>();

		SingleSupportDataExample example = new SingleSupportDataExample();
		org.akxy.zhky.manage.pojo.SingleSupportDataExample.Criteria criteria = example.createCriteria();

		criteria.andMpidIn(mpIds);
		criteria.andAcquisitionTimeBetween(sdf.parse(stime),sdf.parse(etime));
		criteria.andValueNotEqualTo((float)-10000);
		if(lowEnergy!=null&&heighEnergy!=null){
			criteria.andValueBetween(lowEnergy, heighEnergy);
		}

		example.setOrderByClause("acquisition_time");
		//传入一个起始页数和查询的数据数

		times = singleSupportMapper.getUserColumnByExample(example);

		return times;
	}

	@Override
	public List<SingleSupportMpData> getSingleSupportMpDataWithTimeList(int areaId,List<Integer> mpIds,List<String> times,Float lowEnergy, Float heighEnergy) throws ParseException {
		List<Date> queryTimes = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(String string:times){
			queryTimes.add(sdf.parse(string));
		}
		List<SingleSupportMpData> resultList = new ArrayList<>();

		Map<String,Object> param = new HashMap<>();
		param.put("mpIds", mpIds);
		param.put("queryTimes", queryTimes);
		param.put("lowEnergy", lowEnergy);
		param.put("heighEnergy", heighEnergy);
		resultList = singleSupportMapper.getUserColumnByParam(param);

		return resultList;
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
		return singleSupportMapper.getWarnList(param);
	}

	@Override
	public List<Map<String, Object>> getReportData(int areaId, String stime, String etime) {
		Map<String,Object> param = new HashMap<>();
		param.put("areaId", areaId);
		param.put("startTime", stime);
		param.put("endTime", etime);
		return singleSupportMapper.getReportData(param);
	}

	@Override
	public String getReportMaxValueTime(String stime,String etime, List<Integer> mpIds) {
		Map<String,Object> param = new HashMap<>();
		param.put("stime", stime);
		param.put("etime", etime);
		param.put("mpIds", mpIds);
		String result = singleSupportMapper.getReportMaxValueTime(param);
		return result;
	}

	@Override
	public List<Map<String, Object>> getReportGraphData(String time, List<Integer> mpIds) {
		Map<String,Object> param = new HashMap<>();
		param.put("time", time);
		param.put("mpIds", mpIds);
		return singleSupportMapper.getReportGraphData(param);
	}


	@Override
	public int getDataCount(int areaId, String stime, String etime, List<Integer> mpIds) {
		//传入一个起始页数和查询的数据数
		Map<String,Object> param = new HashMap<>();
		param.put("stime", stime);
		param.put("etime", etime);
		param.put("mpIds", mpIds);

		return singleSupportMapper.getWarnCount(param);

	}

	@Override
	public List<SingleSupportMpData> getAllWarnData(int mpId, String startTime, String endTime) {
		Map<String,Object> param = new HashMap<>();
		param.put("singleMpId", mpId);
		param.put("startTime", startTime);
		param.put("endTime", endTime);
		return singleSupportMapper.getAllWarnData(param);
	}

	@Override
	public Map<String, Object> getReportSumData(int areaId, String startTime, String endTime) {
		Map<String,Object> param = new HashMap<>();
		param.put("areaId", areaId);
		param.put("startTime", startTime);
		param.put("endTime", endTime);
		return singleSupportMapper.getReportSumData(param);
	}

	@Override
	public List<Map<String, Object>> getSingleSupportLineBaseData(int lineType, int mpId, String stime, String etime) throws ParseException {
		Config samplemarkConfig = iConfigService.findConfigWithType("line", "samplemark", 0);
		String[] samplemark = samplemarkConfig!=null?samplemarkConfig.getStrValue().split("#"):new String[] {"180#10800#64800"};
		int spm1 = Integer.valueOf(samplemark[0]);
		int spm2 = Integer.valueOf(samplemark[1]);
		int spm3 = Integer.valueOf(samplemark[2]);

		if(lineType == 1) {
			return singleSupportMapper.getSingleSupportBaseData(stime, etime, mpId);
		}else {
			//判断取base/hour/halfday/day表
			int hourData = singleSupportMapper.getSingleSupportHourDataCount(stime, etime, mpId);
			if(hourData < spm1) {
				return singleSupportMapper.getSingleSupportBaseData(stime, etime, mpId);
			}else if(hourData>=spm1&&hourData<spm2){
				return singleSupportMapper.getSingleSupportHourData(stime, etime, mpId);
			}else if(hourData>=spm2&&hourData<spm3) {
				return singleSupportMapper.getSingleSupportHalfDayData(stime, etime, mpId);
			}else {
				return singleSupportMapper.getSingleSupportOneDayData(stime, etime, mpId);
			}
		}
	}
}
