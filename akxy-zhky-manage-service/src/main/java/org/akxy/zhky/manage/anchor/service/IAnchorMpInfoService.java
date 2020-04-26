package org.akxy.zhky.manage.anchor.service;

import java.util.List;

import org.akxy.zhky.manage.pojo.AnchorMp;
import org.akxy.zhky.manage.pojo.IndexTable;

/**
 * @ClassName: IAnchorMpInfoService.java
 * @Description: 钻孔锚杆索测点服务
 * @date: 2018年7月3日
 */
public interface IAnchorMpInfoService {
	/**
	 * @Title: getAnchorNowSchemeMp
	 * @Description: 根据区域id获取锚杆索的当前方案所有测点及测点的锚杆索值
	 * @param: @param areaId
	 * @param: @return
	 * @return: List<IndexTable>
	 */
	public List<IndexTable> getAnchorNowSchemeMp(int areaId);

	/**
	 * @Title: getAnchorAllMp
	 * @Description: 根据区域id获取锚杆索的所有测点
	 * @param: @param areaId
	 * @param: @return
	 * @return: List<IndexTable>
	 */
	public List<IndexTable> getAnchorAllMp(int areaId);

	/**
	 * @Title: getAnchorMpById
	 * @Description: 根据锚杆索测点id获取锚杆索测点信息
	 * @param: @param mpId
	 * @param: @return
	 * @return: AnchorMp
	 */
	public AnchorMp getAnchorMpById(Integer mpId);
	/**
	 * @Title: getAnchorMpByName
	 * @Description: 根据锚杆索测点name获取锚杆索测点信息
	 * @param: @param mpName
	 * @param: @return
	 * @return: AnchorMp
	 */
	public AnchorMp getAnchorMpByName(String mpName,int areaId);

	/**
	 * @Title: getAnchorMpBySect
	 * @Description: 根据起止时间，区域id，获取相应的测点
	 * @param: @param stime
	 * @param: @param etime
	 * @param: @param areaId
	 * @param: @return
	 * @return: int[]
	 */
	public int[] getAnchorMpBySect(String stime , String etime , int areaId,int upTunnelId,int downTunnelId);

}
