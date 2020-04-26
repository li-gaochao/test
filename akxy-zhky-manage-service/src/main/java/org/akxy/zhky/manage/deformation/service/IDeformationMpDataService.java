package org.akxy.zhky.manage.deformation.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.akxy.zhky.manage.pojo.DeformationMpData;
import org.akxy.zhky.manage.pojo.IndexTable;

/**
 * @ClassName: IDeformationService.java
 * @Description: 巷道变形数据服务
 * @date: 2018年6月7日
 */
public interface IDeformationMpDataService {
	/**
	 * @Title: getDeformationMpInfo
	 * @Description: 巷道变形的基本数据查询
	 * @param: @param areaId	区域id
	 * @param: @param mpIds		测点id集合
	 * @param: @param stime		开始时间
	 * @param: @param etime		结束时间
	 * @param: @param spressure	最小压力
	 * @param: @param epressure	最大压力
	 * @param: @return
	 * @return: List<DeformationMpinfo>
	 */
	public List<DeformationMpData> getDeformationMpData(Long frequency,List<Integer> mpIds,String stime,String etime,Float lowLength,Float heighLength) throws ParseException;
	/**
	 * @Title: getDeformationMpDataWithTimeList
	 * @Description: 巷道变形的基本数据查询，指定的时间点list内
	 * @param: @param areaId
	 * @param: @param mpIds
	 * @param: @param times
	 * @param: @return
	 * @return: List<DeformationMpData>
	 */
	public List<DeformationMpData> getDeformationMpDataWithTimeList(int areaId,List<Integer> mpIds,List<String> times,Float lowLength,Float heighLength) throws ParseException;

	/**
	 * @Title: getDeformationMpDataTableCount
	 * @Description: 获取巷道变形数据查询表格的总行数
	 * @param: @param areaId
	 * @param: @param mpIds
	 * @param: @param stime
	 * @param: @param etime
	 * @param: @return
	 * @param: @throws ParseException
	 * @return: int
	 */
	public List<String> getDeformationMpDataTableColumns(int areaId,List<Integer> mpIds,String stime,String etime,Float lowLength,Float heighLength) throws ParseException;


	/**
	 * @Title: getWarning
	 * @Description: 获取一段时间内指定测点和时间的预警状态表
	 * @param: @param areaId
	 * @param: @param fresh
	 * @param: @param mpIds
	 * @param: @return
	 * @return: List<IndexTable>
	 */
	public List<IndexTable> getWarning (int areaId,String stime ,String etime, List<Integer> mpIds , int startrow , int pagesize);

	/**
	 * @Title: getReportData
	 * @Description: 构造巷道变形报表的基本数据表
	 * @param: @param time		'2018-01-01'
	 * @param: @param mpIds		测点id list
	 * @param: @return
	 * @return: List<Map<String,Object>>
	 */
	public List<Map<String,Object>> getReportData(List<Integer> mpIdList, String stime, String etime);

	/**
	 * @Title: getReportMaxValueTime
	 * @Description: 报表数据中，最大值出现的最早时刻  00:00:00
	 * @param: @param time
	 * @param: @param mpIds
	 * @param: @return
	 * @return: String
	 */
	public String getReportMaxValueTime(String stime ,String etime,List<Integer> mpIds);

	/**
	 * @Title: getReportGraphData
	 * @Description: 用最大时刻查询所有测点值
	 * @param: @param time
	 * @param: @param mpIds
	 * @param: @return
	 * @return: List<Map<String,Object>>
	 */
	public List<Map<String,Object>> getReportGraphData(String time,List<Integer> mpIds);

	/**
	 * @Title: getDataCount
	 * @Description: 查询指定条件的数据量
	 * @param: @param areaId
	 * @param: @param stime
	 * @param: @param etime
	 * @param: @param mpIds
	 * @param: @return
	 * @return: int
	 */
	public int getDataCount(int areaId, String stime ,String etime, List<Integer> mpIds);

	public List<Map<String,Object>> getAllWarnData(int mpId,String stime,String etime);

	public Map<String,Object> getSumReportData(int areaId,String stime,String etime);

	List<Map<String,Object>> getDeformationLineBaseData(int lineType, int mpId,String stime ,String etime) throws ParseException ;


}
