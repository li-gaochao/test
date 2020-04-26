package org.akxy.zhky.manage.absciss.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.akxy.zhky.manage.absciss.service.IAbscissLayerReportService;
import org.akxy.zhky.manage.absciss.service.util.SortList;
import org.akxy.zhky.manage.config.service.IConfigService;
import org.akxy.zhky.manage.dao.mapper.AbscissLayerMpMapper;
import org.akxy.zhky.manage.pojo.AbscissLayerMpData;
import org.akxy.zhky.manage.pojo.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.akxy.zhky.akxy_common.util.DateDifferUtils;

/**
 * @ClassName: AbscissLayerReportServiceImpl.java
 * @Description:
 * @date: 2018年9月19日
 */
@Service
public class AbscissLayerReportServiceImpl implements IAbscissLayerReportService{

	@Autowired
	private IConfigService iConfigService;
	@Autowired
	private AbscissLayerMpMapper abscissLayerMpMapper;

	@Override
	public List<Map<String, Object>> getWarmData(List<Integer> mpList, List<AbscissLayerMpData> warmBaseData) {
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();

		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm:ss");
		//报表预警时段
		Config warnPeriodCon = iConfigService.findConfigWithType("warn", "period",0);
		int warnPeriodInt = warnPeriodCon!=null?warnPeriodCon.getIntValue():30;
		//预警时段标准（最小连续分钟数）
		long warnPeriodLong = Long.valueOf(warnPeriodInt);
		float value1 = 0 , value2 = 0 , value = 0 , yellowWarmValue = 0 , readWarmValue = 0;

		List<List<AbscissLayerMpData>> lsitWarm = new ArrayList<List<AbscissLayerMpData>>();
		List<AbscissLayerMpData> tempList = new ArrayList<AbscissLayerMpData>();
		Date startDate = null,endDate = null;
		AbscissLayerMpData sA = null, sB = null;
		if(warmBaseData!=null&&warmBaseData.size()==1){
			result = new ArrayList<Map<String,Object>>();
		}else{
			for(int i = 1 ; i < warmBaseData.size() ; i++){
				sA = warmBaseData.get(i-1);
				if(tempList.size()==0){
					tempList.add(sA);
				}
				sB = warmBaseData.get(i);

				startDate = sA.getAcquisitionTime();
				endDate = sB.getAcquisitionTime();

				long diff = DateDifferUtils.getMinutePoor(startDate, endDate);

				if(sA.getMeasurePointId().equals(sB.getMeasurePointId())&&diff <= warnPeriodLong){
					tempList.add(sB);
				}else{
					lsitWarm.add(tempList);
					tempList = new ArrayList<AbscissLayerMpData>();
				}
			}
			if(tempList.size()>0){
				lsitWarm.add(tempList);
			}
			for(int i = 0 ; i < lsitWarm.size() ; i ++){
				Map<String,Object> map = new LinkedHashMap<String,Object>();
				/*求出sub中的value最大值,相同的最大值取出现时间最早的*/
				AbscissLayerMpData maxData = new SortList().getMaxData(lsitWarm.get(i));
				value1 = maxData.getValue1().floatValue();
				value2 = maxData.getValue2().floatValue();
				value = new BigDecimal(Math.abs(value1 - value2)).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
				yellowWarmValue = maxData.getYellowWarmValue();
				readWarmValue = maxData.getRedWarmValue();
				//map.put("mpId", maxData.getMeasurePointId());

				map.put("mpId", abscissLayerMpMapper.selectByPrimaryKey(maxData.getMeasurePointId()).getName());

				if(value >= yellowWarmValue && value < readWarmValue ){
					map.put("level", "黄色预警");
				}else if(value >= readWarmValue){
					map.put("level", "红色预警");
				}
				map.put("period", sdf.format(lsitWarm.get(i).get(0).getAcquisitionTime()) + "--" + sdf.format(lsitWarm.get(i).get(lsitWarm.get(i).size() - 1).getAcquisitionTime()));
				map.put("maxValue", value);
				map.put("maxTime", sdf.format(maxData.getAcquisitionTime()));
				result.add(map);
			}
		}
		return result;
	}

}
