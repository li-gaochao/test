package org.akxy.zhky.manage.absciss.service.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.akxy.zhky.manage.pojo.AbscissLayerMpData;

import com.akxy.zhky.akxy_common.util.DateDifferUtils;

/**

 * @ClassName: SortList.java
 * @Description:
 *
 * @version: v1.0.0
 * @author: lgc
 * @date: 2018年9月20日
 */
public class SortList {
	/**
	 * 对AbscissLayerMpData list的数据；
	 * 按照value从大到小。采集时间从新到旧，进行排序；
	 * 返回最大值对象
	 * @param list
	 */
	public AbscissLayerMpData getMaxData(List<AbscissLayerMpData> list) {
		//按照时间从早到晚重新排序
		list = getSortLine(list);
		//按差值从大到小排序
		list = getSortValue(list);

		return list.get(0);

	}

	/**
	 * @Title: getSortLine
	 * @Description: 按照时间从早到晚进行排序
	 * @param: @param sub
	 * @param: @return
	 * @return: List<AbscissLayerMpData>
	 */
	public List<AbscissLayerMpData> getSortLine(List<AbscissLayerMpData> sub){
		List<AbscissLayerMpData> newSub = new ArrayList<AbscissLayerMpData>();
		Arraysort sort=new Arraysort(sub);
		//把坐标以三维数组的形式表现
		for(int j = 0 ; j < sub.size() ; j ++ ){
			Arrays.sort(sort.arr, new MyComprator());    //使用指定的排序器，进行排序
		}
		//输出排序结果
		for(int i=0;i<sort.arr.length;i++){
			newSub.add(sort.arr[i]);
		}
		return newSub;
	}


	/**
	 * @Title: getSortLine
	 * @Description: 按照时间从早到晚进行排序
	 * @param: @param sub
	 * @param: @return
	 * @return: List<AbscissLayerMpData>
	 */
	public List<AbscissLayerMpData> getSortValue(List<AbscissLayerMpData> sub){
		List<AbscissLayerMpData> newSub = new ArrayList<AbscissLayerMpData>();
		Arraysort sort=new Arraysort(sub);
		for(int j = 0 ; j < sub.size() ; j ++ ){
			Arrays.sort(sort.arr, new MyComprator2());    //使用指定的排序器，进行排序
		}
		//输出排序结果
		for(int i=0;i<sort.arr.length;i++){
			newSub.add(sort.arr[i]);
		}
		return newSub;
	}


	class Arraysort {
		AbscissLayerMpData[] arr;
		Arraysort(){}
		Arraysort(List<AbscissLayerMpData> points){
			arr=new AbscissLayerMpData[points.size()];    //定义对象数组arr，并分配存储的空间
			for(int i=0;i<points.size();i++)
				arr[i]=points.get(i);
		}
	}

	//比较器，按照时间从早到晚排序
	class MyComprator implements Comparator<Object> {
		@Override
		public int compare(Object arg0, Object arg1) {
			AbscissLayerMpData t1=(AbscissLayerMpData)arg0;
			AbscissLayerMpData t2=(AbscissLayerMpData)arg1;

			Date startDate = t1.getAcquisitionTime();
			Date endDate = t2.getAcquisitionTime();
			long diff = DateDifferUtils.getMinutePoor(startDate, endDate);

			return diff> 0 ? 1:-1;

		}
	}


	//比较器，按照差值绝对值从大到小排序
	class MyComprator2 implements Comparator<Object> {
		@Override
		public int compare(Object arg0, Object arg1) {
			AbscissLayerMpData t1=(AbscissLayerMpData)arg0;
			AbscissLayerMpData t2=(AbscissLayerMpData)arg1;

			Double t1_value1 = t1.getValue1().doubleValue();
			Double t1_value2 = t1.getValue2().doubleValue();
			Double t1_value = Math.abs(t1_value1 - t1_value2);

			Double t2_value1 = t2.getValue1().doubleValue();
			Double t2_value2 = t2.getValue2().doubleValue();
			Double t2_value = Math.abs(t2_value1 - t2_value2);

			Double differ = t1_value - t2_value;

			return differ> 0 ? 1:-1;

		}
	}

}
