package org.akxy.zhky.manage.absciss.service;

import java.util.List;

import org.akxy.zhky.manage.pojo.AbscissLayerMp;
import org.akxy.zhky.manage.pojo.IndexTable;

/**
 * @Description:顶板离层测点信息
 * @date: 2018年7月3日
 */
public interface IAbscissLayerMpInfoService {
	/**
	 * @Title: getStressNowSchemeMp
	 * @Description: 获取顶板离层指定区域的当前方案测点
	 * @param: @param areaId
	 * @param: @return
	 * @return: List<IndexTable>
	 */
	public List<IndexTable> getAbscissLayerNowSchemeMp(int areaId);
	/**
	 * @Title: getStressAllMp
	 * @Description: 获取顶板离层指定区域的所有方案
	 * @param: @param areaId
	 * @param: @return
	 * @return: List<IndexTable>
	 */
	public List<IndexTable> getAbscissLayerAllMp(int areaId);
	/**
	 * @Title: getAbscissLayerMpById
	 * @Description: 根据应力测点id获取应力测点信息
	 * @param: @param mpId
	 * @param: @return
	 * @return: AbscissLayerMp
	 */
	public AbscissLayerMp getAbscissLayerMpById(Integer mpId);
	/**
	 * @Title: getAbscissLayerMpByName
	 * @Description: 根据离层测点name获取测点信息
	 * @param: @param mpName
	 * @param: @return
	 * @return: StressMp
	 */
	public AbscissLayerMp getAbscissLayerMpByName(String mpName,int areaId);

	/**
	 * @Title: getAbscissLayerMpBySect
	 * @Description: 根据起止时间，区域id，获取相应的测点
	 * @param: @param stime
	 * @param: @param etime
	 * @param: @param areaId
	 * @param: @return
	 * @return: int[]
	 */
	public int[] getAbscissLayerMpBySect(String stime , String etime , int areaId,int upTunnelId,int downTunnelId);
}
