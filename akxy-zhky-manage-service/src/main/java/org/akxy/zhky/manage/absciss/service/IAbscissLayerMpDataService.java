package org.akxy.zhky.manage.absciss.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.akxy.zhky.manage.pojo.AbscissLayerMpData;
import org.akxy.zhky.manage.pojo.IndexTable;

/**
 * @Description:顶板离层
 * @date: 2018年6月8日
 */
public interface IAbscissLayerMpDataService {
	/**
	 * @throws ParseException
	 * @Title: getAbscissLayerMpData
	 * @Description: 顶板离层的基本数据查询
	 * @param: @param areaId	区域id
	 * @param: @param mpids		测点id集合
	 * @param: @param stime		开始时间
	 * @param: @param etime		结束时间
	 * @param: @param spressure	最小压力
	 * @param: @param epressure	最大压力
	 * @param: @return
	 * @return: List<AbscissLayerData>
	 * @throws
	 */
	public List<AbscissLayerMpData> getAbscissLayerMpData(int areaId,List<Integer> mpids,String stime,String etime) throws ParseException;
	/**
	 * @Title: getStressMpDataWithTimeList
	 * @Description: 钻孔应力的基本数据查询，指定的时间点list内
	 * @param: @param areaId
	 * @param: @param mpIds
	 * @param: @param times
	 * @param: @return
	 * @return: List<StressMpData>
	 * @throws
	 */
	public List<AbscissLayerMpData> getAbscissLayerMpDataWithTimeList(int areaId,List<Integer> mpIds,List<String> times) throws ParseException;

	/**
	 * @Title: getStressMpDataTableCount
	 * @Description: 获取应力数据查询表格的总行数
	 * @param: @param areaId
	 * @param: @param mpIds
	 * @param: @param stime
	 * @param: @param etime
	 * @param: @return
	 * @param: @throws ParseException
	 * @return: in
	 */
	public List<String> getAbscissLayerMpDataTableColumns(int areaId,List<Integer> mpIds,String stime,String etime) throws ParseException;


	/**
	 * @Title: getWarning
	 * @Description: 获取一段时间内指定测点和时间的预警状态表
	 * @param: @param areaId
	 * @param: @param fresh
	 * @param: @param mpIds
	 * @param: @return
	 * @return: List<IndexTable>
	 * @throws
	 */
	public List<IndexTable> getWarning (int areaId,String stime ,String etime, List<Integer> mpIds);

	/**
	 * @Title: getLongestMpData
	 * @Description: 获取各测点的起止时间点信息
	 * @param: @param areaId
	 * @param: @param stime
	 * @param: @param etime
	 * @param: @param mpIds
	 * @param: @return
	 * @return: AbscissLayerMpData
	 * @throws ParseException
	 */
	public List<Map<String,Object>> getLongestAndLatestMpData(int areaId,String stime,String etime, List<Integer> mpIds) throws ParseException;

	/**
	 * @Title: getAbscissLayerMpDataByMpId
	 * @Description: 根据测点id获取测点信息
	 * @param: @param areaId
	 * @param: @param mpId
	 * @param: @param stime
	 * @param: @param etime
	 * @param: @return
	 * @return: List<AbscissLayerMpData>
	 * @throws ParseException
	 */
	public List<AbscissLayerMpData> getAbscissLayerMpDataByMpId(Long frequency, List<Integer> mpList,String stime,String etime) throws ParseException;

	public List<AbscissLayerMpData> getAdepthAllWarnData(int mpId,String startTime,String endTime);

	public List<AbscissLayerMpData> getBdepthAllWarnData(int mpId,String startTime,String endTime);

	List<Map<String,Object>> getAbscissLineBaseData(int lineType, int mpId,String stime ,String etime) throws ParseException ;

}
