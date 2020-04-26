package org.akxy.zhky.manage.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.akxy.zhky.manage.pojo.MpValueEntity;


/**
 * @Description:
 * @date: 2019年4月9日
 */
public class EchartsHeatMapUtil {

	public static void main(String[] args){
		double x[]=new double[]{20,50,80,110,140,170,200};
		double y[]=new double[]{0,10,15,20};

		double value1[] = new double[]{33,78,12,34,54,66,99};
		double value2[] = new double[]{21,56,18,42,60,80,79};

		int xnum = 180;
		int ynum = 20;

		getPlaneData(x,y,value1,value2,xnum,ynum);
	}


	/**
	 * @Title: getPlaneData
	 * @Description: 获取echarts云图数据
	 * @param x			x坐标数组
	 * @param y			y坐标数组
	 * @param value1	深基点值数组
	 * @param value2	浅基点值数组
	 * @param xnum		x方向划分数量
	 * @param ynum		y方向划分数量
	 * @return
	 * @return: Result
	 */
	public static Map<String, Object> getPlaneData(double[] x, double[] y, double[] value1, double[] value2, int xnum,int ynum){

		double x0[]=new double[xnum];
		double xADD = (new BigDecimal(x[x.length-1]-x[0]).divide(new BigDecimal(xnum))).doubleValue();
		System.out.println("xADD:" + xADD);
		for(int i=0;i<xnum;i++){
			if(i==0){
				x0[i] = 0;
			}else{
				x0[i] = x0[i-1] + xADD;
			}
		}
		double y0[]=new double[ynum];
		double yADD = (new BigDecimal(y[y.length-1]-y[0]).divide(new BigDecimal(ynum))).doubleValue();
		System.out.println("yADD:" + yADD);
		for(int i=0;i<ynum;i++){
			if(i==0){
				y0[i] = 0;
			}else{
				y0[i] = y0[i-1] + yADD;
			}
		}

		double xv1[] = Lag(x, value1, x0);
		double xv2[] = Lag(x, value2, x0);

		List<double[]> res = new ArrayList<>();
		for(int i =0;i<xnum;i++){
			double y01 = 0;
			double y02 = xv1[i];
			double y03 = xv2[i];
			double y04 = 0;
			double yvalue[] = {y01,y02,y03,y04};
			double newvalue[] = Lag(y, yvalue, y0);
			for(int j=0;j<newvalue.length;j++){
				double point[] = new double[]{x0[i],y0[j],newvalue[j]};
				res.add(point);
			}
		}
		System.out.println(res.size());

		//		for(double d[]:res){
		//			System.out.println( d[0] + "," + d[1] + "," + d[2]);
		//		}
		Map<String, Object> resMap = new HashMap<>();
		resMap.put("xData", x0);
		resMap.put("yData", y0);
		resMap.put("Data", res);
		return resMap;
	}



