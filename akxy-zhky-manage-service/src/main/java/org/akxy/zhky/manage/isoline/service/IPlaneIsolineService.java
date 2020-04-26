package org.akxy.zhky.manage.isoline.service;

import java.util.List;
import java.util.Map;

import org.akxy.zhky.manage.pojo.MpValueEntity;


/**
 * @Description: 云图（平面等值线图）
 * @date: 2019年3月6日
 */
public interface IPlaneIsolineService {
	Map<String,Object> getPlaneIsoline(List<MpValueEntity> mpValueList,Map<String,Object> range,double[] isolineLevel,double[] contourValues, double xreserve,int color);

	Map<String,Object> getCollSupportIsoline(List<MpValueEntity> mpValueList,Map<String,Object> range,double[] isolineLevel,double[] contourValues);
}
