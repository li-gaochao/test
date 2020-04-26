package org.akxy.zhky.manage.utils;

import java.util.ArrayList;
import java.util.List;

import org.akxy.zhky.manage.utils.wContour.Contour;
import org.akxy.zhky.manage.utils.wContour.Interpolate;
import org.akxy.zhky.manage.utils.wContour.Global.Border;
import org.akxy.zhky.manage.utils.wContour.Global.PointD;
import org.akxy.zhky.manage.utils.wContour.Global.PolyLine;
import org.akxy.zhky.manage.utils.wContour.Global.Polygon;


public class ContourUtilsCollSupport {

	/**
	 * 拟合自动站数据数据为Polygon
	 *
	 * @param xs            lon数组
	 * @param ys            lat数组
	 * @param values        值
	 * @param contourValues 拟合的值范围
	 * @param xMin          最小x
	 * @param xMax          最大x
	 * @param yMin          最小y
	 * @param yMax          最大y
	 * @param xNum          行数
	 * @param yNum          列数
	 * @param undefValue    无效值
	 * @param clipXs        边界线x数组
	 * @param clipYs        边界线y数组
	 * @throws Exception
	 */
	public static List<Polygon> genereateContour(
			double[] xs, double[] ys, double[] values, double[] contourValues, double xMin,
			double xMax, double yMin, double yMax, int xNum, int yNum, double undefValue,
			double[] clipXs, double[] clipYs, double[] isolineLevel) throws Exception {

		if (xs == null || ys == null || values == null) {
			return null;
		}
		double[][] discreteData = null;
		double[][] gridData = new double[yNum][xNum];
		double[] gridX = new double[xNum];
		double[] gridY = new double[yNum];
		List<PointD> clipPList = new ArrayList<PointD>();
		List<Border> borders = new ArrayList<Border>();
		List<PolyLine> contourLines = new ArrayList<PolyLine>();
		List<Polygon> contourPolygons = new ArrayList<Polygon>();
		discreteData = new double[xs.length][3];
		for (int i = 0; i < xs.length; i++) {
			discreteData[i][0] = xs[i];
			discreteData[i][1] = ys[i];
			discreteData[i][2] = values[i];
		}
		// 创建栅格坐标
		Interpolate.createGridXY_Num(xMin,yMin, xMax, yMax, gridX, gridY);

		gridData = Interpolate.interpolation_IDW_Neighbor(discreteData, gridX,
				gridY,8, undefValue);
		// 追踪等值线
		int nc = contourValues.length;

		int[][] S1 = new int[gridData.length][gridData[0].length];
		borders = Contour.tracingBorders(gridData, gridX, gridY, S1, undefValue);
		contourLines = Contour.tracingContourLines(gridData, gridX, gridY, nc,
				contourValues, undefValue, borders, S1);
		contourLines = Contour.smoothLines(contourLines);
		contourPolygons = Contour.tracingPolygons(gridData, contourLines,
				borders, contourValues);

		if (clipXs == null && clipYs == null) {
		} else {
			for (int i = 0; i < clipXs.length; i++) {
				clipPList.add(new PointD(clipXs[i], clipYs[i]));
			}
			contourPolygons = Contour.clipPolygons(contourPolygons, clipPList);
		}

		//给每个Polygon设置颜色
		for(int i = 0 ;i < contourPolygons.size();i++){
			RGB rgb = getRGB(contourPolygons.get(i).LowValue, isolineLevel[0], isolineLevel[1]);
			contourPolygons.get(i).setRGB(new int[]{rgb.r,rgb.g,rgb.b});
		}
		return contourPolygons;
	}

