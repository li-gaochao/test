package org.akxy.zhky.manage.anchor.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.akxy.zhky.manage.pojo.AnchorMpData;
import org.akxy.zhky.manage.pojo.IndexTable;

/**
 * @ClassName: IAnchorService.java
 * @Description: 钻孔锚杆索数据服务
 * @date: 2018年6月7日
 */
public interface IAnchorMpDataService {
	/**
	 * @Title: getAnchorMpInfo
	 * @Description: 钻孔锚杆索的基本数据查询
	 * @param: @param areaId	区域id
	 * @param: @param mpIds		测点id集合
	 * @param: @param stime		开始时间
	 * @param: @param etime		结束时间
	 * @param: @param spressure	最小压力
	 * @param: @param epressure	最大压力
	 * @param: @return
	 * @return: List<AnchorMpinfo>
	 */
	public List<AnchorMpData> getAnchorMpData(Long frequency,List<Integer> mpIds,String stime,String etime) throws ParseException;
	/**
	 * @Title: getAnchorMpDataWithTimeList
	 * @Description: 钻孔锚杆索的基本数据查询，指定的时间点list内
	 * @param: @param areaId
	 * @param: @param mpIds
	 * @param: @param times
	 * @param: @return
	 * @return: List<AnchorMpData>
	 */
	public List<AnchorMpData> getAnchorMpDataWithTimeList(int areaId,List<Integer> mpIds,List<String> times) throws ParseException;

	/**
	 * @Title: getAnchorMpDataTableCount
	 * @Description: 获取锚杆索数据查询表格的总行数
	 * @param: @param areaId
	 * @param: @param mpIds
	 * @param: @param stime
	 * @param: @param etime
	 * @param: @return
	 * @param: @throws ParseException
	 * @return: int
	 */
	public List<String> getAnchorMpDataTableColumns(int areaId,List<Integer> mpIds,String stime,String etime) throws ParseException;


	/**
	 * @Title: getAnchorLineData
	 * @Description: 钻孔锚杆索的曲线数据查询
	 * @param: @param areaId	区域id
	 * @param: @param mpIds		测点id集合
	 * @param: @param stime		开始时间
	 * @param: @param etime		结束时间
	 * @param: @param spressure	最小压力
	 * @param: @param epressure	最大压力
	 * @param: @return
	 * @return: List<AnchorMpinfo>
	 */
	public List<AnchorMpData> getAnchorLineData(List<Integer> mpIds,String stime,String etime);

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
	 * @Description: 构造锚杆索报表的基本数据表
	 * @param: @param time		'2018-01-01'
	 * @param: @param mpIds		测点id list
	 * @param: @return
	 * @return: List<Map<String,Object>>
	 */
	public List<Map<String,Object>> getReportData(String stime,String etime,List<Integer> mpIds);

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

	public List<AnchorMpData> getAllWarnData(int mpId,String startTime,String endTime);

	List<Map<String,Object>> getAnchorLineBaseData(int lineType, int mpId,String stime ,String etime) throws ParseException ;

}
