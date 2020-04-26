package org.akxy.zhky.manage.tunnel.service;

import java.util.List;
import java.util.Map;

import org.akxy.zhky.manage.pojo.PosTunnel;
import org.akxy.zhky.manage.pojo.Tunnel;

/**
 * @ClassName: ITunnelService.java
 * @Description: 巷道查询
 * @date: 2018年9月28日
 */
public interface ITunnelService {
	public Tunnel findTunnelById(int tunnelId);

	public List<Tunnel> findTunnelByAreaId(int areaId);

	public List<Tunnel> findAllTunnel();

	public void deleteTunnelByAreaId(int areaId);

	public void insertTunnel(Tunnel tunnel);

	/**
	 * @Title: getAllTunnelByArea
	 * @Description: 根据区域id获取所有带坐标集合的巷道
	 * @param: @param areaId
	 * @param: @return
	 * @return: List<PosTunnel>
	 */
	public List<PosTunnel> getPosTunnelsByArea(int areaId);

	/**
	 * @Title: findUpAndDownTunnelByAreaId
	 * @Description: 获取工作面的上下巷巷道
	 * @param: @param areaId
	 * @param: @return
	 * @return: List<Tunnel>
	 */
	public List<Tunnel> findUpAndDownTunnelByAreaId(int areaId);

	/**
	 * @Title: findTunnelByAreaIdAndTunnelName
	 * @Description: 根据工作面ID和巷道名称，获取巷道
	 * @param: @param areaId
	 * @param: @param tunnelName
	 * @param: @return
	 * @return: Tunnel
	 */
	public Tunnel findTunnelByAreaIdAndTunnelName(int areaId,String tunnelName);

	/**
	 * @Title: findTunnelStressDepth
	 * @Description: 获取巷道内应力测点的深浅基点深度
	 * @param: @param tunnelId
	 * @param: @return
	 * @return: Map<String,Float>
	 */
	public Map<String,Float> findTunnelStressDepth(int tunnelId);

	/**
	 * @Title: isTunnelSerial
	 * @Description: 判断所有的工作面是否存在上下巷
	 * @param: @return
	 * @return: Boolean
	 */
	public Boolean isAreaUpAndDown(int areaId);

	/**
	 * @Title: isTunnelSerial
	 * @Description: 判断所有的工作面的上下巷是否连续
	 * @param: @return
	 * @return: Boolean
	 */
	public Boolean isTunnelSerial(int areaId);

	/**
	 * @Title: getTunneRange
	 * @Description: 获取上下巷的各自范围、整体范围
	 * @param: @param upTunnelId
	 * @param: @param downTunnelId
	 * @param: @return
	 * @return: Map<String,Float>
	 */
	public Map<String,Object> getTunneRange(int upTunnelId,int downTunnelId);
}
