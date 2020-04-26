package org.akxy.zhky.manage.collsupport.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.akxy.zhky.manage.collsupport.service.ICollSupportMpDataService;
import org.akxy.zhky.manage.config.service.IConfigService;
import org.akxy.zhky.manage.dao.mapper.CollSupportDataMapper;
import org.akxy.zhky.manage.dao.mapper.CollSupportMpDataMapper;
import org.akxy.zhky.manage.pojo.CollSupportData;
import org.akxy.zhky.manage.pojo.CollSupportDataExample;
import org.akxy.zhky.manage.pojo.CollSupportMpData;
import org.akxy.zhky.manage.pojo.Config;
import org.akxy.zhky.manage.pojo.IndexTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: CollSupportServiceImpl.java
 * @Description:
 * @date: 2018年6月11日
 */
@Service
public class CollSupportMpDataServiceImpl implements ICollSupportMpDataService{
	@Autowired
	private IConfigService iConfigService;
	@Autowired
	private CollSupportMpDataMapper collSupportMapper;
	@Autowired
	private CollSupportDataMapper collSupportDataMapper;

	@Override
	public List<CollSupportMpData> getCollSupportMpData(Long frequency, List<Integer> mpIds, String stime, String etime,Float lowEnergy,Float heighEnergy) throws ParseException{
		List<CollSupportMpData> list = new ArrayList<>();
		Map<String,Object> param = new HashMap<>();
		param.put("mpIds", mpIds);
		param.put("stime", stime);
		param.put("etime", etime);
		param.put("lowEnergy", lowEnergy);
		param.put("heighEnergy", heighEnergy);
		param.put("frequency", frequency);

		list = collSupportMapper.getHeatMapData(param);
		return list;
	}

	@Override
	public List<String> getCollSupportMpDataTableColumns(int areaId, List<Integer> mpIds, String stime, String etime, Float lowEnergy,Float heighEnergy)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<String> times = new ArrayList<>();

		CollSupportDataExample example = new CollSupportDataExample();
		org.akxy.zhky.manage.pojo.CollSupportDataExample.Criteria criteria = example.createCriteria();

		criteria.andMpidIn(mpIds);
		criteria.andAcquisitionTimeBetween(sdf.parse(stime),sdf.parse(etime));

		if(lowEnergy!=null&&heighEnergy!=null){
			criteria.andValueBetween(lowEnergy, heighEnergy);
		}
		criteria.andValueNotEqualTo((float)-10000);

		example.setOrderByClause("acquisition_time");
		//传入一个起始页数和查询的数据数

		times = collSupportMapper.getUserColumnByExample(example);

