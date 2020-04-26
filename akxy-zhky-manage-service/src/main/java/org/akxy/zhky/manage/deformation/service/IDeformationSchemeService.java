package org.akxy.zhky.manage.deformation.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.akxy.zhky.manage.pojo.DeformationMp;
import org.akxy.zhky.manage.pojo.DeformationMpinfo;
import org.akxy.zhky.manage.pojo.DeformationSiKey;
import org.akxy.zhky.manage.pojo.DeformationSl;

/**
 * @ClassName: IDeformationSchemeServiceImpl.java
 * @Description:
 * @date: 2018年8月27日
 */
public interface IDeformationSchemeService {
	/**
	 * @Title: getDeformationSl
	 * @Description: 根据区域id,获取巷道变形的方案列表
	 * @param: @param areaId
	 * @param: @return
	 * @return: List<DeformationSl>
	 */
	public List<DeformationSl> getDeformationSl(int areaId);

	/**
	 * @Title: getDeformationSlByName
	 * @Description: 根据方案名称获得sl方案
	 * @param: @param name
	 * @param: @return
	 * @return: DeformationSl
	 */
	public DeformationSl getDeformationSlByName(String name,int areaId);

	/**
	 * @Title: addDeformationSl
	 * @Description: 新增方案信息
	 * @param: @param deformationSl
	 * @param: @return
	 * @return: boolean
	 */
	public boolean addDeformationSl(DeformationSl deformationSl);

	/**
	 * @Title: updateDeformationSl
	 * @Description: 更新方案信息
	 * @param: @param deformationSl
	 * @param: @return
	 * @return: boolean
	 */
	public boolean updateDeformationSl(DeformationSl deformationSl);

	/**
	 * @Title: getDeformationSiKey
	 * @Description: 获取巷道变形方案与测点的对应关系
	 * @param: @return
	 * @return: List<DeformationSiKey>
	 */
	public List<DeformationSiKey> getDeformationSiKey(int schemeId);

	/**
	 * @Title: addDeformationSis
	 * @Description: 新增方案详情表
	 * @param: @param deformationSis
	 * @param: @return
	 * @return: int
	 */
	public boolean addDeformationSiKey(DeformationSiKey deformationSiKey);

	/**
	 * @Title: deleteDeformationSiBySchemeId
	 * @Description: 删除si表中某方案的全部数据
	 * @param: @param schemeId
	 * @param: @return
	 * @return: boolean
	 */
	public boolean deleteDeformationSiBySchemeId(int schemeId);

	/**
	 * @Title: getDeformationMp
	 * @Description: 巷道变形根据方案id获取方案下的测点
	 * @param: @param schemeId
	 * @param: @return
	 * @return: List<DeformationMp>
	 */

	public List<DeformationMp> getDeformationMp(int schemeId);

	/**
	 * @Title: getDeformationMpByName
	 * @Description: 根据测点名称获取测点
	 * @param: @param name
	 * @param: @return
	 * @return: DeformationMp
	 */
	public DeformationMp getDeformationMpByName(String name,int areaId);

	/**
	 * @Title: getDeformationAllScheme
	 * @Description: 获取所有区域下的巷道变形方案及方案下的测点
	 * @param: @param areaId
	 * @param: @return
	 * @return: List<Map<String,Object>>
	 */
	public Map<String,Object> getDeformationAllScheme(int areaId,String stime ,String etime) throws ParseException;

	/**
	 * @Title: addOrUpdateMps
	 * @Description: 新增或更新deformationMp
	 * @param: @param deformationMps
	 * @return: void
	 */
	public void addOrUpdateMps(List<DeformationMp> deformationMps);


	/**
	 * @Title: getAllSchemeBySect
	 * @Description: 用于找测点的，时间段所涵盖的所有方案
	 * @param: @param stime
	 * @param: @param etime
	 * @param: @param areaId
	 * @param: @return
	 * @return: List<DeformationSl>
	 */
	List<Integer> getAllSchemeBySect(String stime,String etime,int areaId,int upTunnelId,int downTunnelId);

	/**
	 * @Title: getOldestMeasurePointsByAreaId
	 * @Description: 找到区域最早的方案测点
	 * @param: @param aid
	 * @param: @return
	 * @return: List<SchemeDeformationMp>
	 */
	List<DeformationMpinfo> getOldestMeasurePointsByAreaId(int aid,int upTunnelId,int downTunnelId);

	/**
	 * @Title: deleteScheme
	 * @Description: 删除方案
	 * @param: @param schemeId
	 * @param: @return
	 * @return: int
	 */
	int deleteScheme(int schemeId);

	/**
	 * @Title: getMpStartTime
	 * @Description: 查询测点的开始时间
	 * @param: @param areaId
	 * @param: @param mpId
	 * @param: @return
	 * @return: String
	 */
	public String getMpStartTime(int areaId,int mpId);
}
