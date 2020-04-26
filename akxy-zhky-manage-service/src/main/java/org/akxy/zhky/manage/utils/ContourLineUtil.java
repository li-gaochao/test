package org.akxy.zhky.manage.utils;
import java.util.List;

import org.akxy.zhky.manage.pojo.MpValueEntity;
import org.akxy.zhky.manage.utils.wContour.Global.Polygon;


/**
 * @Description:
 * @date: 2018年8月16日
 */
//新建一个实体x，y，value
public class ContourLineUtil {
	public static List<Polygon> getContourLine(List<MpValueEntity> gsd, double xMin, double xMax, double yMin,
			double yMax, double zMin, double zMax, int xNum, int yNum , double[] contourValues,double[] isolineLevel,int color) {
		int size = gsd.size();
		double[] xs = new double[size];
		double[] ys = new double[size];
		double[] values = new double[size];

		double undefValue = -5;
		for (int i = 0; i < size; i++) {
			MpValueEntity gsData = gsd.get(i);
			xs[i] = gsData.getX() ;
			ys[i] = gsData.getY() ;
			values[i] = gsData.getValue();
		}

		//裁剪 一个面首尾相连五个点
		double[] clipXs = new double[]{xMin-1,xMin-1,xMax+1,xMax+1,xMin-1};

		double[] clipYs = new double[]{yMin-1,yMax+1,yMax+1,yMin-1,yMin-1};

		List<Polygon> polygons = null;
		try {
			polygons = ContourUtils.genereateContour(xs, ys, values, contourValues, xMin, xMax, yMin, yMax, xNum, yNum, undefValue, clipXs, clipYs,isolineLevel,color);
		} catch (Exception e) {
			e.printStackTrace();

		}
		return polygons;
	}

}
