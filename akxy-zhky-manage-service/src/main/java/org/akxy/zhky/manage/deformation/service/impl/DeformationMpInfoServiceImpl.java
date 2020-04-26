package org.akxy.zhky.manage.deformation.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.akxy.zhky.manage.dao.mapper.DeformationMpMapper;
import org.akxy.zhky.manage.dao.mapper.DeformationMpinfoMapper;
import org.akxy.zhky.manage.dao.mapper.IndexTableMapper;
import org.akxy.zhky.manage.deformation.service.IDeformationMpInfoService;
import org.akxy.zhky.manage.deformation.service.IDeformationSchemeService;
import org.akxy.zhky.manage.pojo.DeformationMp;
import org.akxy.zhky.manage.pojo.DeformationMpinfo;
import org.akxy.zhky.manage.pojo.IndexTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: DeformationMpInfoServiceImpl.java
 * @Description:
 * @date: 2018年7月3日
 */
@Service
public class DeformationMpInfoServiceImpl implements IDeformationMpInfoService{
	@Autowired
	private IndexTableMapper indexMapperTable;
	@Autowired
	private DeformationMpMapper deformationMpMapper;
	@Autowired
	private DeformationMpinfoMapper deformationMpinfoMapper;
	@Autowired
	private IDeformationSchemeService iDeformationSchemeService;

	@Override
	public List<IndexTable> getDeformationNowSchemeMp(int areaId) {
		return indexMapperTable.getDeformationNowSchemecMpList(areaId);
	}

	@Override
	public List<IndexTable> getDeformationAllMp(int areaId) {
		return indexMapperTable.getDeformationAllMpList(areaId);
	}

	@Override
	public DeformationMp getDeformationMpById(Integer mpId) {
		return deformationMpMapper.selectByPrimaryKey(mpId);
	}
	@Override
	public DeformationMp getDeformationMpByName(String mpName,int areaId) {

		return deformationMpMapper.selectMpByName(mpName,areaId);
	}

	@Override
	public int[] getDeformationMpBySect(String stime, String etime, int areaId,int upTunnelId,int downTunnelId) {
		List<Integer> slist = new ArrayList<Integer>();

		slist = iDeformationSchemeService.getAllSchemeBySect(stime, etime,areaId,upTunnelId,downTunnelId);

		List<DeformationMpinfo> tlist = new ArrayList<DeformationMpinfo>();
		if(slist.size() == 0){
			tlist = iDeformationSchemeService.getOldestMeasurePointsByAreaId(areaId,upTunnelId,downTunnelId);
		}else{
			for(int i = 0 ; i < slist.size();i++){
				tlist.add(deformationMpinfoMapper.getDeformationMpinfoByMpId(slist.get(i)));
			}
		}

		//根据冒泡法，对通道进行排序
		DeformationMpinfo temp = null;
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
