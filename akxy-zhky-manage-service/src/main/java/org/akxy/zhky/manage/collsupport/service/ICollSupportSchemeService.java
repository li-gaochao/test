package org.akxy.zhky.manage.collsupport.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.akxy.zhky.manage.pojo.CollSupportMp;
import org.akxy.zhky.manage.pojo.CollSupportMpinfo;
import org.akxy.zhky.manage.pojo.CollSupportSiKey;
import org.akxy.zhky.manage.pojo.CollSupportSl;

/**
 * @ClassName: ICollSupportSchemeServiceImpl.java
 * @Description:
 * @date: 2018年8月27日
 */
public interface ICollSupportSchemeService {
	/**
	 * @Title: getCollSupportSl
	 * @Description: 根据区域id,获取钻孔锚杆索的方案列表
	 * @param: @param areaId
	 * @param: @return
	 * @return: List<CollSupportSl>
	 */
	public List<CollSupportSl> getCollSupportSl(int areaId);

	/**
	 * @Title: getCollSupportSlByName
	 * @Description: 根据方案名称获得sl方案
	 * @param: @param name
	 * @param: @return
	 * @return: CollSupportSl
	 */
	public CollSupportSl getCollSupportSlByName(String name,int areaId);

	/**
	 * @Title: addCollSupportSl
	 * @Description: 新增方案信息
	 * @param: @param collSupportSl
	 * @param: @return
	 * @return: boolean
	 */
	public boolean addCollSupportSl(CollSupportSl collSupportSl);

	/**
	 * @Title: updateCollSupportSl
	 * @Description: 更新方案信息
	 * @param: @param collSupportSl
	 * @param: @return
	 * @return: boolean
	 */
	public boolean updateCollSupportSl(CollSupportSl collSupportSl);

	/**
	 * @Title: getCollSupportSiKey
	 * @Description: 获取钻孔锚杆索方案与测点的对应关系
	 * @param: @return
	 * @return: List<CollSupportSiKey>
	 */
	public List<CollSupportSiKey> getCollSupportSiKey(int schemeId);

	/**
	 * @Title: addCollSupportSis
	 * @Description: 新增方案详情表
	 * @param: @param collSupportSis
	 * @param: @return
	 * @return: int
	 */
	public boolean addCollSupportSiKey(CollSupportSiKey collSupportSiKey);

	/**
	 * @Title: deleteCollSupportSiBySchemeId
	 * @Description: 删除si表中某方案的全部数据
	 * @param: @param schemeId
	 * @param: @return
	 * @return: boolean
	 */
	public boolean deleteCollSupportSiBySchemeId(int schemeId);

	/**
	 * @Title: getCollSupportMp
	 * @Description: 钻孔锚杆索根据方案id获取方案下的测点
	 * @param: @param schemeId
	 * @param: @return
	 * @return: List<CollSupportMp>
	 */

	public List<CollSupportMp> getCollSupportMp(int schemeId);

	/**
	 * @Title: getCollSupportMpByName
	 * @Description: 根据测点名称获取测点
	 * @param: @param name
	 * @param: @return
	 * @return: CollSupportMp
	 */
	public CollSupportMp getCollSupportMpByName(String name,int areaId);

	/**
	 * @Title: getCollSupportAllScheme
	 * @Description: 获取所有区域下的钻孔锚杆索方案及方案下的测点
	 * @param: @param areaId
	 * @param: @return
	 * @return: List<Map<String,Object>>
	 */
	public Map<String,Object> getCollSupportAllScheme(int areaId,String stime ,String etime) throws ParseException;

	/**
	 * @Title: addOrUpdateMps
	 * @Description: 新增或更新collSupportMp
	 * @param: @param collSupportMps
	 * @return: void
	 */
	public void addOrUpdateMps(List<CollSupportMp> collSupportMps);


	/**
	 * @Title: getAllSchemeBySect
	 * @Description: 用于找测点的，时间段所涵盖的所有方案
	 * @param: @param stime
	 * @param: @param etime
	 * @param: @param areaId
	 * @param: @return
	 * @return: List<CollSupportSl>
	 */
	List<Integer> getAllSchemeBySect(String stime,String etime,int areaId,int upTunnelId,int downTunnelId);

	/**
	 * @Title: getOldestMeasurePointsByAreaId
	 * @Description: 找到区域最早的方案测点
	 * @param: @param aid
	 * @param: @return
	 * @return: List<SchemeCollSupportMp>
	 */
	List<CollSupportMpinfo> getOldestMeasurePointsByAreaId(int aid,int upTunnelId,int downTunnelId);

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
