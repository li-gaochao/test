package org.akxy.zhky.manage.collsupport.service;

import java.util.List;

import org.akxy.zhky.manage.pojo.CollSupportMp;
import org.akxy.zhky.manage.pojo.IndexTable;

/**
 * @ClassName: ICollSupportMpInfoService.java
 * @Description: 钻孔锚杆索测点服务
 * @date: 2018年7月3日
 */
public interface ICollSupportMpInfoService {
	/**
	 * @Title: getCollSupportNowSchemeMp
	 * @Description: 根据区域id获取锚杆索的当前方案所有测点及测点的锚杆索值
	 * @param: @param areaId
	 * @param: @return
	 * @return: List<IndexTable>
	 */
	public List<IndexTable> getCollSupportNowSchemeMp(int areaId);

	/**
	 * @Title: getCollSupportAllMp
	 * @Description: 根据区域id获取锚杆索的所有测点
	 * @param: @param areaId
	 * @param: @return
	 * @return: List<IndexTable>
	 */
	public List<IndexTable> getCollSupportAllMp(int areaId);

	/**
	 * @Title: getCollSupportMpById
	 * @Description: 根据锚杆索测点id获取锚杆索测点信息
	 * @param: @param mpId
	 * @param: @return
	 * @return: CollSupportMp
	 */
	public CollSupportMp getCollSupportMpById(Integer mpId);
	/**
	 * @Title: getCollSupportMpByName
	 * @Description: 根据锚杆索测点name获取锚杆索测点信息
	 * @param: @param mpName
	 * @param: @return
	 * @return: CollSupportMp
	 */
	public CollSupportMp getCollSupportMpByName(String mpName,int areaId);

	/**
	 * @Title: getCollSupportMpBySect
	 * @Description: 根据起止时间，区域id，获取相应的测点
	 * @param: @param stime
	 * @param: @param etime
	 * @param: @param areaId
	 * @param: @return
	 * @return: int[]
	 */
	public int[] getCollSupportMpBySect(String stime , String etime , int areaId,int upTunnelId,int downTunnelId);

}
