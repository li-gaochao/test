package org.akxy.zhky.manage.absciss.service;

import java.util.List;
import java.util.Map;

import org.akxy.zhky.manage.pojo.AbscissLayerMpData;

/**
 * @Description: 顶板离层报表
 * @date: 2018年9月19日
 */
public interface IAbscissLayerReportService {
	/**
	 * @Title: getWarmData
	 * @Description: 获取报表中的预警数据
	 * @param: @param mpList
	 * @param: @param warmBaseData
	 * @param: @return
	 * @return: List<Map<String,Object>>
	 */
	public List<Map<String,Object>> getWarmData(List<Integer> mpList,List<AbscissLayerMpData> warmBaseData);
}
