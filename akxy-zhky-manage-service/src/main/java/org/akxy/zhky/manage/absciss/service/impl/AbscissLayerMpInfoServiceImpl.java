package org.akxy.zhky.manage.absciss.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.akxy.zhky.manage.absciss.service.IAbscissLayerMpInfoService;
import org.akxy.zhky.manage.absciss.service.IAbscissLayerSchemeService;
import org.akxy.zhky.manage.dao.mapper.AbscissLayerMpMapper;
import org.akxy.zhky.manage.dao.mapper.AbscissLayerMpinfoMapper;
import org.akxy.zhky.manage.dao.mapper.IndexTableMapper;
import org.akxy.zhky.manage.pojo.AbscissLayerMp;
import org.akxy.zhky.manage.pojo.AbscissLayerMpinfo;
import org.akxy.zhky.manage.pojo.IndexTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: AbscissLayerMpInfoServiceImpl.java
 * @Description:
 * @date: 2018年7月3日
 */
@Service
public class AbscissLayerMpInfoServiceImpl implements IAbscissLayerMpInfoService{
	@Autowired
	private IndexTableMapper indexMapperTable;
	@Autowired
	private AbscissLayerMpMapper abscissLayerMpMapper;
	@Autowired
	private AbscissLayerMpinfoMapper abscissLayerMpinfoMapper;
	@Autowired
	private IAbscissLayerSchemeService iAbscissLayerSchemeService;

	@Override
	public List<IndexTable> getAbscissLayerNowSchemeMp(int areaId) {
		return indexMapperTable.getAbscissLayerNowSchemecMpList(areaId);
	}

	@Override
	public List<IndexTable> getAbscissLayerAllMp(int areaId) {
		return indexMapperTable.getAbscissLayerAllMpList(areaId);
	}
	@Override
	public AbscissLayerMp getAbscissLayerMpById(Integer mpId) {
		return abscissLayerMpMapper.selectByPrimaryKey(mpId);
	}

	@Override
	public AbscissLayerMp getAbscissLayerMpByName(String mpName,int areaId) {
		return abscissLayerMpMapper.selectMpByName(mpName,areaId);
	}

	@Override
	public int[] getAbscissLayerMpBySect(String stime, String etime, int areaId,int upTunnelId,int downTunnelId) {
		List<Integer> slist = new ArrayList<Integer>();

		slist = iAbscissLayerSchemeService.getAllSchemeBySect(stime, etime,areaId,upTunnelId,downTunnelId);

		List<AbscissLayerMpinfo> tlist = new ArrayList<AbscissLayerMpinfo>();
		if(slist.size() == 0){
			tlist = iAbscissLayerSchemeService.getOldestMeasurePointsByAreaId(areaId,upTunnelId,downTunnelId);
		}else{
			for(int i = 0 ; i < slist.size();i++){
				tlist.add(abscissLayerMpinfoMapper.getAbscissLayerMpinfoByMpId(slist.get(i)));
			}
		}

		//根据冒泡法，对通道进行排序
		AbscissLayerMpinfo temp = null;
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
