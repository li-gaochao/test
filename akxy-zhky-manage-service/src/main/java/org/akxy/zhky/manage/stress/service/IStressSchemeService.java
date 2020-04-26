package org.akxy.zhky.manage.stress.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.akxy.zhky.manage.pojo.StressMp;
import org.akxy.zhky.manage.pojo.StressMpinfo;
import org.akxy.zhky.manage.pojo.StressSiKey;
import org.akxy.zhky.manage.pojo.StressSl;

/**
 * @ClassName: IStressSchemeServiceImpl.java
 * @Description:
 * @date: 2018年8月27日
 */
public interface IStressSchemeService {
	/**
	 * @Title: getStressSl
	 * @Description: 根据区域id,获取钻孔应力的方案列表
	 * @param: @param areaId
	 * @param: @return
	 * @return: List<StressSl>
	 */
	public List<StressSl> getStressSl(int areaId);

	/**
	 * @Title: getStressSlByName
	 * @Description: 根据方案名称获得sl方案
	 * @param: @param name
	 * @param: @return
	 * @return: StressSl
	 */
	public StressSl getStressSlByName(String name,int areaId);

	/**
	 * @Title: addStressSl
	 * @Description: 新增方案信息
	 * @param: @param stressSl
	 * @param: @return
	 * @return: boolean
	 */
	public boolean addStressSl(StressSl stressSl);

	/**
	 * @Title: updateStressSl
	 * @Description: 更新方案信息
	 * @param: @param stressSl
	 * @param: @return
	 * @return: boolean
	 */
	public boolean updateStressSl(StressSl stressSl);

	/**
	 * @Title: getStressSiKey
	 * @Description: 获取钻孔应力方案与测点的对应关系
	 * @param: @return
	 * @return: List<StressSiKey>
	 */
	public List<StressSiKey> getStressSiKey(int schemeId);

	/**
	 * @Title: addStressSis
	 * @Description: 新增方案详情表
	 * @param: @param stressSis
	 * @param: @return
	 * @return: int
	 */
	public boolean addStressSiKey(StressSiKey stressSiKey);

	/**
	 * @Title: deleteStressSiBySchemeId
	 * @Description: 删除si表中某方案的全部数据
	 * @param: @param schemeId
	 * @param: @return
	 * @return: boolean
	 */
	public boolean deleteStressSiBySchemeId(int schemeId);

	/**
	 * @Title: getStressMp
	 * @Description: 钻孔应力根据方案id获取方案下的测点
	 * @param: @param schemeId
	 * @param: @return
	 * @return: List<StressMp>
	 */

	public List<StressMp> getStressMp(int schemeId);

	/**
	 * @Title: getStressMpByName
	 * @Description: 根据测点名称获取测点
	 * @param: @param name
	 * @param: @return
	 * @return: StressMp
	 */
	public StressMp getStressMpByName(String name,int areaId);

	/**
	 * @Title: getStressAllScheme
	 * @Description: 获取所有区域下的钻孔应力方案及方案下的测点
	 * @param: @param areaId
	 * @param: @return
	 * @return: List<Map<String,Object>>
	 */
	public Map<String,Object> getStressAllScheme(int areaId,String stime ,String etime) throws ParseException;

	/**
	 * @Title: addOrUpdateMps
	 * @Description: 新增或更新stressMp
	 * @param: @param stressMps
	 * @return: void
	 */
	public void addOrUpdateMps(List<StressMp> stressMps);


	/**
	 * @Title: getAllSchemeBySect
	 * @Description: 用于找测点的，时间段所涵盖的所有方案
	 * @param: @param stime
	 * @param: @param etime
	 * @param: @param areaId
	 * @param: @return
	 * @return: List<StressSl>
	 */
	public List<Integer> getAllSchemeBySect(String stime,String etime,int areaId,int upTunnelId,int downTunnelId);

	/**
	 * @Title: getOldestMeasurePointsByAreaId
	 * @Description: 找到区域最早的方案测点
	 * @param: @param aid
	 * @param: @return
	 * @return: List<SchemeStressMp>
	 */
	public List<StressMpinfo> getOldestMeasurePointsByAreaId(int aid,int upTunnelId,int downTunnelId);

	/**
	 * @Title: deleteScheme
	 * @Description: 删除方案
	 * @param: @param schemeId
	 * @param: @return
	 * @return: int
	 */
	public int deleteScheme(int schemeId);

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
