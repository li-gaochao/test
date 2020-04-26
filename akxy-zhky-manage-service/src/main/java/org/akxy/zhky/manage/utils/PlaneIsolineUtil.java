package org.akxy.zhky.manage.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.akxy.zhky.manage.pojo.MpValueEntity;
import org.akxy.zhky.manage.utils.wContour.Global.Polygon;


/**
 * @Description: 平面等值线图
 * @date: 2019年2月21日
 */
public class PlaneIsolineUtil{
	/**
	 * @Title: getIsoline
	 * @Description: 根据传入的测点（值）获取平面等值线图数据
	 * @param: @param mpValues  测点--值
	 * @param: @return
	 * @return: Map<String,Object>
	 */
	public static Map<String,Object> getIsoline(List<MpValueEntity> mpValues,Map<String,Object> range,double[] isolineLevel,double[] contourValues,double xreserve,int color){

		List<Double> xList = new ArrayList<>();
		List<Double> yList = new ArrayList<>();
		List<Double> vList = new ArrayList<>();
		for(MpValueEntity mpValueEntity:mpValues){
			xList.add(mpValueEntity.getX());
			yList.add(mpValueEntity.getY());
			vList.add(mpValueEntity.getValue());
		}

		double xMin = Double.parseDouble(range.get("min_x").toString());
		double xMax = Double.parseDouble(range.get("max_x").toString());

		double yMin = Double.parseDouble(range.get("min_y").toString());
		double yMax = Double.parseDouble(range.get("max_y").toString());

		double zMin = Collections.min(vList);
		double zMax = Collections.max(vList);

		//获得XY方向上的分隔点数，作为参数代入

		int xNum = (int) xreserve;

		double tempDiv = new BigDecimal(xMax - xMin).divide(new BigDecimal(xNum),BigDecimal.ROUND_HALF_UP).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		xMax+=tempDiv;
		xNum +=1;
		//int xNum = new BigDecimal(xMax - xMin).divide(new BigDecimal("30"),BigDecimal.ROUND_HALF_UP).intValue();
		int yNum = new BigDecimal(yMax).divide(new BigDecimal("1"),BigDecimal.ROUND_HALF_UP).intValue();
		//获取三维等值线数据

		List<Polygon> isoline = ContourLineUtil.getContourLine(mpValues, xMin, xMax, yMin, yMax, zMin, zMax, xNum, yNum ,contourValues,isolineLevel,color);

		//获取二维等值线数据
		//Contour2d polylines = Contour2DUtils.get2DContour(isoline);

		Map<String,Object> map = new HashMap<>();

		map.put("isoline", isoline);

		map.put("contourValues", contourValues);

		map.put("zMin", isolineLevel[0]);
		map.put("zMax", isolineLevel[1]);
		return map;
	}

	/**
	 * @Title: distinct
	 * @Description: double集合去重
	 * @param: @param vList
	 * @param: @return
	 * @return: List<Double>
	 */
	public static List<Double> distinct(List<Double> vList){
		vList=new ArrayList<>(new TreeSet<>(vList));
		return vList;
	}
}
