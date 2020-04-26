package org.akxy.zhky.manage.stress.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.akxy.zhky.manage.dao.mapper.StressMpMapper;
import org.akxy.zhky.manage.dao.mapper.StressMpinfoMapper;
import org.akxy.zhky.manage.dao.mapper.StressSiMapper;
import org.akxy.zhky.manage.dao.mapper.StressSlMapper;
import org.akxy.zhky.manage.pojo.SchemeStressMp;
import org.akxy.zhky.manage.pojo.Sensor;
import org.akxy.zhky.manage.pojo.StressMp;
import org.akxy.zhky.manage.pojo.StressMpExample;
import org.akxy.zhky.manage.pojo.StressMpExample.Criteria;
import org.akxy.zhky.manage.pojo.StressMpinfo;
import org.akxy.zhky.manage.pojo.StressSiExample;
import org.akxy.zhky.manage.pojo.StressSiKey;
import org.akxy.zhky.manage.pojo.StressSl;
import org.akxy.zhky.manage.pojo.StressSlExample;
import org.akxy.zhky.manage.pojo.Tunnel;
import org.akxy.zhky.manage.sensor.service.ISensorService;
import org.akxy.zhky.manage.stress.service.IStressSchemeService;
import org.akxy.zhky.manage.tunnel.service.ITunnelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: IStressSchemeServiceImpl.java
 * @Description:
 * @date: 2018年8月27日
 */
@Service
public class StressSchemeServiceImpl implements IStressSchemeService{
	@Autowired
	private StressSlMapper stressSlMapper;
	@Autowired
	private StressSiMapper stressSiMapper;
	@Autowired
	private StressMpMapper stressMpMapper;
	@Autowired
	private ITunnelService iTunnelService;
	@Autowired
	private ISensorService iSensorService;
	@Autowired
	private StressMpinfoMapper stressMpinfoMapper;

	@Override
	public List<StressSl> getStressSl(int areaId) {
		StressSlExample example = new StressSlExample();
		org.akxy.zhky.manage.pojo.StressSlExample.Criteria criteria = example.createCriteria();
		criteria.andAreaIdEqualTo(areaId);
		return stressSlMapper.selectByExample(example);
	}
	@Override
	public boolean addStressSl(StressSl stressSl){
		return stressSlMapper.insert(stressSl) > 0 ?true:false;
	}

	@Override
	public List<StressMp> getStressMp(int schemeId) {
		//现在si表中找到方案id下的所有mpid
		StressSiExample siExample = new StressSiExample();
		org.akxy.zhky.manage.pojo.StressSiExample.Criteria siCriteria = siExample.createCriteria();
		siCriteria.andSchemeIdEqualTo(schemeId);
		List<StressSiKey> siList = stressSiMapper.selectByExample(siExample);

		//构建方案中的测点id list
		List<Integer> mpIdList = new ArrayList<>();
		for(StressSiKey si:siList){
			mpIdList.add(si.getMeasurePointId());
		}
		StressMpExample mpExample = new StressMpExample();
		org.akxy.zhky.manage.pojo.StressMpExample.Criteria mpCriteria = mpExample.createCriteria();
		mpCriteria.andIdIn(mpIdList);

		return stressMpMapper.selectByExample(mpExample);
	}

	@Override
	public List<StressSiKey> getStressSiKey(int schemeId) {
		StressSiExample example = new StressSiExample();
		org.akxy.zhky.manage.pojo.StressSiExample.Criteria criteria = example.createCriteria();
		criteria.andSchemeIdEqualTo(schemeId);
		return stressSiMapper.selectByExample(example);
	}
	@Override
	public boolean addStressSiKey(StressSiKey stressSiKey){

		return stressSiMapper.insert(stressSiKey) > 0 ?true:false;

	}

