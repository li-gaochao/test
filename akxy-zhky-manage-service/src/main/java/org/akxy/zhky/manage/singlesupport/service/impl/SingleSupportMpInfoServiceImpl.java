package org.akxy.zhky.manage.singlesupport.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.akxy.zhky.manage.area.service.IWorkFaceProgressService;
import org.akxy.zhky.manage.area.service.util.DataOpUtil;
import org.akxy.zhky.manage.dao.mapper.IndexTableMapper;
import org.akxy.zhky.manage.dao.mapper.SingleSupportMpMapper;
import org.akxy.zhky.manage.dao.mapper.SingleSupportMpinfoMapper;
import org.akxy.zhky.manage.pojo.IndexTable;
import org.akxy.zhky.manage.pojo.Line;
import org.akxy.zhky.manage.pojo.SingleSupportMp;
import org.akxy.zhky.manage.pojo.SingleSupportMpinfo;
import org.akxy.zhky.manage.pojo.Vec3dPoint;
import org.akxy.zhky.manage.singlesupport.service.ISingleSupportMpInfoService;
import org.akxy.zhky.manage.singlesupport.service.ISingleSupportSchemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: SingleSupportMpInfoServiceImpl.java
 * @Description:
 * @date: 2018年7月3日
 */
@Service
public class SingleSupportMpInfoServiceImpl implements ISingleSupportMpInfoService{
	@Autowired
	private IndexTableMapper indexMapperTable;
	@Autowired
	private SingleSupportMpMapper singleSupportMpMapper;
	@Autowired
	private IWorkFaceProgressService iWorkFaceProgressService;
	@Autowired
	private SingleSupportMpinfoMapper singleSupportMpinfoMapper;
	@Autowired
	private ISingleSupportSchemeService iSingleSupportSchemeService;

	@Override
	public List<IndexTable> getSingleSupportNowSchemeMp(int areaId) {

		List<IndexTable> list = indexMapperTable.getSingleSupportNowSchemecMpList(areaId);
		Line line = null;
		Vec3dPoint start =null, end = null, temp = null;
		//重新计算坐标
		for(int i = 0 ; i < list.size() ; i++){
			//计算当前的工作面line
			line = iWorkFaceProgressService.getWorkFaceLine(list.get(i).getAreaId(),true);
			start = line.getStartVec3dPoint();
			end = line.getEndVec3dPoint();
			temp = DataOpUtil.getXY(start, end, list.get(i).getDistance());
			list.get(i).setX(temp.getX());
			list.get(i).setY(temp.getY());
		}
		return list;
	}

	@Override
	public List<IndexTable> getSingleSupportAllMp(int areaId) {
		return indexMapperTable.getSingleSupportAllMpList(areaId);
	}

	@Override
	public SingleSupportMp getSingleSupportMpById(Integer mpId) {
		return singleSupportMpMapper.selectByPrimaryKey(mpId);
	}
	@Override
	public SingleSupportMp getSingleSupportMpByName(String mpName,int areaId) {

		return singleSupportMpMapper.selectMpByName(mpName,areaId);
	}

	@Override
	public int[] getSingleSupportMpBySect(String stime, String etime, int areaId,int upTunnelId,int downTunnelId) {
		List<Integer> slist = new ArrayList<Integer>();

		slist = iSingleSupportSchemeService.getAllSchemeBySect(stime, etime,areaId,upTunnelId,downTunnelId);

		List<SingleSupportMpinfo> tlist = new ArrayList<SingleSupportMpinfo>();
		if(slist.size() == 0){
			tlist = iSingleSupportSchemeService.getOldestMeasurePointsByAreaId(areaId,upTunnelId,downTunnelId);
		}else{
			for(int i = 0 ; i < slist.size();i++){
				tlist.add(singleSupportMpinfoMapper.getSingleSupportMpinfoByMpId(slist.get(i)));
			}
		}

		//根据冒泡法，对通道进行排序
		SingleSupportMpinfo temp = null;
		for (int i = tlist.size() - 1; i > 0; --i)
		{
			for (int j = 0; j < i; ++j)
			{
				if (tlist.get(j + 1).getDistance().compareTo(tlist.get(j).getDistance()) < 0)
				{
					temp = tlist.get(j);
					tlist.set(j, tlist.get(j + 1));
					tlist.set(j + 1, temp);
				}
			}
		}

		temp = null;

		for (int i = tlist.size() - 1; i > 0; --i)
		{
			for (int j = 0; j < i; ++j)
			{
				if (tlist.get(j + 1).getLocation().compareTo(tlist.get(j).getLocation()) < 0)
				{
					temp = tlist.get(j);
					tlist.set(j, tlist.get(j + 1));
					tlist.set(j + 1, temp);
				}
			}
		}

		//转化为int数组arri
		int[] arri = new int[tlist.size()];
		for(int i=0;i<tlist.size();i++){
			arri[i] = tlist.get(i).getMeasurePointId();
		}

		return arri;

	}
}
