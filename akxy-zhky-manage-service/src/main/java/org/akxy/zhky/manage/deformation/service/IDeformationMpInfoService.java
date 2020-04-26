package org.akxy.zhky.manage.deformation.service;

import java.util.List;

import org.akxy.zhky.manage.pojo.DeformationMp;
import org.akxy.zhky.manage.pojo.IndexTable;

/**
 * @ClassName: IDeformationMpInfoService.java
 * @Description: 巷道变形测点服务
 * @date: 2018年7月3日
 */
public interface IDeformationMpInfoService {
	/**
	 * @Title: getDeformationNowSchemeMp
	 * @Description: 根据区域id获取巷道变形的当前方案所有测点及测点的巷道变形值
	 * @param: @param areaId
	 * @param: @return
	 * @return: List<IndexTable>
	 */
	public List<IndexTable> getDeformationNowSchemeMp(int areaId);

	/**
	 * @Title: getDeformationAllMp
	 * @Description: 根据区域id获取巷道变形的所有测点
	 * @param: @param areaId
	 * @param: @return
	 * @return: List<IndexTable>
	 */
	public List<IndexTable> getDeformationAllMp(int areaId);

	/**
	 * @Title: getDeformationMpById
	 * @Description: 根据巷道变形测点id获取巷道变形测点信息
	 * @param: @param mpId
	 * @param: @return
	 * @return: DeformationMp
	 */
	public DeformationMp getDeformationMpById(Integer mpId);
	/**
	 * @Title: getDeformationMpByName
	 * @Description: 根据巷道变形测点name获取巷道变形测点信息
	 * @param: @param mpName
	 * @param: @return
	 * @return: DeformationMp
	 */
	public DeformationMp getDeformationMpByName(String mpName,int areaId);

	/**
	 * @Title: getDeformationMpBySect
	 * @Description: 根据起止时间，区域id，获取相应的测点
	 * @param: @param stime
	 * @param: @param etime
	 * @param: @param areaId
	 * @param: @return
	 * @return: int[]
	 */
	public int[] getDeformationMpBySect(String stime , String etime , int areaId,int upTunnelId,int downTunnelId);

}