	@Override
	public Map<String,Object> getStressAllScheme(int areaId ,String stime ,String etime) throws ParseException {

		Map<String,Object> res = new HashMap<>();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		//获取所有的sensor类型
		List<Sensor> sensorList = iSensorService.getSensorByType("stress");

		//找区域下的所有巷道
		List<Tunnel> tunnelList = iTunnelService.findTunnelByAreaId(areaId);

		Map<String,Object> staticData = new HashMap<>();

		staticData.put("tunnel", tunnelList);
		staticData.put("sensor", sensorList);

		//用于保存完整方案的ist
		List<Map<String,Object>> schemeList = new ArrayList<>();
		//先找到所有的方案
		List<StressSl> slList = new ArrayList<>();
		if(stime == null || stime.equals("")){
			slList = stressSlMapper.getLastSchemes(areaId);
		}else{
			StressSlExample example = new StressSlExample();
			org.akxy.zhky.manage.pojo.StressSlExample.Criteria criteria = example.createCriteria();
			criteria.andAreaIdEqualTo(areaId);
			criteria.andCreateTimeBetween(sdf.parse(stime), sdf.parse(etime));
			example.setOrderByClause("create_time");
			slList =  stressSlMapper.selectByExample(example);
		}

		//在找到所有的含方案id的所有测点
		List<SchemeStressMp> mpList = stressMpMapper.selectSchemeMp(areaId);
		//根据方案和测点中的方案id，组装方案到map里，将组装好的多个方案添加到list中
		for(StressSl sl:slList){
			//List<Map<String,Object>> scheme = new ArrayList<Map<String,Object>>();
			Map<String,Object> schemeSl = new HashMap<>();

			List<SchemeStressMp> schemeMp = new ArrayList<>();
			for(SchemeStressMp mp:mpList){
				if(mp.getSchemeId().equals(sl.getId())){
					schemeMp.add(mp);
				}
			}
			schemeSl.put("scheme", sl);
			schemeSl.put("mp", schemeMp);

			schemeList.add(schemeSl);
		}
		res.put("schemeList", schemeList);
		res.put("staticData", staticData);

		return res;
	}
	@Override
	public void addOrUpdateMps(List<StressMp> stressMps) {
		StressMp oldMp = null;
		for(StressMp stressMp:stressMps)
		{
			oldMp = stressMpMapper.selectMpByName(stressMp.getName(),stressMp.getAreaId());
			//如果已存在该测点名称，就按照名称更新该测点
			if(oldMp!=null){
				StressMpExample example = new StressMpExample();
				Criteria criteria = example.createCriteria();
				criteria.andNameEqualTo(stressMp.getName());
				criteria.andAreaIdEqualTo(stressMp.getAreaId());
				if(!oldMp.toString().equals(stressMp.toString())){
					stressMp.setId(oldMp.getId());
					stressMpMapper.updateByExample(stressMp, example);
				}
			}else{
				//如果也不存在该测点名称，就新增
				stressMp.setId(null);
				stressMpMapper.insert(stressMp);
			}
		}
	}

	@Override
	public StressSl getStressSlByName(String name,int areaId) {
		StressSlExample example = new StressSlExample();
		org.akxy.zhky.manage.pojo.StressSlExample.Criteria criteria = example.createCriteria();
		criteria.andNameEqualTo(name);
		criteria.andAreaIdEqualTo(areaId);
		return stressSlMapper.selectByExample(example).get(0);
	}

	@Override
	public StressMp getStressMpByName(String name,int areaId) {
		return stressMpMapper.selectMpByName(name,areaId);
	}

	@Override
	public boolean deleteStressSiBySchemeId(int schemeId) {
		StressSiExample example = new StressSiExample();
		org.akxy.zhky.manage.pojo.StressSiExample.Criteria criteria = example.createCriteria();
		criteria.andSchemeIdEqualTo(schemeId);
		stressSiMapper.deleteByExample(example);
		return stressSiMapper.deleteByExample(example) > 0 ?true:false;
	}

	@Override
	public boolean updateStressSl(StressSl stressSl) {
		return stressSlMapper.updateByPrimaryKey(stressSl) > 0 ?true:false;
	}

	@Override
	public List<Integer> getAllSchemeBySect(String stime, String etime, int areaId,int upTunnelId,int downTunnelId) {
		return stressSlMapper.getAllSchemeBySect(stime, etime,areaId,upTunnelId,downTunnelId);
	}

	@Override
	public List<StressMpinfo> getOldestMeasurePointsByAreaId(int areaId,int upTunnelId,int downTunnelId) {
		StressSl scheme = stressSlMapper.getLastScheme(areaId);

		List<StressMpinfo> mps = new ArrayList<>();
		if(scheme!=null){
			if(upTunnelId==0&&downTunnelId==0){
				mps = stressMpinfoMapper.getStressMpinfoBySchemeId(scheme.getId());
			}else{
				mps = stressMpinfoMapper.getStressMpinfoBySchemeIdAndTunnel(scheme.getId(),upTunnelId,downTunnelId);
			}

		}
		return mps;
	}

	@Override
	public int deleteScheme(int schemeId) {
		/*删除_sl表*/
		int slFlag = stressSlMapper.deleteByPrimaryKey(schemeId);
		/*删除_si表*/
		StressSiExample example = new StressSiExample();
		org.akxy.zhky.manage.pojo.StressSiExample.Criteria criteria = example.createCriteria();
		criteria.andSchemeIdEqualTo(schemeId);
		int siFlag = stressSiMapper.deleteByExample(example);
		if(slFlag > 0 || siFlag > 0){
			return 0;
		}else{
			return -1;
		}
	}

	@Override
	public String getMpStartTime(int areaId, int mpId) {
		String time = stressSlMapper.getMpStartTime(areaId, mpId);
		return time;
	}
}
