package org.akxy.zhky.manage.collsupport.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.akxy.zhky.manage.pojo.CollSupportData;
import org.akxy.zhky.manage.pojo.CollSupportMpData;
import org.akxy.zhky.manage.pojo.IndexTable;

/**
 * @ClassName: ICollSupportService.java
 * @Description: 钻孔支架数据服务
 * @date: 2018年6月7日
 */
public interface ICollSupportMpDataService {
	/**
	 * @Title: getCollSupportMpInfo
	 * @Description: 钻孔支架的基本数据查询
	 * @param: @param areaId	区域id
	 * @param: @param mpIds		测点id集合
	 * @param: @param stime		开始时间
	 * @param: @param etime		结束时间
	 * @param: @param spressure	最小压力
	 * @param: @param epressure	最大压力
	 * @param: @return
	 * @return: List<CollSupportMpinfo>
	 */
	public List<CollSupportMpData> getCollSupportMpData(Long frequency,List<Integer> mpIds,String stime,String etime,Float lowEnergy,Float heighEnergy) throws ParseException;
	/**
	 * @Title: getCollSupportMpDataWithTimeList
	 * @Description: 钻孔支架的基本数据查询，指定的时间点list内
	 * @param: @param areaId
	 * @param: @param mpIds
	 * @param: @param times
	 * @param: @return
	 * @return: List<CollSupportMpData>
	 */
	public List<CollSupportMpData> getCollSupportMpDataWithTimeList(int areaId,List<Integer> mpIds,List<String> times,Float lowEnergy, Float heighEnergy) throws ParseException;

	/**
	 * @Title: getCollSupportMpDataTableCount
	 * @Description: 获取支架数据查询表格的总行数
	 * @param: @param areaId
	 * @param: @param mpIds
	 * @param: @param stime
	 * @param: @param etime
	 * @param: @return
	 * @param: @throws ParseException
	 * @return: int
	 */
	public List<String> getCollSupportMpDataTableColumns(int areaId,List<Integer> mpIds,String stime,String etime, Float lowEnergy,Float heighEnergy) throws ParseException;


	/**
	 * @Title: getCollSupportMpLineData
	 * @Description: 支架的曲线数据查询
	 * @param: @param areaId	区域id
	 * @param: @param mpIds		测点id集合
	 * @param: @param stime		开始时间
	 * @param: @param etime		结束时间
	 * @param: @param spressure	最小压力
	 * @param: @param epressure	最大压力
	 * @param: @return
	 * @return: List<CollSupportMpinfo>
	 */
	public List<CollSupportMpData> getCollSupportMpLineData(List<Integer> mpIds,String stime,String etime,Float lowEnergy,Float heighEnergy);

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
	 * @Description: 构造支架报表的基本数据表
	 * @param: @param time		'2018-01-01'
	 * @param: @param mpIds		测点id list
	 * @param: @return
	 * @return: List<Map<String,Object>>
	 */
	public List<Map<String,Object>> getReportData(int areaId, String stime,String etime);

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

	/**
	 * @Title: getWeightLine
	 * @Description: 加权曲线
	 * @param: @param mpId
	 * @param: @param startTime
	 * @param: @param endTime
	 * @param: @param timespan
	 * @param: @return
	 * @return: int
	 */
	public List<Map<String,Object>> getWeightLine(int mpId,String startTime,String endTime,int timespan);

	public List<CollSupportMpData> getAllWarnData(int mpId,String startTime,String endTime);

	public List<CollSupportMpData> getAllAngleWarnData(int mpId,String startTime,String endTime);

	public List<CollSupportData> getCollSupportData(int mpId,String startTime,String endTime) throws ParseException;

	public Map<String, Object> getDataStatistics(int areaId,String startTime,String endTime);

	public Map<String,Object> getDayMaxValue(Integer supportNum, String stime ,String etime);

	public List<Map<String,Object>> getDayAverValue(List<Integer> supportNums, String stime ,String etime);

	public List<Map<String,Object>> getDayMaxValueWithProcedure(String etime ,String stime,Integer supportNum,Integer timeSpan);

	public List<Map<String,Object>> getReportExpanData(List<Map<String,Object>> bodyList,String stime,String etime) throws ParseException;

	List<Map<String,Object>> getCollSupportLineBaseData(int lineType, int mpId,String stime ,String etime) throws ParseException ;

}
