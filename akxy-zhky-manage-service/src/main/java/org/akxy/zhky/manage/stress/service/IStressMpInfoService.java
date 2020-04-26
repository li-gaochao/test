package org.akxy.zhky.manage.stress.service;

import java.util.List;

import org.akxy.zhky.manage.pojo.IndexTable;
import org.akxy.zhky.manage.pojo.StressMp;

/**
 * @ClassName: IStressMpInfoService.java
 * @Description: 钻孔应力测点服务
 * @date: 2018年7月3日
 */
public interface IStressMpInfoService {
	/**
	 * @Title: getStressNowSchemeMp
	 * @Description: 根据区域id获取应力的当前方案所有测点及测点的应力值
	 * @param: @param areaId
	 * @param: @return
	 * @return: List<IndexTable>
	 */
	public List<IndexTable> getStressNowSchemeMp(int areaId);

	/**
	 * @Title: getStressAllMp
	 * @Description: 根据区域id获取应力的所有测点
	 * @param: @param areaId
	 * @param: @return
	 * @return: List<IndexTable>
	 */
	public List<IndexTable> getStressAllMp(int areaId);

	/**
	 * @Title: getStressMpById
	 * @Description: 根据应力测点id获取应力测点信息
	 * @param: @param mpId
	 * @param: @return
	 * @return: StressMp
	 */
	public StressMp getStressMpById(Integer mpId);
	/**
	 * @Title: getStressMpByName
	 * @Description: 根据应力测点name获取应力测点信息
	 * @param: @param mpName
	 * @param: @return
	 * @return: StressMp
	 */
	public StressMp getStressMpByName(String mpName,int areaId);

	/**
	 * @Title: getStressMpBySect
	 * @Description: 根据起止时间，区域id，获取相应的测点
	 * @param: @param stime
	 * @param: @param etime
	 * @param: @param areaId
	 * @param: @return
	 * @return: int[]
	 */
	public int[] getStressMpBySect(String stime , String etime , int areaId,int upTunnelId,int downTunnelId);

	public Float getMinDepthByTunnelId(int tunnelId);

	public Float getMaxDepthByTunnelId(int tunnelId);

	public List<StressMp> getMpListByTimeTunnelIdDepth(String time,int areaId, int tunnelId,float depth);
}
