package org.akxy.zhky.manage.deformation.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.akxy.zhky.manage.config.service.IConfigService;
import org.akxy.zhky.manage.dao.mapper.DeformationMpDataMapper;
import org.akxy.zhky.manage.deformation.service.IDeformationMpDataService;
import org.akxy.zhky.manage.pojo.Config;
import org.akxy.zhky.manage.pojo.DeformationMpData;
import org.akxy.zhky.manage.pojo.IndexTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: DeformationServiceImpl.java
 * @Description:
 * @date: 2018年6月11日
 */
@Service
public class DeformationMpDataServiceImpl implements IDeformationMpDataService{
	@Autowired
	private IConfigService iConfigService;
	@Autowired
	private DeformationMpDataMapper deformationMapper;
	@Override
	public List<DeformationMpData> getDeformationMpData(Long frequency, List<Integer> mpIds, String stime, String etime,Float lowLength,Float heighLength) throws ParseException{
		List<DeformationMpData> list = new ArrayList<>();

		Map<String,Object> param = new HashMap<>();
		param.put("mpIds", mpIds);
		param.put("stime", stime);
		param.put("etime", etime);
		param.put("lowLength", lowLength);
		param.put("heighLength", heighLength);
		param.put("frequency", frequency);

		list = deformationMapper.getHeatMapData(param);

		return list;
	}

	@Override
	public List<String> getDeformationMpDataTableColumns(int areaId, List<Integer> mpIds, String stime, String etime,Float lowLength,Float heighLength)
			throws ParseException {
		Map<String,Object> param = new HashMap<>();
		param.put("mpIds", mpIds);
		param.put("startTime", stime);
		param.put("endTime", etime);
		param.put("lowLength", lowLength);
		param.put("heighLength", heighLength);
		return deformationMapper.getUserColumnByExample(param);
	}

	@Override
	public List<DeformationMpData> getDeformationMpDataWithTimeList(int areaId, List<Integer> mpIds, List<String> times,Float lowLength,Float heighLength) throws ParseException {
		List<Date> queryTimes = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(String string:times){
			queryTimes.add(sdf.parse(string));
		}
		List<DeformationMpData> resultList = new ArrayList<>();

		Map<String,Object> param = new HashMap<>();
		param.put("mpIds", mpIds);
		param.put("queryTimes", queryTimes);
		param.put("lowLength", lowLength);
		param.put("heighLength", heighLength);
		resultList = deformationMapper.getUserColumnByParam(param);

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
		return deformationMapper.getWarnList(param);
	}

	@Override
	public List<Map<String, Object>> getReportData(List<Integer> mpIdList, String stime, String etime) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		try {
			calendar.setTime(sdf.parse(stime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		String yesterdayStartTime = sdf.format(calendar.getTime());

		List<Map<String,Object>> res = new ArrayList<>();
		for(int i = 0 ; i < mpIdList.size() ; i++){
			Map<String,Object> param = new HashMap<>();
			param.put("yesterdayStartTime", yesterdayStartTime);
			param.put("startTime", stime);
			param.put("endTime", etime);
			param.put("mpId", mpIdList.get(i));
			res.add(deformationMapper.getReportData(param));
		}
		return res;
	}

	@Override
	public String getReportMaxValueTime(String stime,String etime, List<Integer> mpIds) {
		Map<String,Object> param = new HashMap<>();
		param.put("stime", stime);
		param.put("etime", etime);
		param.put("mpIds", mpIds);
		String result = deformationMapper.getReportMaxValueTime(param);
		return result;
	}

	@Override
	public List<Map<String, Object>> getReportGraphData(String time, List<Integer> mpIds) {
		Map<String,Object> param = new HashMap<>();
		param.put("time", time);
		param.put("mpIds", mpIds);
		return deformationMapper.getReportGraphData(param);
	}

	@Override
	public int getDataCount(int areaId, String stime, String etime, List<Integer> mpIds) {
		//传入一个起始页数和查询的数据数
		Map<String,Object> param = new HashMap<>();
		param.put("stime", stime);
		param.put("etime", etime);
		param.put("mpIds", mpIds);

		return deformationMapper.getWarnCount(param);

	}

	@Override
	public List<Map<String, Object>> getAllWarnData(int mpId, String stime, String etime) {
		Map<String,Object> param = new HashMap<>();
		param.put("mpId", mpId);
		param.put("startTime", stime);
		param.put("endTime", etime);
		return deformationMapper.getAllWarnData(param);
	}

	@Override
	public Map<String, Object> getSumReportData(int areaId, String stime, String etime) {
		Map<String,Object> param = new HashMap<>();
		param.put("areaId", areaId);
		param.put("startTime", stime);
		param.put("endTime", etime);
		return deformationMapper.getSumReportData(param);
	}

	@Override
	public List<Map<String, Object>> getDeformationLineBaseData(int lineType, int mpId, String stime, String etime) throws ParseException {
		Config samplemarkConfig = iConfigService.findConfigWithType("line", "samplemark", 0);
		String[] samplemark = samplemarkConfig!=null?samplemarkConfig.getStrValue().split("#"):new String[] {"180#10800#64800"};
		int spm1 = Integer.valueOf(samplemark[0]);
		int spm2 = Integer.valueOf(samplemark[1]);
		int spm3 = Integer.valueOf(samplemark[2]);

		if(lineType == 1) {
			return deformationMapper.getDeformationBaseData(stime, etime, mpId);
		}else {
			//判断取base/hour/halfday/day表
			int hourData = deformationMapper.getDeformationHourDataCount(stime, etime, mpId);
			if(hourData < spm1) {
				return deformationMapper.getDeformationBaseData(stime, etime, mpId);
			}else if(hourData>=spm1&&hourData<spm2){
				return deformationMapper.getDeformationHourData(stime, etime, mpId);
			}else if(hourData>=spm2&&hourData<spm3) {
				return deformationMapper.getDeformationHalfDayData(stime, etime, mpId);
			}else {
				return deformationMapper.getDeformationOneDayData(stime, etime, mpId);
			}
		}
	}
}
