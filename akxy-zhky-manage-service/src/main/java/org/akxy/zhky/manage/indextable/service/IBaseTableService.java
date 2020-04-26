package org.akxy.zhky.manage.indextable.service;

import java.util.List;
import java.util.Map;

import org.akxy.zhky.manage.pojo.IndexTable;

/**
 * @ClassName: IBaseTableService.java
 * @Description: 各个工作面的首页数据表
 * @date: 2018年6月11日
 */
public interface IBaseTableService {
	public List<IndexTable> getAllIndexTable();
	public List<IndexTable> getIndexTableByAreaIdAndType(int areaId, int type);
	public List<IndexTable> getIndexTable(int areaId);
	public Map<String,Object> getBorderByAreaAndTypeAndTunnel(int areaId, int type, int tunnelId);
	/**
	 * @Title: getIndexMpInfo
	 * @Description: 获取首页弹窗信息
	 * @param: @param areaId
	 * @param: @return
	 * @return: Map<String,Object>
	 */
	public Map<String,Object> getIndexMpInfo(int areaId,int type);

}
