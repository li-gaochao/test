package org.akxy.zhky.manage.absciss.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.akxy.zhky.manage.absciss.service.IAbscissLayerSchemeService;
import org.akxy.zhky.manage.dao.mapper.AbscissLayerMpMapper;
import org.akxy.zhky.manage.dao.mapper.AbscissLayerMpinfoMapper;
import org.akxy.zhky.manage.dao.mapper.AbscissLayerSiMapper;
import org.akxy.zhky.manage.dao.mapper.AbscissLayerSlMapper;
import org.akxy.zhky.manage.pojo.AbscissLayerMp;
import org.akxy.zhky.manage.pojo.AbscissLayerMpExample;
import org.akxy.zhky.manage.pojo.AbscissLayerMpExample.Criteria;
import org.akxy.zhky.manage.pojo.AbscissLayerMpinfo;
import org.akxy.zhky.manage.pojo.AbscissLayerSiExample;
import org.akxy.zhky.manage.pojo.AbscissLayerSiKey;
import org.akxy.zhky.manage.pojo.AbscissLayerSl;
import org.akxy.zhky.manage.pojo.AbscissLayerSlExample;
import org.akxy.zhky.manage.pojo.SchemeAbscissLayerMp;
import org.akxy.zhky.manage.pojo.Sensor;
import org.akxy.zhky.manage.pojo.Tunnel;
import org.akxy.zhky.manage.sensor.service.ISensorService;
import org.akxy.zhky.manage.tunnel.service.ITunnelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: AbscissLayerSchemeServiceImpl.java
 * @Description:
 * @date: 2018年8月27日
 */
@Service
public class AbscissLayerSchemeServiceImpl implements IAbscissLayerSchemeService{
	@Autowired
	private ITunnelService iTunnelService;
	@Autowired
	private ISensorService iSensorService;
	@Autowired
	private AbscissLayerSlMapper abscissLayerSlMapper;
	@Autowired
	private AbscissLayerMpMapper abscissLayerMpMapper;
	@Autowired
	private AbscissLayerSiMapper abscissLayerSiMapper;
	@Autowired
	private AbscissLayerMpinfoMapper abscissLayerMpinfoMapper;

	@Override
	public List<AbscissLayerSl> getAbscissLayerSl(int areaId) {
		AbscissLayerSlExample example = new AbscissLayerSlExample();
		org.akxy.zhky.manage.pojo.AbscissLayerSlExample.Criteria criteria = example.createCriteria();
		criteria.andAreaIdEqualTo(areaId);
		return abscissLayerSlMapper.selectByExample(example);
	}
	@Override
	public boolean addAbscissLayerSl(AbscissLayerSl abscissLayerSl){
		return abscissLayerSlMapper.insertSelective(abscissLayerSl) > 0 ?true:false;
	}

	@Override
	public List<AbscissLayerMp> getAbscissLayerMp(int schemeId) {
		//现在si表中找到方案id下的所有mpid
		AbscissLayerSiExample siExample = new AbscissLayerSiExample();
		org.akxy.zhky.manage.pojo.AbscissLayerSiExample.Criteria siCriteria = siExample.createCriteria();
		siCriteria.andSchemeIdEqualTo(schemeId);
		List<AbscissLayerSiKey> siList = abscissLayerSiMapper.selectByExample(siExample);

		//构建方案中的测点id list
		List<Integer> mpIdList = new ArrayList<>();
		for(AbscissLayerSiKey si:siList){
			mpIdList.add(si.getMeasurePointId());
		}
		AbscissLayerMpExample mpExample = new AbscissLayerMpExample();
		org.akxy.zhky.manage.pojo.AbscissLayerMpExample.Criteria mpCriteria = mpExample.createCriteria();
		mpCriteria.andIdIn(mpIdList);

		return abscissLayerMpMapper.selectByExample(mpExample);
	}

	@Override
	public List<AbscissLayerSiKey> getAbscissLayerSiKey(int schemeId) {
		AbscissLayerSiExample example = new AbscissLayerSiExample();
		org.akxy.zhky.manage.pojo.AbscissLayerSiExample.Criteria criteria = example.createCriteria();
		criteria.andSchemeIdEqualTo(schemeId);
		return abscissLayerSiMapper.selectByExample(example);
	}
	@Override
	public boolean addAbscissLayerSiKey(AbscissLayerSiKey abscissLayerSiKey){

		return abscissLayerSiMapper.insert(abscissLayerSiKey) > 0 ?true:false;

	}

