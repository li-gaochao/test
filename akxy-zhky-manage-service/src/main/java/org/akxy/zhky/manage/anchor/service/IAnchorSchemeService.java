package org.akxy.zhky.manage.anchor.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.akxy.zhky.manage.pojo.AnchorMp;
import org.akxy.zhky.manage.pojo.AnchorMpinfo;
import org.akxy.zhky.manage.pojo.AnchorSiKey;
import org.akxy.zhky.manage.pojo.AnchorSl;

/**
 * @ClassName: IAnchorSchemeServiceImpl.java
 * @Description:
 * @date: 2018年8月27日
 */
public interface IAnchorSchemeService {
	/**
	 * @Title: getAnchorSl
	 * @Description: 根据区域id,获取钻孔锚杆索的方案列表
	 * @param: @param areaId
	 * @param: @return
	 * @return: List<AnchorSl>
	 */
	public List<AnchorSl> getAnchorSl(int areaId);

	/**
	 * @Title: getAnchorSlByName
	 * @Description: 根据方案名称获得sl方案
	 * @param: @param name
	 * @param: @return
	 * @return: AnchorSl
	 */
	public AnchorSl getAnchorSlByName(String name,int areaId);

	/**
	 * @Title: addAnchorSl
	 * @Description: 新增方案信息
	 * @param: @param anchorSl
	 * @param: @return
	 * @return: boolean
	 */
	public boolean addAnchorSl(AnchorSl anchorSl);

	/**
	 * @Title: updateAnchorSl
	 * @Description: 更新方案信息
	 * @param: @param anchorSl
	 * @param: @return
	 * @return: boolean
	 */
	public boolean updateAnchorSl(AnchorSl anchorSl);

	/**
	 * @Title: getAnchorSiKey
	 * @Description: 获取钻孔锚杆索方案与测点的对应关系
	 * @param: @return
	 * @return: List<AnchorSiKey>
	 */
	public List<AnchorSiKey> getAnchorSiKey(int schemeId);

	/**
	 * @Title: addAnchorSis
	 * @Description: 新增方案详情表
	 * @param: @param anchorSis
	 * @param: @return
	 * @return: int
	 */
	public boolean addAnchorSiKey(AnchorSiKey anchorSiKey);

	/**
	 * @Title: deleteAnchorSiBySchemeId
	 * @Description: 删除si表中某方案的全部数据
	 * @param: @param schemeId
	 * @param: @return
	 * @return: boolean
	 */
	public boolean deleteAnchorSiBySchemeId(int schemeId);

	/**
	 * @Title: getAnchorMp
	 * @Description: 钻孔锚杆索根据方案id获取方案下的测点
	 * @param: @param schemeId
	 * @param: @return
	 * @return: List<AnchorMp>
	 */

	public List<AnchorMp> getAnchorMp(int schemeId);

	/**
	 * @Title: getAnchorMpByName
	 * @Description: 根据测点名称获取测点
	 * @param: @param name
	 * @param: @return
	 * @return: AnchorMp
	 */
	public AnchorMp getAnchorMpByName(String name,int areaId);

	/**
	 * @Title: getAnchorAllScheme
	 * @Description: 获取所有区域下的钻孔锚杆索方案及方案下的测点
	 * @param: @param areaId
	 * @param: @return
	 * @return: List<Map<String,Object>>
	 */
	public Map<String,Object> getAnchorAllScheme(int areaId,String stime ,String etime) throws ParseException;

	/**
	 * @Title: addOrUpdateMps
	 * @Description: 新增或更新anchorMp
	 * @param: @param anchorMps
	 * @return: void
	 */
	public void addOrUpdateMps(List<AnchorMp> anchorMps);


	/**
	 * @Title: getAllSchemeBySect
	 * @Description: 用于找测点的，时间段所涵盖的所有方案
	 * @param: @param stime
	 * @param: @param etime
	 * @param: @param areaId
	 * @param: @return
	 * @return: List<AnchorSl>
	 */
	List<Integer> getAllSchemeBySect(String stime,String etime,int areaId, int upTunnelId,int downTunnelId);

	/**
	 * @Title: getOldestMeasurePointsByAreaId
	 * @Description: 找到区域最早的方案测点
	 * @param: @param aid
	 * @param: @return
	 * @return: List<SchemeAnchorMp>
	 */
	List<AnchorMpinfo> getOldestMeasurePointsByAreaId(int aid,int upTunnelId,int downTunnelId);

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
