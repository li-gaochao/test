package org.akxy.zhky.manage.tunnel.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.akxy.zhky.manage.dao.mapper.TunnelMapper;
import org.akxy.zhky.manage.pojo.Line;
import org.akxy.zhky.manage.pojo.PosTunnel;
import org.akxy.zhky.manage.pojo.Tunnel;
import org.akxy.zhky.manage.pojo.TunnelExample;
import org.akxy.zhky.manage.pojo.TunnelExample.Criteria;
import org.akxy.zhky.manage.pojo.Vec3dPoint;
import org.akxy.zhky.manage.tunnel.service.ITunnelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: TunnelServiceImpl.java
 * @Description:
 * @date: 2018年9月28日
 */
@Service
public class TunnelServiceImpl implements ITunnelService{
	@Autowired
	private TunnelMapper tunnelMapper;

	@Override
	public Tunnel findTunnelById(int id) {
		return tunnelMapper.selectByPrimaryKey(id);
	}

	@Override
	public List<Tunnel> findTunnelByAreaId(int areaId) {
		TunnelExample example = new TunnelExample();
		Criteria criteria = example.createCriteria();
		criteria.andAreaIdEqualTo(areaId);
		example.setOrderByClause("layer");
		return tunnelMapper.selectByExample(example);
	}

	@Override
	public List<Tunnel> findAllTunnel() {
		return tunnelMapper.selectAllTunnel();
	}

	@Override
	public void deleteTunnelByAreaId(int areaId) {
		TunnelExample example = new TunnelExample();
		Criteria criteria = example.createCriteria();
		criteria.andAreaIdEqualTo(areaId);
		tunnelMapper.deleteByExample(example);
	}

	@Override
	public void insertTunnel(Tunnel tunnel) {
		tunnelMapper.insert(tunnel);
	}

	@Override
	public List<PosTunnel> getPosTunnelsByArea(int areaId) {
		List<Tunnel> tunnels = this.findTunnelByAreaId(areaId);
		List<PosTunnel> posTunnels = new ArrayList<PosTunnel>();
		if (tunnels==null || tunnels.isEmpty()){
			return null;
		}
		else if (tunnels.size() == 0) {
			return null;
		} else {
			for (int i = 0; i < tunnels.size(); i++) {
				String[] tmps = tunnels.get(i).getPos().split(";");
				List<Vec3dPoint> list = new ArrayList<Vec3dPoint>();
				List<Line> pline = new ArrayList<Line>();
				for (int j = 0; j < tmps.length; j++) {
					if (!tmps[j].isEmpty()) {
						String[] tmps1 = tmps[j].split(",");
						double x = Double.valueOf(tmps1[0]);
						double y = Double.valueOf(tmps1[1]);
						double z = Double.valueOf(tmps1[2]);
						Vec3dPoint vect = new Vec3dPoint(x, y, z);
						list.add(vect);
					}
				}
				for(int n = 0 ; n < list.size() ; n++){
					if(n+1 < list.size()){
						Line line = new Line(list.get(n),list.get(n+1));
						pline.add(line);
					}
				}
				posTunnels.add(new PosTunnel(tunnels.get(i).getId(), list,pline));
			}
		}
		return posTunnels;
	}

	@Override
	public List<Tunnel> findUpAndDownTunnelByAreaId(int areaId) {
		List<Integer> num = new ArrayList<Integer>();
		num.add(1);
		num.add(2);
		TunnelExample example = new TunnelExample();
		Criteria criteria = example.createCriteria();
		criteria.andAreaIdEqualTo(areaId);
		criteria.andUpAndDownIn(num);
		return tunnelMapper.selectByExample(example);
	}

	@Override
	public Tunnel findTunnelByAreaIdAndTunnelName(int areaId, String tunnelName) {
		TunnelExample example = new TunnelExample();
		Criteria criteria = example.createCriteria();
		criteria.andAreaIdEqualTo(areaId);
		criteria.andNameEqualTo(tunnelName);
		List<Tunnel> tunnels = tunnelMapper.selectByExample(example);
		if(tunnels!=null && tunnels.size() > 0 ){
			return tunnels.get(0);
		}else{
			return null;
		}

	}


	@Override
	public Map<String,Float> findTunnelStressDepth(int tunnelId) {
		return tunnelMapper.selectTunnelStressDepth(tunnelId);
	}

	@Override
	public Boolean isAreaUpAndDown(int areaId) {
		int count = tunnelMapper.isAreaUpAndDown(areaId);
		if(count==2){
			return true;
		}
		return false;
	}

	@Override
	public Boolean isTunnelSerial(int areaId) {
		int layer = tunnelMapper.isTunnelSerial(areaId);
		if(layer==1){
			return true;
		}
		return false;
	}


	@Override
	public Map<String, Object> getTunneRange(int upTunnelId, int downTunnelId) {
		return tunnelMapper.getTunnelRange(upTunnelId, downTunnelId);
	}


}
