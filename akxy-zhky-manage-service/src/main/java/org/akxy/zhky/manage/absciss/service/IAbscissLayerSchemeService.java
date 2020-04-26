package org.akxy.zhky.manage.absciss.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.akxy.zhky.manage.pojo.AbscissLayerMp;
import org.akxy.zhky.manage.pojo.AbscissLayerMpinfo;
import org.akxy.zhky.manage.pojo.AbscissLayerSiKey;
import org.akxy.zhky.manage.pojo.AbscissLayerSl;

/**
 * @Description:
 * @date: 2018年8月27日
 */
public interface IAbscissLayerSchemeService {
	/**
	 * @Title: getAbscissLayerAllScheme
	 * @Description: 根据区域ID获取该区域的所有方案
	 * @param: @param areaId
	 * @param: @return
	 * @return: List<List<Map<String,Object>>>
	 */
	public Map<String,Object> getAbscissLayerAllScheme(int areaId,String stime ,String etime) throws ParseException;
	/**
	 * @Title: getAbscissLayerSl
	 * @Description: 根据区域id,获取钻孔应力的方案列表
	 * @param: @param areaId
	 * @param: @return
	 * @return: List<AbscissLayerSl>
	 */
	public List<AbscissLayerSl> getAbscissLayerSl(int areaId);

	/**
	 * @Title: getAbscissLayerSlByName
	 * @Description: 根据方案名称获得sl方案
	 * @param: @param name
	 * @param: @return
	 * @return: AbscissLayerSl
	 */
	public AbscissLayerSl getAbscissLayerSlByName(String name,int areaId);

	/**
	 * @Title: addAbscissLayerSl
	 * @Description: 新增方案信息
	 * @param: @param abscissLayerSl
	 * @param: @return
	 * @return: boolean
	 */
	public boolean addAbscissLayerSl(AbscissLayerSl abscissLayerSl);

	/**
	 * @Title: updateAbscissLayerSl
	 * @Description: 更新方案信息
	 * @param: @param abscissLayerSl
	 * @param: @return
	 * @return: boolean
	 */
	public boolean updateAbscissLayerSl(AbscissLayerSl abscissLayerSl);

	/**
	 * @Title: getAbscissLayerSiKey
	 * @Description: 获取钻孔应力方案与测点的对应关系
	 * @param: @return
	 * @return: List<AbscissLayerSiKey>
	 */
	public List<AbscissLayerSiKey> getAbscissLayerSiKey(int schemeId);

	/**
	 * @Title: addAbscissLayerSis
	 * @Description: 新增方案详情表
	 * @param: @param abscissLayerSis
	 * @param: @return
	 * @return: int
	 */
	public boolean addAbscissLayerSiKey(AbscissLayerSiKey abscissLayerSiKey);

	/**
	 * @Title: deleteAbscissLayerSiBySchemeId
	 * @Description: 删除si表中某方案的全部数据
	 * @param: @param schemeId
	 * @param: @return
	 * @return: boolean
	 */
	public boolean deleteAbscissLayerSiBySchemeId(int schemeId);

	/**
	 * @Title: getAbscissLayerMp
	 * @Description: 钻孔应力根据方案id获取方案下的测点
	 * @param: @param schemeId
	 * @param: @return
	 * @return: List<AbscissLayerMp>
	 */

	public List<AbscissLayerMp> getAbscissLayerMp(int schemeId);

	/**
	 * @Title: getAbscissLayerMpByName
	 * @Description: 根据测点名称获取测点
	 * @param: @param name
	 * @param: @return
	 * @return: AbscissLayerMp
	 */
	public AbscissLayerMp getAbscissLayerMpByName(String name,int areaId);
	/**
	 * @Title: addOrUpdateMps
	 * @Description: 新增或更新abscissLayerMp
	 * @param: @param abscissLayerMps
	 * @return: void
	 */
	public void addOrUpdateMps(List<AbscissLayerMp> abscissLayerMps);

	/**
	 * @Title: getAllSchemeBySect
	 * @Description: 用于找测点的，时间段所涵盖的所有方案
	 * @param: @param stime
	 * @param: @param etime
	 * @param: @param areaId
	 * @param: @return
	 * @return: List<AbscissLayerSl>
	 */
	List<Integer> getAllSchemeBySect(String stime,String etime,int areaId,int upTunnelId,int downTunnelId);

	/**
	 * @Title: getOldestMeasurePointsByAreaId
	 * @Description: 找到区域最早的方案测点
	 * @param: @param aid
	 * @param: @return
	 * @return: List<AbscissLayerMpinfo>
	 */
	List<AbscissLayerMpinfo> getOldestMeasurePointsByAreaId(int aid,int upTunnelId,int downTunnelId);

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
