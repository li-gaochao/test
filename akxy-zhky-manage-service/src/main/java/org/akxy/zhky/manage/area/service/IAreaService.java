package org.akxy.zhky.manage.area.service;

import java.util.List;

import org.akxy.zhky.manage.pojo.Area;

/**
 * @ClassName: IAreaService.java
 * @Description: 区域(工作面)
 * @date: 2018年6月12日
 */
public interface IAreaService {
	/**
	 * @Title: getAllArea
	 * @Description: 获取所有工作面和各个工作面下的预警测点个数
	 * @param: @return
	 * @return: List<Area>
	 * @throws
	 */
	public List<Object> getAllArea();

	/**
	 * @Title: getSubSystemArea
	 * @Description: 获取子系统的区域
	 * @param: @param subSystemType
	 * @param: @return
	 * @return: List<Area>
	 * @throws
	 */
	public List<Area> getSubSystemArea(int subSystemType);

	/**
	 * @Title: getAreaById
	 * @Description: 通过区域id查询区域
	 * @param: @param areaId
	 * @param: @return
	 * @return: Area
	 * @throws
	 */
	public Area getAreaById(int areaId);

	/**
	 * @Title: getInitAllArea
	 * @Description: 区域管理中的获取所有区域
	 * @param: @return
	 * @return: List<Area>
	 */
	public List<Area> getInitAllArea();

	/**
	 * @Title: getAreaByName
	 * @Description: 根据测点名称获取测点
	 * @param: @param areaName
	 * @param: @return
	 * @return: Area
	 */
	public Area getAreaByName(String areaName);

	/**
	 * @Title: insertArea
	 * @Description: 新增区域
	 * @param: @param area
	 * @return: void
	 */
	public void insertArea(Area area);

	/**
	 * @Title: updateArea
	 * @Description: 更新区域
	 * @param: @param area
	 * @return: void
	 */
	public void updateArea(Area area);

	/**
	 * @Title: updateAreaSelective
	 * @Description: 更新area中不为空的字段
	 * @param: @param area
	 * @return: void
	 */
	public void updateAreaSelective(Area area);

	/**
	 * @Title: deleteAreaByIdAreaId
	 * @Description: 根据区域id，删除区域
	 * @param: @param areaId
	 * @return: void
	 */
	public int deleteAreaByIdAresaId(int areaId);

	/**
	 * @Title: getAllUsedArea
	 * @Description: 获取所有在使用中的工作面
	 * @param: @return
	 * @return: List<Area>
	 */
	public List<Area> getAllUsedArea();
}