	public static List<Polygon> genCountourLines(double[] xs,
			double[] ys, double[] values, double[] contourValues, double xMin, double xMax,
			double yMin, double yMax, int xNum, int yNum, double undefValue, double[] clipXs, double[] clipYs, boolean type, double[] isolineLevel) {

		double[][] discreteData = null;
		double[][] gridData = new double[yNum][xNum];
		double[] gridX = new double[xNum];
		double[] gridY = new double[yNum];
		List<PointD> clipPList = new ArrayList<PointD>();
		List<Border> borders = new ArrayList<Border>();
		List<PolyLine> contourLines = new ArrayList<PolyLine>();
		List<Polygon> contourPolygons = new ArrayList<Polygon>();
		List<Polygon> clippedPolygons = new ArrayList<Polygon>();

		discreteData = new double[xs.length][3];
		for (int i = 0; i < xs.length; i++) {
			discreteData[i][0] = xs[i];
			discreteData[i][1] = ys[i];
		}
		// 创建栅格坐标
		Interpolate.createGridXY_Num(xMin, yMin, xMax, yMax, gridX, gridY);
		//温度的插值方法  缺少按经度搜索的参数  表示 在每个格点中插值时不按经度进行搜索，而是按照全图进行搜索  速度较慢
		gridData = Interpolate.interpolation_IDW_Neighbor(discreteData, gridX, gridY, 10, undefValue);
		// 追踪等值线
		int nc = contourValues.length;
		int[][] S1 = new int[gridData.length][gridData[0].length];
		borders = Contour
				.tracingBorders(gridData, gridX, gridY, S1, undefValue);
		contourLines = Contour.tracingContourLines(gridData, gridX, gridY, nc,
				contourValues, undefValue, borders, S1);
		contourLines = Contour.smoothLines(contourLines);

		contourPolygons = Contour.tracingPolygons(gridData, contourLines,
				borders, contourValues);
		if (clipXs != null && clipYs != null) {
			for (int i = 0; i < clipXs.length; i++) {
				clipPList.add(new PointD(clipXs[i], clipYs[i]));
			}
			clippedPolygons = Contour.clipPolygons(contourPolygons, clipPList);
		} else {
			clippedPolygons = contourPolygons;
		}
		//给每个Polygon设置颜色
		for(int i = 0 ;i < clippedPolygons.size();i++){
			RGB rgb = getRGB(clippedPolygons.get(i).LowValue, isolineLevel[0], isolineLevel[1]);
			clippedPolygons.get(i).setRGB(new int[]{rgb.r,rgb.g,rgb.b});
		}
		return clippedPolygons;
	}

	public static List<Polygon> genCountourLinesEary(double[] xs,
			double[] ys, double[] values, double[] contourValues, double xMin, double xMax,
			double yMin, double yMax, int xNum, int yNum, double undefValue, double[] clipXs, double[] clipYs, boolean type, double[] isolineLevel) {
		double[][] discreteData = null;
		double[][] gridData = new double[yNum][xNum];
		double[] gridX = new double[xNum];
		double[] gridY = new double[yNum];
		List<PointD> clipPList = new ArrayList<PointD>();
		List<Border> borders = new ArrayList<Border>();
		List<PolyLine> contourLines = new ArrayList<PolyLine>();
		List<Polygon> contourPolygons = new ArrayList<Polygon>();
		List<Polygon> clippedPolygons = new ArrayList<Polygon>();

		discreteData = new double[xs.length][3];
		for (int i = 0; i < xs.length; i++) {
			discreteData[i][0] = xs[i];
			discreteData[i][1] = ys[i];
			discreteData[i][2] = values[i];
		}
		// 创建栅格坐标
		Interpolate.createGridXY_Num(xMin, yMin, xMax, yMax, gridX, gridY);
		//温度的插值方法  缺少按经度搜索的参数  表示 在每个格点中插值时不按经度进行搜索，而是按照全图进行搜索  速度较慢
		gridData = Interpolate.interpolation_IDW_Neighbor(discreteData, gridX, gridY,50, undefValue);
		// 追踪等值线
		int nc = contourValues.length;
		int[][] S1 = new int[gridData.length][gridData[0].length];
		borders = Contour
				.tracingBorders(gridData, gridX, gridY, S1, undefValue);
		contourLines = Contour.tracingContourLines(gridData, gridX, gridY, nc,
				contourValues, undefValue, borders, S1);
		contourLines = Contour.smoothLines(contourLines);

		contourPolygons = Contour.tracingPolygons(gridData, contourLines,
				borders, contourValues);
		if (clipXs != null && clipYs != null) {
			for (int i = 0; i < clipXs.length; i++) {
				clipPList.add(new PointD(clipXs[i], clipYs[i]));
			}
			clippedPolygons = Contour.clipPolygons(contourPolygons, clipPList);
		} else {
			clippedPolygons = contourPolygons;
		}
		//给每个Polygon设置颜色
		for(int i = 0 ;i < clippedPolygons.size();i++){
			RGB rgb = getRGB(clippedPolygons.get(i).LowValue, isolineLevel[0], isolineLevel[1]);
			clippedPolygons.get(i).setRGB(new int[]{rgb.r,rgb.g,rgb.b});
		}
		return clippedPolygons;
	}


