package org.akxy.zhky.manage.anchor.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.akxy.zhky.manage.anchor.service.IAnchorMpDataService;
import org.akxy.zhky.manage.config.service.IConfigService;
import org.akxy.zhky.manage.dao.mapper.AnchorMpDataMapper;
import org.akxy.zhky.manage.pojo.AnchorMpData;
import org.akxy.zhky.manage.pojo.Config;
import org.akxy.zhky.manage.pojo.IndexTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: AnchorServiceImpl.java
 * @Description:
 * @date: 2018年6月11日
 */
@Service
public class AnchorMpDataServiceImpl implements IAnchorMpDataService{
	@Autowired
	private IConfigService iConfigService;
	@Autowired
	private AnchorMpDataMapper anchorMapper;
	@Override
	public List<AnchorMpData> getAnchorMpData(Long frequency, List<Integer> mpIds, String stime, String etime) throws ParseException{
		List<AnchorMpData> list = new ArrayList<>();

		Map<String,Object> param = new HashMap<>();
		param.put("mpIds", mpIds);
		param.put("stime", stime);
		param.put("etime", etime);
		param.put("frequency", frequency);

		list = anchorMapper.getHeatMapData(param);
		return list;
	}

	@Override
	public List<String> getAnchorMpDataTableColumns(int areaId, List<Integer> mpIds, String stime, String etime)
			throws ParseException {
		List<String> times = new ArrayList<>();
		//		AnchorDataExample example = new AnchorDataExample();
		//		org.akxy.zhky.manage.pojo.AnchorDataExample.Criteria criteria = example.createCriteria();
		//
		//		criteria.andMpidIn(mpIds);
		//		criteria.andAcquisitionTimeBetween(sdf.parse(stime),sdf.parse(etime));
		//
		//		example.setOrderByClause("acquisition_time");
		//传入一个起始页数和查询的数据数

		//		times = anchorMapper.getUserColumnByExample(example);
		Map<String,Object> param = new HashMap<>();
		param.put("mpIds", mpIds);
		param.put("startTime", stime);
		param.put("endTime", etime);

		times = anchorMapper.getUserColumnByExample(param);

		return times;
	}

	@Override
	public List<AnchorMpData> getAnchorLineData(List<Integer> mpIds, String stime, String etime){
		List<AnchorMpData> list = new ArrayList<>();

		Map<String,Object> param = new HashMap<>();
		param.put("mpIds", mpIds);
		param.put("stime", stime);
		param.put("etime", etime);

		list = anchorMapper.getLineData(param);
		return list;
	}



	@Override
	public List<AnchorMpData> getAnchorMpDataWithTimeList(int areaId, List<Integer> mpIds, List<String> times) throws ParseException {
		List<Date> queryTimes = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(String string:times){
			queryTimes.add(sdf.parse(string));
		}
		List<AnchorMpData> resultList = new ArrayList<>();

		Map<String,Object> param = new HashMap<>();
		param.put("mpIds", mpIds);
		param.put("queryTimes", queryTimes);

		resultList = anchorMapper.getUserColumnByParam(param);

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
		return anchorMapper.getWarnList(param);
	}

	@Override
	public List<Map<String, Object>> getReportData(String stime, String etime , List<Integer> mpIds) {
		List<Map<String, Object>> res = new ArrayList<>();
		for(Integer i:mpIds){
			Map<String,Object> param = new HashMap<>();
			param.put("startTime", stime);
			param.put("endTime", etime);
			param.put("mpId", i);
			Map<String,Object> map = anchorMapper.getReportData(param);
			if(map!=null){
				res.add(map);
			}
		}
		return res;
	}

	@Override
	public String getReportMaxValueTime(String stime,String etime, List<Integer> mpIds) {
		Map<String,Object> param = new HashMap<>();
		param.put("stime", stime);
		param.put("etime", etime);
		param.put("mpIds", mpIds);
		String result = anchorMapper.getReportMaxValueTime(param);
		return result;
	}

	@Override
	public List<Map<String, Object>> getReportGraphData(String time, List<Integer> mpIds) {
		Map<String,Object> param = new HashMap<>();
		param.put("time", time);
		param.put("mpIds", mpIds);
		return anchorMapper.getReportGraphData(param);
	}

	@Override
	public int getDataCount(int areaId, String stime, String etime, List<Integer> mpIds) {
		//传入一个起始页数和查询的数据数
		Map<String,Object> param = new HashMap<>();
		param.put("stime", stime);
		param.put("etime", etime);
		param.put("mpIds", mpIds);

		return anchorMapper.getWarnCount(param);
	}

	@Override
	public List<AnchorMpData> getAllWarnData(int mpId, String startTime, String endTime) {
		Map<String,Object> param = new HashMap<>();
		param.put("anchorMpId", mpId);
		param.put("startTime", startTime);
		param.put("endTime", endTime);
		return anchorMapper.getAllWarnData(param);
	}

	@Override
	public List<Map<String, Object>> getAnchorLineBaseData(int lineType, int mpId, String stime, String etime) throws ParseException {
		Config samplemarkConfig = iConfigService.findConfigWithType("line", "samplemark", 0);
		String[] samplemark = samplemarkConfig!=null?samplemarkConfig.getStrValue().split("#"):new String[] {"180#10800#64800"};
		int spm1 = Integer.valueOf(samplemark[0]);
		int spm2 = Integer.valueOf(samplemark[1]);
		int spm3 = Integer.valueOf(samplemark[2]);

		if(lineType == 1) {
			return anchorMapper.getAnchorBaseData(stime, etime, mpId);
		}else {
			//判断取base/hour/halfday/day表
			int hourData = anchorMapper.getAnchorHourDataCount(stime, etime, mpId);
			if(hourData < spm1) {
				return anchorMapper.getAnchorBaseData(stime, etime, mpId);
			}else if(hourData>=spm1&&hourData<spm2){
				return anchorMapper.getAnchorHourData(stime, etime, mpId);
			}else if(hourData>=spm2&&hourData<spm3) {
				return anchorMapper.getAnchorHalfDayData(stime, etime, mpId);
			}else {
				return anchorMapper.getAnchorOneDayData(stime, etime, mpId);
			}
		}
	}
}
