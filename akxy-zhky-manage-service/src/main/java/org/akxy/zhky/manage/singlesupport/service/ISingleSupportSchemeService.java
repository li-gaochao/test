package org.akxy.zhky.manage.singlesupport.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.akxy.zhky.manage.pojo.SingleSupportMp;
import org.akxy.zhky.manage.pojo.SingleSupportMpinfo;
import org.akxy.zhky.manage.pojo.SingleSupportSiKey;
import org.akxy.zhky.manage.pojo.SingleSupportSl;

/**
 * @ClassName: ISingleSupportSchemeServiceImpl.java
 * @Description:
 * @date: 2018年8月27日
 */
public interface ISingleSupportSchemeService {
	/**
	 * @Title: getSingleSupportSl
	 * @Description: 根据区域id,获取钻孔锚杆索的方案列表
	 * @param: @param areaId
	 * @param: @return
	 * @return: List<SingleSupportSl>
	 */
	public List<SingleSupportSl> getSingleSupportSl(int areaId);

	/**
	 * @Title: getSingleSupportSlByName
	 * @Description: 根据方案名称获得sl方案
	 * @param: @param name
	 * @param: @return
	 * @return: SingleSupportSl
	 */
	public SingleSupportSl getSingleSupportSlByName(String name,int areaId);

	/**
	 * @Title: addSingleSupportSl
	 * @Description: 新增方案信息
	 * @param: @param singleSupportSl
	 * @param: @return
	 * @return: boolean
	 */
	public boolean addSingleSupportSl(SingleSupportSl singleSupportSl);

	/**
	 * @Title: updateSingleSupportSl
	 * @Description: 更新方案信息
	 * @param: @param singleSupportSl
	 * @param: @return
	 * @return: boolean
	 */
	public boolean updateSingleSupportSl(SingleSupportSl singleSupportSl);

	/**
	 * @Title: getSingleSupportSiKey
	 * @Description: 获取钻孔锚杆索方案与测点的对应关系
	 * @param: @return
	 * @return: List<SingleSupportSiKey>
	 */
	public List<SingleSupportSiKey> getSingleSupportSiKey(int schemeId);

	/**
	 * @Title: addSingleSupportSis
	 * @Description: 新增方案详情表
	 * @param: @param singleSupportSis
	 * @param: @return
	 * @return: int
	 */
	public boolean addSingleSupportSiKey(SingleSupportSiKey singleSupportSiKey);

	/**
	 * @Title: deleteSingleSupportSiBySchemeId
	 * @Description: 删除si表中某方案的全部数据
	 * @param: @param schemeId
	 * @param: @return
	 * @return: boolean
	 */
	public boolean deleteSingleSupportSiBySchemeId(int schemeId);

	/**
	 * @Title: getSingleSupportMp
	 * @Description: 钻孔锚杆索根据方案id获取方案下的测点
	 * @param: @param schemeId
	 * @param: @return
	 * @return: List<SingleSupportMp>
	 */

	public List<SingleSupportMp> getSingleSupportMp(int schemeId);

	/**
	 * @Title: getSingleSupportMpByName
	 * @Description: 根据测点名称获取测点
	 * @param: @param name
	 * @param: @return
	 * @return: SingleSupportMp
	 */
	public SingleSupportMp getSingleSupportMpByName(String name,int areaId);

	/**
	 * @Title: getSingleSupportAllScheme
	 * @Description: 获取所有区域下的钻孔锚杆索方案及方案下的测点
	 * @param: @param areaId
	 * @param: @return
	 * @return: List<Map<String,Object>>
	 */
	public Map<String,Object> getSingleSupportAllScheme(int areaId,String stime ,String etime) throws ParseException;

	/**
	 * @Title: addOrUpdateMps
	 * @Description: 新增或更新singleSupportMp
	 * @param: @param singleSupportMps
	 * @return: void
	 */
	public void addOrUpdateMps(List<SingleSupportMp> singleSupportMps);


	/**
	 * @Title: getAllSchemeBySect
	 * @Description: 用于找测点的，时间段所涵盖的所有方案
	 * @param: @param stime
	 * @param: @param etime
	 * @param: @param areaId
	 * @param: @return
	 * @return: List<SingleSupportSl>
	 */
	List<Integer> getAllSchemeBySect(String stime,String etime,int areaId,int upTunnelId,int downTunnelId);

	/**
	 * @Title: getOldestMeasurePointsByAreaId
	 * @Description: 找到区域最早的方案测点
	 * @param: @param aid
	 * @param: @return
	 * @return: List<SchemeSingleSupportMp>
	 */
	List<SingleSupportMpinfo> getOldestMeasurePointsByAreaId(int aid,int upTunnelId,int downTunnelId);

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
