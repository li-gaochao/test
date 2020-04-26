package org.akxy.zhky.manage.stress.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.akxy.zhky.manage.pojo.IndexTable;
import org.akxy.zhky.manage.pojo.StressMpData;

/**
 * @ClassName: IStressService.java
 * @Description: 钻孔应力数据服务
 * @date: 2018年6月7日
 */
public interface IStressMpDataService {
	/**
	 * @Title: getStressMpInfo
	 * @Description: 钻孔应力的基本数据查询
	 * @param: @param areaId	区域id
	 * @param: @param mpIds		测点id集合
	 * @param: @param stime		开始时间
	 * @param: @param etime		结束时间
	 * @param: @param spressure	最小压力
	 * @param: @param epressure	最大压力
	 * @param: @return
	 * @return: List<StressMpinfo>
	 */
	public List<StressMpData> getStressMpData(Long frequency,List<Integer> mpIds,String stime,String etime) throws ParseException;
	/**
	 * @Title: getStressMpDataWithTimeList
	 * @Description: 钻孔应力的基本数据查询，指定的时间点list内
	 * @param: @param areaId
	 * @param: @param mpIds
	 * @param: @param times
	 * @param: @return
	 * @return: List<StressMpData>
	 */
	public List<StressMpData> getStressMpDataWithTimeList(int areaId,List<Integer> mpIds,List<String> times) throws ParseException;

	/**
	 * @Title: getStressMpDataTableCount
	 * @Description: 获取应力数据查询表格的总行数
	 * @param: @param areaId
	 * @param: @param mpIds
	 * @param: @param stime
	 * @param: @param etime
	 * @param: @return
	 * @param: @throws ParseException
	 * @return: int
	 */
	public List<String> getStressMpDataTableColumns(int areaId,List<Integer> mpIds,String stime,String etime) throws ParseException;

	/**
	 * @Title: getStressLineData
	 * @Description: 获取应力曲线图数据
	 * @param: @param mpIds
	 * @param: @param stime
	 * @param: @param etime
	 * @param: @return
	 * @return: List<StressMpData>
	 */
	public List<StressMpData> getStressLineData(List<Integer> mpIds, String stime, String etime);

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
	 * @Description: 构造应力报表的基本数据表
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

	public List<StressMpData> getAllWarnData(int mpId,String startTime,String endTime);

	/**
	 * @Title: getAreaDistanceByMpIdAndDayTime
	 * @Description: 根据测点id和时间获取当天的距离工作面距离
	 * @param: @param areaId
	 * @param: @param dayTime
	 * @param: @return
	 * @return: Float
	 */
	Float getAreaDistanceByMpIdAndDayTime(int mpId, String dayTime);

	/**
	 * @Title: getMaxValueByMpIdAndDayTime
	 * @Description: 根据测点id和时间获取当日的最大数据
	 * @param: @param mpId
	 * @param: @param dayTime
	 * @param: @return
	 * @return: Float
	 */
	Float getMaxValueByMpIdAndDayTime(int mpId, String dayTime);

	/**
	 * @Title: getValueByMpIdAndTime
	 * @Description: 通过测点id和时间，获取距离这个时间最近的数据
	 * @param: @param mpId
	 * @param: @param time
	 * @param: @return
	 * @return: Float
	 */
	Map<String,Object> getValueByMpIdAndTime(int mpId,String time);

	List<Map<String,Object>> getStressLineDataNew(int mpId,String stime ,String etime) throws ParseException ;

	List<Map<String,Object>> getStressLineBaseData(int lineType, int mpId,String stime ,String etime) throws ParseException ;

}