	public static Map<String, Object> getPlaneData(List<MpValueEntity> mpValueList,double minDepth,double maxDepth){
		List<Double> xlist = new ArrayList<>();

		double[] y = new double[4];
		List<Double> value1list = new ArrayList<>();

		List<Double> value2list = new ArrayList<>();

		int xnum = 0;
		int ynum = 0;

		System.out.println("mpValueList.size:" + mpValueList.size());

		for(int i = 0 ; i < mpValueList.size() ; i ++){
			//			xlist.add(mpValueList.get(i).getX());
			//			if(i == 0){
			//				if(mpValueList.get(i).getY().equals(minDepth)){
			//					value1[i] = mpValueList.get(i).getValue();
			//					value2[i] = mpValueList.get(i+1).getValue();
			//				}else{
			//					value1[i] = mpValueList.get(i+1).getValue();
			//					value2[i] = mpValueList.get(i).getValue();
			//				}
			//			}else{
			//				if(i%2 == 1){
			//					value1[i] = mpValueList.get(i-1).getValue();
			//					value2[i] = mpValueList.get(i).getValue();
			//				}else{
			//					value1[i] = mpValueList.get(i).getValue();
			//					value2[i] = mpValueList.get(i+1).getValue();
			//				}
			//			}

			if(mpValueList.get(i).getY().equals(minDepth)){
				value1list.add(mpValueList.get(i).getValue());
				xlist.add(mpValueList.get(i).getX());
			}else{
				value2list.add(mpValueList.get(i).getValue());
			}

		}

		double[] x = new double[xlist.size()];
		double[] value1 = new double[value1list.size()];
		double[] value2 = new double[value2list.size()];

		System.out.println("xlist:" + xlist.size());
		System.out.println("value1list:" + value1list.size());
		System.out.println("value2list:" + value2list.size());

		for(int i = 0 ; i < xlist.size() ; i++){
			x[i] = new BigDecimal(xlist.get(i)).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		for(int i = 0 ; i < value1list.size() ; i++){
			value1[i] = value1list.get(i);
		}
		for(int i = 0 ; i < value2list.size() ; i++){
			value2[i] = value2list.get(i);
		}

		//		y[0] = 0;
		//		y[1] = minDepth;
		//		y[2] = maxDepth;
		//		y[3] = maxDepth + (maxDepth - minDepth);

		y[0] = 0;
		y[1] = minDepth;
		y[2] = minDepth + (maxDepth - minDepth) / 2;
		y[3] = maxDepth ;

		xnum = Math.abs(new BigDecimal(x[x.length-1]).subtract(new BigDecimal(x[0])).intValue());
		ynum = Math.abs(new BigDecimal(y[y.length-1]).subtract(new BigDecimal(y[0])).intValue());

		System.out.println("xnum:" + xnum);
		System.out.println("ynum:" + ynum);

		double x0[]=new double[xnum];
		//		double xADD = (new BigDecimal(xnum).divide(new BigDecimal(x.length))).doubleValue();
		double xADD = 1;
		System.out.println("xADD:" + xADD);
		for(int i=0;i<xnum;i++){
			if(i==0){
				x0[i] = x[0];
			}else{
				x0[i] = x0[i-1] + xADD;
			}
		}
		double y0[]=new double[ynum];
		double yADD = (new BigDecimal(y[3]-y[0]).divide(new BigDecimal(ynum))).doubleValue();
		System.out.println("yADD:" + yADD);
		for(int i=0;i<ynum;i++){
			if(i==0){
				y0[i] = y[0];
			}else{
				y0[i] = y0[i-1] + yADD;
			}
		}

		//		System.out.println("x00000000000000000000000000000000000000000:" + x.length);
		//		for(int i = 0 ; i < x.length; i++){
		//			System.out.println(x[i]);
		//		}
		//
		//
		//		System.out.println("value1111111111111111111111111111111111111:" + value1.length);
		//		for(int i = 0 ; i < value1.length ; i++){
		//			System.out.println(value1[i]);
		//		}
		//
		//		System.out.println("value2222222222222222222222222222222222222:" + value2.length);
		//		for(int i = 0 ; i < value2.length ; i++){
		//			System.out.println(value2[i]);
		//		}


		double xv1[] = Lag(x, value1, x0);
		double xv2[] = Lag(x, value2, x0);

		List<double[]> res = new ArrayList<>();
		for(int i =0;i<xnum;i++){
			double y01 = 0;
			double y02 = xv1[i];
			double y03 = xv2[i];
			double y04 = 0;
			double yvalue[] = {y01,y02,y03,y04};
			double newvalue[] = Lag(y, yvalue, y0);
			for(int j=0;j<newvalue.length;j++){
				double point[] = new double[]{x0[i],y0[j],newvalue[j]};
				res.add(point);
			}
		}
		System.out.println(res.size());

		//		for(double d[]:res){
		//			System.out.println( d[0] + "," + d[1] + "," + d[2]);
		//		}
		Map<String, Object> resMap = new HashMap<>();
		resMap.put("xData", x0);
		resMap.put("yData", y0);
		resMap.put("Data", res);
		return resMap;
	}


	private static double[] Lag(double x[],double y[],double x0[]){
		int m=x.length;
		int n=x0.length;
		double y0[]=new double[n];
		for(int ia=0;ia<n;ia++) {
			Double j=0.0;
			for(int ib=0;ib<m;ib++) {
				double k=1;
				for(int ic=0;ic<m;ic++) {
					if(ib!=ic){
						k=k*(x0[ia]-x[ic])/(x[ib]-x[ic]);
					}
				}
				k=k*y[ib];
				j=j+k;
			}
			if(j<0){
				y0[ia] = 0 ;
			}else{
				if(j.isNaN()||j.isInfinite()){
					j=0.0;
				}
				y0[ia]=new BigDecimal(j).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				//y0[ia]=j;
			}
		}
		return y0;
	}

	public static double[] arrayDistanct(double[] x){

		//实例化一个set集合
		Set<Double> set = new HashSet<>();
		//遍历数组并存入集合,如果元素已存在则不会重复存入
		for (int i = 0; i < x.length; i++) {
			set.add(x[i]);
		}
		//返回Set集合的数组形式
		Object[] obj = new Object[set.size()];
		obj = set.toArray();
		double[] newx = new double[set.size()];
		for(int i = 0 ; i < obj.length ; i++){
			newx[i] = (double) obj[i];
		}
		return newx;
	}

}
