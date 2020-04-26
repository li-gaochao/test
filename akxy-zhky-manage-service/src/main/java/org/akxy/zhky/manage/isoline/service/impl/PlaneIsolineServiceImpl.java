package org.akxy.zhky.manage.isoline.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.akxy.zhky.manage.isoline.service.IPlaneIsolineService;
import org.akxy.zhky.manage.pojo.MpValueEntity;
import org.akxy.zhky.manage.utils.PlaneIsolineUtil;
import org.akxy.zhky.manage.utils.PlaneIsolineUtilCollSupport;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @date: 2019年3月6日
 */
@Service
public class PlaneIsolineServiceImpl implements IPlaneIsolineService{
	@Override
	public Map<String, Object> getPlaneIsoline(List<MpValueEntity> mpValueList, Map<String,Object> range, double[] isolineLevel,double[] contourValues,double xreserve,int color) {
		Map<String,Object> planeIsoline = new HashMap<String,Object>();
		planeIsoline = PlaneIsolineUtil.getIsoline(mpValueList,range,isolineLevel,contourValues,xreserve,color);
		return planeIsoline;
	}

	@Override
	public Map<String, Object> getCollSupportIsoline(List<MpValueEntity> mpValueList, Map<String, Object> range,
			double[] isolineLevel, double[] contourValues) {
		Map<String,Object> planeIsoline = new HashMap<String,Object>();
		planeIsoline = PlaneIsolineUtilCollSupport.getIsoline(mpValueList,range,isolineLevel,contourValues);
		return planeIsoline;
	}
}
