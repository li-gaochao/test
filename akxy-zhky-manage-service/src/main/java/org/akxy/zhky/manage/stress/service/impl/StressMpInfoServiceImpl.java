package org.akxy.zhky.manage.stress.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.akxy.zhky.manage.dao.mapper.IndexTableMapper;
import org.akxy.zhky.manage.dao.mapper.StressMpMapper;
import org.akxy.zhky.manage.dao.mapper.StressMpinfoMapper;
import org.akxy.zhky.manage.pojo.IndexTable;
import org.akxy.zhky.manage.pojo.StressMp;
import org.akxy.zhky.manage.pojo.StressMpinfo;
import org.akxy.zhky.manage.stress.service.IStressMpInfoService;
import org.akxy.zhky.manage.stress.service.IStressSchemeService;
import org.akxy.zhky.manage.tunnel.service.ITunnelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: StressMpInfoServiceImpl.java
 * @Description:
 * @date: 2018年7月3日
 */
@Service
public class StressMpInfoServiceImpl implements IStressMpInfoService{
	@Autowired
	private StressMpMapper stressMpMapper;
	@Autowired
	private IndexTableMapper indexMapperTable;
	@Autowired
	private StressMpinfoMapper stressMpinfoMapper;
	@Autowired
	private IStressSchemeService iStressSchemeService;
	@Autowired
	private ITunnelService iTunnelService;

	@Override
	public List<IndexTable> getStressNowSchemeMp(int areaId) {
		return indexMapperTable.getStressNowSchemecMpList(areaId);
	}

	@Override
	public List<IndexTable> getStressAllMp(int areaId) {
		return indexMapperTable.getStressAllMpList(areaId);
	}

	@Override
	public StressMp getStressMpById(Integer mpId) {
		return stressMpMapper.selectByPrimaryKey(mpId);
	}
	@Override
	public StressMp getStressMpByName(String mpName,int areaId) {

		return stressMpMapper.selectMpByName(mpName,areaId);
	}

	@Override
	public int[] getStressMpBySect(String stime, String etime, int areaId,int upTunnelId,int downTunnelId) {
		List<Integer> slist = new ArrayList<>();

		slist = iStressSchemeService.getAllSchemeBySect(stime, etime,areaId,upTunnelId,downTunnelId);

		List<StressMpinfo> tlist = new ArrayList<>();
		if(slist.size() == 0){
			tlist = iStressSchemeService.getOldestMeasurePointsByAreaId(areaId,upTunnelId,downTunnelId);
		}else{
			for(int i = 0 ; i < slist.size();i++){
				tlist.add(stressMpinfoMapper.getStressMpinfoByMpId(slist.get(i)));
			}
		}

		//根据冒泡法，对通道进行排序
		StressMpinfo temp = null;
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
				if (iTunnelService.findTunnelById(tlist.get(j + 1).getLocation()).getLayer().compareTo(iTunnelService.findTunnelById(tlist.get(j).getLocation()).getLayer()) < 0)
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

	@Override
	public Float getMinDepthByTunnelId(int tunnelId) {
		return stressMpMapper.getMinDepthByTunnelId(tunnelId);
	}

	@Override
	public Float getMaxDepthByTunnelId(int tunnelId) {
		return stressMpMapper.getMaxDepthByTunnelId(tunnelId);
	}

	@Override
	public List<StressMp> getMpListByTimeTunnelIdDepth(String time, int areaId, int tunnelId, float depth) {
		return stressMpMapper.getMpListByTimeTunnelIdDepth(time, areaId,tunnelId, depth);
	}


}