	//	public static RGB getRGB(double Value, double Yellow, double Red){
	//		RGB RGB = new RGB(0,70,255);
	//		if (Value > Red + 3)
	//		{
	//			return new RGB(230,0,0);
	//		}
	//		else
	//		{
	//			if (Value < Yellow/560.0)
	//			{
	//				//起步蓝色
	//				return new RGB(0,0,255);
	//			}
	//			else if (Value >= Yellow/357.0 && Value < Yellow*255/357.0)
	//			{
	//				for (int i = 1 ; i < 255 ; i++)
	//				{
	//					if (Value >= i*Yellow/357.0 && Value < (i+1)*Yellow/357.0)
	//					{
	//						//减弱蓝色
	//						return new RGB(0,i,255);
	//					}
	//				}
	//			}
	//
	//			else if (Value >= Yellow*255/357.0 && Value < Yellow*306/357.0)
	//			{
	//				for (int i = 0; i < 51 ; i++)
	//				{
	//					if (Value >= (i+255)*Yellow/357.0 && Value < (i+256)*Yellow/357.0)
	//					{
	//						//由蓝变绿
	//						return new RGB(0,255,255-i*5);
	//					}
	//				}
	//			}
	//
	//
	//
	//			else if (Value >= Yellow*306/357.0 && Value < Yellow)
	//			{
	//				for (int i = 0; i < 51 ; i++)
	//				{
	//					if (Value >= (i+306)*Yellow/357.0 && Value < (i+307)*Yellow/357.0)
	//					{
	//						//由绿变黄
	//						return new RGB(i*5,255,0);
	//					}
	//				}
	//			}
	//
	//
	//
	//			else if (Value >= Yellow && Value < Red)
	//			{
	//				for (int i = 0; i < 255 ; i++)
	//				{
	//					if (Value >= i*(Red - Yellow)/255.0 + Yellow && Value < (i+1)*(Red - Yellow)/255.0 + Yellow)
	//					{
	//						//由黄变红
	//						return new RGB(255,255-i,0);
	//					}
	//				}
	//			}
	//			else if (Value <= Red + 3)
	//			{
	//				for (int i = 0; i < 25 ; i++)
	//				{
	//					if (Value >= i*(Red + 3)/25.0 + Red && Value < (i+1)*(Red + 3)/25.0 + Red)
	//					{
	//						//加深红色
	//						return new RGB(255-i,0,0);
	//					}
	//				}
	//			}
	//		}
	//		return RGB;
	//	}

	//	public static RGB getRGB(double Value, double Yellow, double Red){
	//		RGB RGB = new RGB(0,70,255);
	//		if (Value > Red + 3)
	//		{
	//			return new RGB(255,255,255);
	//		}
	//		else
	//		{
	//			if (Value < Yellow/560.0)
	//			{
	//				//起步蓝色
	//				return new RGB(255,255,255);
	//			}
	//			else if (Value >= Yellow/357.0 && Value < Yellow*255/357.0)
	//			{
	//				for (int i = 1 ; i < 255 ; i++)
	//				{
	//					if (Value >= i*Yellow/357.0 && Value < (i+1)*Yellow/357.0)
	//					{
	//						//减弱蓝色
	//						return new RGB(255,255,255);
	//					}
	//				}
	//			}
	//
	//			else if (Value >= Yellow*255/357.0 && Value < Yellow*306/357.0)
	//			{
	//				for (int i = 0; i < 51 ; i++)
	//				{
	//					if (Value >= (i+255)*Yellow/357.0 && Value < (i+256)*Yellow/357.0)
	//					{
	//						//由蓝变绿
	//						return new RGB(255,255,255);
	//					}
	//				}
	//			}
	//
	//
	//
	//			else if (Value >= Yellow*306/357.0 && Value < Yellow)
	//			{
	//				for (int i = 0; i < 51 ; i++)
	//				{
	//					if (Value >= (i+306)*Yellow/357.0 && Value < (i+307)*Yellow/357.0)
	//					{
	//						//由绿变黄
	//						return new RGB(255,255,255);
	//					}
	//				}
	//			}
	//
	//
	//
	//			else if (Value >= Yellow && Value < Red)
	//			{
	//				for (int i = 0; i < 100 ; i++)
	//				{
	//					if (Value >= i*(Red - Yellow)/100.0 + Yellow && Value < (i+1)*(Red - Yellow)/100.0 + Yellow)
	//					{
	//						//由黄变红
	//						return new RGB(255,100-i,0);
	//					}
	//				}
	//			}
	//			else if (Value <= Red + 3)
	//			{
	//				for (int i = 0; i < 25 ; i++)
	//				{
	//					if (Value >= i*(Red + 3)/25.0 + Red && Value < (i+1)*(Red + 3)/25.0 + Red)
	//					{
	//						//加深红色
	//						return new RGB(255-i,0,0);
	//					}
	//				}
	//			}
	//		}
	//		return RGB;
	//	}

	public static RGB getRGB(double Value, double Yellow, double Red){
		if (Value < Red)
		{
			return new RGB(255,255,255);
		}
		else
		{
			return new RGB(255,0,0);
		}
	}

}