package org.akxy.zhky.manage.singlesupport.service;

import java.util.List;

import org.akxy.zhky.manage.pojo.IndexTable;
import org.akxy.zhky.manage.pojo.SingleSupportMp;

/**
 * @ClassName: ISingleSupportMpInfoService.java
 * @Description: 钻孔锚杆索测点服务
 * @date: 2018年7月3日
 */
public interface ISingleSupportMpInfoService {
	/**
	 * @Title: getSingleSupportNowSchemeMp
	 * @Description: 根据区域id获取锚杆索的当前方案所有测点及测点的锚杆索值
	 * @param: @param areaId
	 * @param: @return
	 * @return: List<IndexTable>
	 */
	public List<IndexTable> getSingleSupportNowSchemeMp(int areaId);

	/**
	 * @Title: getSingleSupportAllMp
	 * @Description: 根据区域id获取锚杆索的所有测点
	 * @param: @param areaId
	 * @param: @return
	 * @return: List<IndexTable>
	 */
	public List<IndexTable> getSingleSupportAllMp(int areaId);

	/**
	 * @Title: getSingleSupportMpById
	 * @Description: 根据锚杆索测点id获取锚杆索测点信息
	 * @param: @param mpId
	 * @param: @return
	 * @return: SingleSupportMp
	 */
	public SingleSupportMp getSingleSupportMpById(Integer mpId);
	/**
	 * @Title: getSingleSupportMpByName
	 * @Description: 根据锚杆索测点name获取锚杆索测点信息
	 * @param: @param mpName
	 * @param: @return
	 * @return: SingleSupportMp
	 */
	public SingleSupportMp getSingleSupportMpByName(String mpName,int areaId);

	/**
	 * @Title: getSingleSupportMpBySect
	 * @Description: 根据起止时间，区域id，获取相应的测点
	 * @param: @param stime
	 * @param: @param etime
	 * @param: @param areaId
	 * @param: @return
	 * @return: int[]
	 */
	public int[] getSingleSupportMpBySect(String stime , String etime , int areaId,int upTunnelId,int downTunnelId);

}