	@Override
	public Map<String,Object> getAbscissLayerAllScheme(int areaId ,String stime ,String etime) throws ParseException {
		Map<String,Object> res = new HashMap<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//获取所有的sensor类型
		List<Sensor> sensorList = iSensorService.getSensorByType("absciss");
		//找区域下的所有巷道
		List<Tunnel> tunnelList = iTunnelService.findTunnelByAreaId(areaId);

		Map<String,Object> staticData = new HashMap<>();

		staticData.put("tunnel", tunnelList);
		staticData.put("sensor", sensorList);

		//用于保存完整方案的ist
		List<Map<String,Object>> schemeList = new ArrayList<>();
		//先找到所有的方案
		List<AbscissLayerSl> slList = new ArrayList<>();
		if(stime == null || stime.equals("")){
			slList = abscissLayerSlMapper.getLastSchemes(areaId);
		}else{
			AbscissLayerSlExample example = new AbscissLayerSlExample();
			org.akxy.zhky.manage.pojo.AbscissLayerSlExample.Criteria criteria = example.createCriteria();
			criteria.andAreaIdEqualTo(areaId);
			criteria.andCreateTimeBetween(sdf.parse(stime), sdf.parse(etime));
			example.setOrderByClause("create_time");
			slList =  abscissLayerSlMapper.selectByExample(example);
		}

		//在找到所有的含方案id的所有测点
		List<SchemeAbscissLayerMp> mpList = abscissLayerMpMapper.selectSchemeMp(areaId);
		//根据方案和测点中的方案id，组装方案到map里，将组装好的多个方案添加到list中
		for(AbscissLayerSl sl:slList){
			Map<String,Object> schemeSl = new HashMap<>();

			List<SchemeAbscissLayerMp> schemeMp = new ArrayList<>();
			for(SchemeAbscissLayerMp mp:mpList){
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
	public void addOrUpdateMps(List<AbscissLayerMp> abscissLayerMps) {
		AbscissLayerMp oldMp = null;
		for(AbscissLayerMp abscissLayerMp:abscissLayerMps)
		{
			oldMp = abscissLayerMpMapper.selectMpByName(abscissLayerMp.getName(),abscissLayerMp.getAreaId());
			//如果已存在该测点名称，就按照名称更新该测点
			if(oldMp!=null){
				AbscissLayerMpExample example = new AbscissLayerMpExample();
				Criteria criteria = example.createCriteria();
				criteria.andNameEqualTo(abscissLayerMp.getName());
				criteria.andAreaIdEqualTo(abscissLayerMp.getAreaId());
				if(!oldMp.toString().equals(abscissLayerMp.toString())){
					abscissLayerMpMapper.updateByExample(abscissLayerMp, example);
				}
			}else{
				//如果也不存在该测点名称，就新增
				abscissLayerMp.setId(null);
				abscissLayerMpMapper.insert(abscissLayerMp);

			}
		}
	}

	@Override
	public AbscissLayerSl getAbscissLayerSlByName(String name,int areaId) {
		AbscissLayerSlExample example = new AbscissLayerSlExample();
		org.akxy.zhky.manage.pojo.AbscissLayerSlExample.Criteria criteria = example.createCriteria();
		criteria.andNameEqualTo(name);
		criteria.andAreaIdEqualTo(areaId);
		return abscissLayerSlMapper.selectByExample(example).get(0);
	}

	@Override
	public AbscissLayerMp getAbscissLayerMpByName(String name,int areaId) {
		return abscissLayerMpMapper.selectMpByName(name,areaId);
	}

	@Override
	public boolean deleteAbscissLayerSiBySchemeId(int schemeId) {
		AbscissLayerSiExample example = new AbscissLayerSiExample();
		org.akxy.zhky.manage.pojo.AbscissLayerSiExample.Criteria criteria = example.createCriteria();
		criteria.andSchemeIdEqualTo(schemeId);
		abscissLayerSiMapper.deleteByExample(example);
		return abscissLayerSiMapper.deleteByExample(example) > 0 ?true:false;
	}

	@Override
	public boolean updateAbscissLayerSl(AbscissLayerSl abscissLayerSl) {
		return abscissLayerSlMapper.updateByPrimaryKey(abscissLayerSl) > 0 ?true:false;
	}

	@Override
	public List<Integer> getAllSchemeBySect(String stime, String etime, int areaId,int upTunnelId,int downTunnelId) {
		return abscissLayerSlMapper.getAllSchemeBySect(stime, etime,areaId,upTunnelId,downTunnelId);
	}

	@Override
	public List<AbscissLayerMpinfo> getOldestMeasurePointsByAreaId(int areaId,int upTunnelId,int downTunnelId) {
		AbscissLayerSl scheme = abscissLayerSlMapper.getLastScheme(areaId);

		List<AbscissLayerMpinfo> mps = new ArrayList<>();
		if(scheme!=null){
			if(upTunnelId==0&&downTunnelId==0){
				mps = abscissLayerMpinfoMapper.getAbscissLayerMpinfoBySchemeId(scheme.getId());
			}else{
				mps = abscissLayerMpinfoMapper.getAbscissLayerMpinfoBySchemeIdAndTunnel(scheme.getId(),upTunnelId,downTunnelId);
			}
		}
		return mps;
	}

	@Override
	public int deleteScheme(int schemeId) {
		/*删除_sl表*/
		int slFlag = abscissLayerSlMapper.deleteByPrimaryKey(schemeId);
		/*删除_si表*/
		AbscissLayerSiExample example = new AbscissLayerSiExample();
		org.akxy.zhky.manage.pojo.AbscissLayerSiExample.Criteria criteria = example.createCriteria();
		criteria.andSchemeIdEqualTo(schemeId);
		int siFlag = abscissLayerSiMapper.deleteByExample(example);
		if(slFlag > 0 || siFlag > 0){
			return 0;
		}else{
			return -1;
		}
	}

	@Override
	public String getMpStartTime(int areaId, int mpId) {
		String time = abscissLayerSlMapper.getMpStartTime(areaId, mpId);
		return time;
	}

}