		return times;
	}

	@Override
	public List<CollSupportMpData> getCollSupportMpDataWithTimeList(int areaId, List<Integer> mpIds, List<String> times,Float lowEnergy, Float heighEnergy) throws ParseException {
		List<Date> queryTimes = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(String string:times){
			queryTimes.add(sdf.parse(string));
		}
		List<CollSupportMpData> resultList = new ArrayList<>();

		Map<String,Object> param = new HashMap<>();
		param.put("mpIds", mpIds);
		param.put("queryTimes", queryTimes);
		param.put("lowEnergy", lowEnergy);
		param.put("heighEnergy", heighEnergy);
		resultList = collSupportMapper.getUserColumnByParam(param);

		return resultList;
	}

	@Override
	public List<CollSupportMpData> getCollSupportMpLineData(List<Integer> mpIds, String stime, String etime,Float lowEnergy,Float heighEnergy){
		List<CollSupportMpData> list = new ArrayList<>();
		Map<String,Object> param = new HashMap<>();
		param.put("mpIds", mpIds);
		param.put("stime", stime);
		param.put("etime", etime);
		param.put("lowEnergy", lowEnergy);
		param.put("heighEnergy", heighEnergy);

		list = collSupportMapper.getLineData(param);
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
		return collSupportMapper.getWarnList(param);
	}

	@Override
	public List<Map<String, Object>> getReportData(int areaId, String stime,String etime) {

		Map<String,Object> param = new HashMap<>();
		param.put("areaId", areaId);
		param.put("startTime", stime);
		param.put("endTime", etime);
		return collSupportMapper.getReportData(param);
	}

	@Override
	public String getReportMaxValueTime(String stime,String etime, List<Integer> mpIds) {

		Map<String,Object> param = new HashMap<>();
		param.put("stime", stime);
		param.put("etime", etime);
		param.put("mpIds", mpIds);
		String result = collSupportMapper.getReportMaxValueTime(param);
		return result;
	}

	@Override
	public List<Map<String, Object>> getReportGraphData(String time, List<Integer> mpIds) {
		Map<String,Object> param = new HashMap<>();
		param.put("time", time);
		param.put("mpIds", mpIds);
		return collSupportMapper.getReportGraphData(param);
	}

	@Override
	public int getDataCount(int areaId, String stime, String etime, List<Integer> mpIds) {
		//传入一个起始页数和查询的数据数
		Map<String,Object> param = new HashMap<>();
		param.put("stime", stime);
		param.put("etime", etime);
		param.put("mpIds", mpIds);

		return collSupportMapper.getWarnCount(param);

	}

	@Override
	public List<Map<String,Object>> getWeightLine(int mpId, String startTime, String endTime, int timespan) {
		Map<String,Object> param = new HashMap<>();
		param.put("mpId", mpId);
		param.put("startTime", startTime);
		param.put("endTime", endTime);
		param.put("timespan", timespan);

		return collSupportMapper.getWeightLine(param);
	}

	@Override
	public List<CollSupportMpData> getAllWarnData(int mpId, String startTime, String endTime) {
		Map<String,Object> param = new HashMap<>();
		param.put("collMpId", mpId);
		param.put("startTime", startTime);
		param.put("endTime", endTime);
		return collSupportMapper.getAllWarnData(param);
	}

	@Override
	public List<CollSupportMpData> getAllAngleWarnData(int mpId, String startTime, String endTime) {
		Map<String,Object> param = new HashMap<>();
		param.put("collMpId", mpId);
		param.put("startTime", startTime);
		param.put("endTime", endTime);
		return collSupportMapper.getAllAngleWarnData(param);
	}

	@Override
	public List<CollSupportData> getCollSupportData(int mpId, String startTime, String endTime) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		CollSupportDataExample example = new CollSupportDataExample();
		org.akxy.zhky.manage.pojo.CollSupportDataExample.Criteria criteria = example.createCriteria();
		criteria.andMpidEqualTo(mpId);
		criteria.andAcquisitionTimeBetween(sdf.parse(startTime), sdf.parse(endTime));
		return collSupportDataMapper.selectByExample(example);
	}

	@Override
	public Map<String, Object> getDataStatistics(int areaId, String startTime, String endTime) {
		Map<String,Object> param = new HashMap<>();
		param.put("areaId", areaId);
		param.put("startTime", startTime);
		param.put("endTime", endTime);
		Map<String,Object> res = collSupportMapper.getDataStatistics(param);
		if(res!=null){
			if(!res.containsKey("collSupport_max_value")){
				res.put("collSupport_max_value", "--");
			}
			if(!res.containsKey("collSupport_max_name")){
				res.put("collSupport_max_name", "--");
			}
			if(!res.containsKey("collSupport_min_value")){
				res.put("collSupport_min_value", "--");
			}
			if(!res.containsKey("collSupport_min_name")){
				res.put("collSupport_min_name", "--");
			}
			if(!res.containsKey("collSupport_aver_value")){
				res.put("collSupport_aver_value", "--");
			}
			if(!res.containsKey("expan_max_value")){
				res.put("expan_max_value", "--");
			}
			if(!res.containsKey("expan_max_name")){
				res.put("expan_max_name", "--");
			}
			if(!res.containsKey("expan_min_value")){
				res.put("expan_min_value", "--");
			}
			if(!res.containsKey("expan_min_name")){
				res.put("expan_min_name", "--");
			}
			if(!res.containsKey("expan_aver_value")){
				res.put("expan_aver_value", "--");
			}
			if(!res.containsKey("warn_number")){
				res.put("warn_number", "--");
			}
		}
		return res;
	}


	@Override
	public Map<String,Object> getDayMaxValue(Integer supportNum, String stime, String etime) {
		Map<String,Object> param = new HashMap<>();
		param.put("supportNum", supportNum);
		param.put("stime", stime);
		param.put("etime", etime);
		return collSupportMapper.getDayMaxValue(param);
	}

	@Override
	public List<Map<String,Object>> getDayAverValue(List<Integer> supportNums, String stime, String etime) {
		Map<String,Object> param = new HashMap<>();
		param.put("supportNums", supportNums);
		param.put("stime", stime);
		param.put("etime", etime);
		return collSupportMapper.getDayAverValue(param);
	}


	@Override
	public List<Map<String,Object>> getDayMaxValueWithProcedure(String etime ,String stime,Integer supportNum,Integer timeSpan) {
		Map<String,Object> param = new HashMap<>();
		param.put("supportNum", supportNum);
		param.put("stime", stime);
		param.put("etime", etime);
		param.put("timespan", timeSpan);
		return collSupportMapper.getDayMaxValueWithProcedure(param);
	}

	@Override
	public List<Map<String, Object>> getReportExpanData(List<Map<String, Object>> bodyList,String stime,String etime) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		//默认两个是一组支架
		for(int i = 0 ; i < bodyList.size() ; i++){
			SetList<String> befortExpanList = new SetList<>();
			SetList<String> afterExpanList = new SetList<>();
			int afterNumber = 0;
			int beforeNumber = 0;
			double afterValue = 0;
			double beforeValue = 0;
			double averValue = 0;
			int supportNum = (int) bodyList.get(i).get("device_id");
			if(i+1 <= bodyList.size()-1){
				int aId = (int) bodyList.get(i).get("id");
				int bId = (int) bodyList.get(i+1).get("id");
				List<CollSupportData> aList = this.getCollSupportData(aId,stime,etime);
				List<CollSupportData> bList = this.getCollSupportData(bId,stime,etime);
				for(int j = 0 ; j < aList.size() ; j++){
					if(j+1 <= aList.size() - 1){

						if((aList.get(j).getValue() - aList.get(j+1).getValue()) > 10){
							befortExpanList.add(sdf.format(aList.get(j).getAcquisitionTime()));
						}
						if((aList.get(j).getValue() - aList.get(j+1).getValue()) < -5){
							afterExpanList.add(sdf.format(aList.get(j+1).getAcquisitionTime()));
						}
					}
				}
				for(int j = 0 ; j < bList.size() ; j++){
					if(j+1 <= bList.size() - 1){
						if((bList.get(j).getValue() - bList.get(j+1).getValue()) > 10){
							befortExpanList.add(sdf.format(bList.get(j).getAcquisitionTime()));
						}
						if((bList.get(j).getValue() - bList.get(j+1).getValue()) < -5){
							afterExpanList.add(sdf.format(bList.get(j+1).getAcquisitionTime()));
						}
					}

				}
			}

			ListSort(befortExpanList);
			ListSort(afterExpanList);

			for(int a = 0 ; a < befortExpanList.size() ; a++){
				if(befortExpanList.size() == 1){
					//Double a1 = collSupportMapper.getExpanData(supportNum, befortExpanList.get(a));
					beforeValue = 0;
					beforeNumber++;
				}else{
					if(a+1 <= befortExpanList.size()-1){
						Double a1 = collSupportMapper.getExpanData(supportNum, befortExpanList.get(a));
						Double a2 = collSupportMapper.getExpanData(supportNum, befortExpanList.get(a+1));
						a1 = a1==null?0:a1;
						a2 = a2==null?0:a2;
						beforeValue += (a1-a2);
						beforeNumber++;
					}
				}

			}
			if(beforeNumber == 0){
				beforeValue = 0;
			}else{
				beforeValue = new BigDecimal(beforeValue).divide(new BigDecimal(beforeNumber),2, BigDecimal.ROUND_HALF_UP).doubleValue();
			}

			for(int a = 0 ; a < afterExpanList.size() ; a++){
				if(afterExpanList.size() == 0){
					//Double a1 = collSupportMapper.getExpanData(supportNum, afterExpanList.get(a));
					afterValue = 0;
					afterNumber++;
				}else{
					if(a+1 <= afterExpanList.size()-1){
						Double a1 = collSupportMapper.getExpanData(supportNum, afterExpanList.get(a));
						Double a2 = collSupportMapper.getExpanData(supportNum, afterExpanList.get(a+1));
						a1 = a1==null?0:a1;
						a2 = a2==null?0:a2;
						afterValue += (a1-a2);
						afterNumber++;
					}
				}
			}
			if(afterNumber == 0){
				afterValue = 0;
			}else{
				afterValue = new BigDecimal(afterValue).divide(new BigDecimal(afterNumber),2, BigDecimal.ROUND_HALF_UP).doubleValue();
			}

			averValue = new BigDecimal(afterValue + beforeValue).divide(new BigDecimal(2),2, BigDecimal.ROUND_HALF_UP).doubleValue();

			bodyList.get(i).put("max_expan", afterValue);
			bodyList.get(i).put("min_expan", beforeValue);
			bodyList.get(i).put("aver_expan", averValue);

			i+= 1;
		}


		return bodyList;
	}


	@Override
	public List<Map<String, Object>> getCollSupportLineBaseData(int lineType, int mpId, String stime, String etime) throws ParseException {
		Config samplemarkConfig = iConfigService.findConfigWithType("line", "samplemark", 0);
		String[] samplemark = samplemarkConfig!=null?samplemarkConfig.getStrValue().split("#"):new String[] {"180#10800#64800"};
		int spm1 = Integer.valueOf(samplemark[0]);
		int spm2 = Integer.valueOf(samplemark[1]);
		int spm3 = Integer.valueOf(samplemark[2]);

		if(lineType == 1) {
			return collSupportMapper.getCollSupportBaseData(stime, etime, mpId);
		}else {
			//判断取base/hour/halfday/day表
			int hourData = collSupportMapper.getCollSupportHourDataCount(stime, etime, mpId);
			if(hourData < spm1) {
				return collSupportMapper.getCollSupportBaseData(stime, etime, mpId);
			}else if(hourData>=spm1&&hourData<spm2){
				return collSupportMapper.getCollSupportHourData(stime, etime, mpId);
			}else if(hourData>=spm2&&hourData<spm3) {
				return collSupportMapper.getCollSupportHalfDayData(stime, etime, mpId);
			}else {
				return collSupportMapper.getCollSupportOneDayData(stime, etime, mpId);
			}
		}
	}

	/**
	 * 将集合中的内容按时间排序
	 * @param list
	 */
	private static void ListSort(SetList<String> list) {
		Collections.sort(list, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				try {
					Date dt1 = format.parse(o1);
					Date dt2 = format.parse(o2);
					if (dt1.getTime() > dt2.getTime()) {
						return 1;
					} else if (dt1.getTime() < dt2.getTime()) {
						return -1;
					} else {
						return 0;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return 0;
			}
		});
	}
}