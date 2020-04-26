package org.akxy.zhky.manage.area.service.impl;

import java.util.List;

import org.akxy.zhky.manage.area.service.IWorkFaceProgressService;
import org.akxy.zhky.manage.area.service.util.GetPoint;
import org.akxy.zhky.manage.footage.service.IFootageService;
import org.akxy.zhky.manage.pojo.Line;
import org.akxy.zhky.manage.pojo.PosTunnel;
import org.akxy.zhky.manage.pojo.Vec3dPoint;
import org.akxy.zhky.manage.tunnel.service.ITunnelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description: 工作面位置计算
 * @date: 2019年3月22日
 */
@Service
public class WorkFaceProgressServiceImpl implements IWorkFaceProgressService{
	@Autowired
	private ITunnelService iTunnelService;
	@Autowired
	private IFootageService iFootageService;
	@Override
	public Line getWorkFaceLine(int areaId,boolean isSupport){
		Line line = new Line();
		Vec3dPoint upPoint = null;
		Vec3dPoint downPoint = null;
		List<PosTunnel> posTunnels = iTunnelService.getPosTunnelsByArea(areaId);
		if(posTunnels!=null&&posTunnels.size()>1){

			/*对各巷道，求当前推进点*/
			PosTunnel upTunnel = posTunnels.get(0);
			PosTunnel downTunnel = posTunnels.get(1);

			Double upAllValue = iFootageService.getALLfootageByTunnelId(upTunnel.getId())!=null?iFootageService.getALLfootageByTunnelId(upTunnel.getId()):0;
			Double downAllValue = iFootageService.getALLfootageByTunnelId(downTunnel.getId())!=null?iFootageService.getALLfootageByTunnelId(downTunnel.getId()):0;

			if(isSupport){
				if(upAllValue!=null&&upAllValue.compareTo(new Double(1))>=0){
					upAllValue = upAllValue - 5;
				}
				if(downAllValue!=null&&downAllValue.compareTo(new Double(1))>=0){
					downAllValue = downAllValue - 5;
				}
			}

			Double tempUpDistance = 0.0, tempDownDistance = 0.0;

			for(int i = 0 ; i < upTunnel.getPline().size() ; i++){
				tempUpDistance += upTunnel.getPline().get(i).getDistance();
				if(upAllValue <= tempUpDistance){
					upPoint = GetPoint.getTunnelAreaPoint(
							upTunnel.getPline().get(i).getStartVec3dPoint(),
							upTunnel.getPline().get(i).getEndVec3dPoint(),
							upAllValue);
				}
			}

			for(int i = 0 ; i < downTunnel.getPline().size() ; i++){
				tempDownDistance += downTunnel.getPline().get(i).getDistance();
				if(downAllValue <= tempDownDistance){
					downPoint = GetPoint.getTunnelAreaPoint(
							downTunnel.getPline().get(i).getStartVec3dPoint(),
							downTunnel.getPline().get(i).getEndVec3dPoint(),
							downAllValue);
				}
			}
		}

		line.setStartVec3dPoint(upPoint);
		line.setEndVec3dPoint(downPoint);
		return line;
	}
}