package org.akxy.zhky.manage.footage.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.akxy.zhky.manage.pojo.Footage;

/**
 * @ClassName: IFootageService.java
 * @Description: 进尺管理
 * @date: 2018年6月12日
 */

public interface IFootageService {
	/**
	 * @Title: getAllFootage
	 * @Description: 按照巷道Id查询该区域的所有进尺信息
	 * @param: @param areaId
	 * @param: @return
	 * @return: List<Footage>
	 * @throws
	 */
	public List<Footage> getAllFootage(int tunnelId);

	/**
	 * @Title: addFootage
	 * @Description: 增加进尺,返回进尺id
	 * @param: @param footage
	 * @param: @return
	 * @return: Integer
	 * @throws
	 */
	public Integer addFootage(Footage footage);

	/**
	 * @Title: deleteFootage
	 * @Description: 删除进尺
	 * @param: @param id
	 * @param: @return
	 * @return: boolean
	 * @throws
	 */
	public boolean deleteFootage(int id);

	/**
	 * @Title: getFootageByTimeAndId
	 * @Description: 根据日期（日）和巷道id，获取进尺信息
	 * @param: @param time
	 * @param: @param tunnelId
	 * @param: @return
	 * @return: Footage
	 * @throws ParseException
	 */
	public Footage getFootageByTimeAndId(String time,int tunnelId) throws ParseException;

	/**
	 * @Title: getMonthTotalByTunnelIdAndMonth
	 * @Description: 根据月份（yyyy-MM）和巷道id获取当月累计进尺
	 * @param: @param monthTim
	 * @param: @param tunnelId
	 * @param: @return
	 * @return: float
	 * @throws ParseException
	 */
	public Double getMonthTotalByTunnelIdAndMonth(String monthTime,String dayTime,int tunnelId) throws ParseException;

	public Map<String,Object> getReportFootage(String time ,int upTunnelId,int belowTunnelId) throws ParseException;

	public Double getALLfootageByTunnelId(int tunnelId);

	public Integer isFootageExist(int tunnelId, String time) throws ParseException;

}
