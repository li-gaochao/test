package org.akxy.zhky.manage.anchor.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.akxy.zhky.manage.anchor.service.IAnchorMpInfoService;
import org.akxy.zhky.manage.anchor.service.IAnchorSchemeService;
import org.akxy.zhky.manage.dao.mapper.AnchorMpMapper;
import org.akxy.zhky.manage.dao.mapper.AnchorMpinfoMapper;
import org.akxy.zhky.manage.dao.mapper.IndexTableMapper;
import org.akxy.zhky.manage.pojo.AnchorMp;
import org.akxy.zhky.manage.pojo.AnchorMpinfo;
import org.akxy.zhky.manage.pojo.IndexTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: AnchorMpInfoServiceImpl.java
 * @Description:
 * @date: 2018年7月3日
 */
@Service
public class AnchorMpInfoServiceImpl implements IAnchorMpInfoService{
	@Autowired
	private AnchorMpMapper anchorMpMapper;
	@Autowired
	private IndexTableMapper indexMapperTable;
	@Autowired
	private AnchorMpinfoMapper anchorMpinfoMapper;
	@Autowired
	private IAnchorSchemeService iAnchorSchemeService;

	@Override
	public List<IndexTable> getAnchorNowSchemeMp(int areaId) {
		return indexMapperTable.getAnchorNowSchemecMpList(areaId);
	}

	@Override
	public List<IndexTable> getAnchorAllMp(int areaId) {
		return indexMapperTable.getAnchorAllMpList(areaId);
	}

	@Override
	public AnchorMp getAnchorMpById(Integer mpId) {
		return anchorMpMapper.selectByPrimaryKey(mpId);
	}
	@Override
	public AnchorMp getAnchorMpByName(String mpName,int areaId) {

		return anchorMpMapper.selectMpByName(mpName,areaId);
	}

	@Override
	public int[] getAnchorMpBySect(String stime, String etime, int areaId, int upTunnelId,int downTunnelId) {
		List<Integer> slist = new ArrayList<Integer>();

		slist = iAnchorSchemeService.getAllSchemeBySect(stime, etime,areaId,upTunnelId,downTunnelId);

		List<AnchorMpinfo> tlist = new ArrayList<AnchorMpinfo>();
		if(slist.size() == 0){
			tlist = iAnchorSchemeService.getOldestMeasurePointsByAreaId(areaId,upTunnelId,downTunnelId);
		}else{
			for(int i = 0 ; i < slist.size();i++){
				tlist.add(anchorMpinfoMapper.getAnchorMpinfoByMpId(slist.get(i)));
			}
		}

		//根据冒泡法，对通道进行排序
		AnchorMpinfo temp = null;
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
