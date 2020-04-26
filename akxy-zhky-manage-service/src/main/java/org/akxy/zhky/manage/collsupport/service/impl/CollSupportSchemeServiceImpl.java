package org.akxy.zhky.manage.collsupport.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.akxy.zhky.manage.collsupport.service.ICollSupportSchemeService;
import org.akxy.zhky.manage.config.service.IConfigService;
import org.akxy.zhky.manage.dao.mapper.CollSupportMpMapper;
import org.akxy.zhky.manage.dao.mapper.CollSupportMpinfoMapper;
import org.akxy.zhky.manage.dao.mapper.CollSupportSiMapper;
import org.akxy.zhky.manage.dao.mapper.CollSupportSlMapper;
import org.akxy.zhky.manage.pojo.CollSupportMp;
import org.akxy.zhky.manage.pojo.CollSupportMpExample;
import org.akxy.zhky.manage.pojo.CollSupportMpExample.Criteria;
import org.akxy.zhky.manage.pojo.CollSupportMpinfo;
import org.akxy.zhky.manage.pojo.CollSupportSiExample;
import org.akxy.zhky.manage.pojo.CollSupportSiKey;
import org.akxy.zhky.manage.pojo.CollSupportSl;
import org.akxy.zhky.manage.pojo.CollSupportSlExample;
import org.akxy.zhky.manage.pojo.Config;
import org.akxy.zhky.manage.pojo.SchemeCollSupportMp;
import org.akxy.zhky.manage.pojo.Sensor;
import org.akxy.zhky.manage.pojo.Tunnel;
import org.akxy.zhky.manage.sensor.service.ISensorService;
import org.akxy.zhky.manage.tunnel.service.ITunnelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: ICollSupportSchemeServiceImpl.java
 * @Description:
 * @date: 2018年8月27日
 */
@Service
public class CollSupportSchemeServiceImpl implements ICollSupportSchemeService{
	@Autowired
	private ITunnelService iTunnelService;
	@Autowired
	private ISensorService iSensorService;
	@Autowired
	private IConfigService iConfigService;
	@Autowired
	private CollSupportSlMapper collSupportSlMapper;
	@Autowired
	private CollSupportSiMapper collSupportSiMapper;
	@Autowired
	private CollSupportMpMapper collSupportMpMapper;
	@Autowired
	private CollSupportMpinfoMapper collSupportMpinfoMapper;

	@Override
	public List<CollSupportSl> getCollSupportSl(int areaId) {
		CollSupportSlExample example = new CollSupportSlExample();
		org.akxy.zhky.manage.pojo.CollSupportSlExample.Criteria criteria = example.createCriteria();
		criteria.andAreaIdEqualTo(areaId);
		return collSupportSlMapper.selectByExample(example);
	}
	@Override
	public boolean addCollSupportSl(CollSupportSl collSupportSl){
		return collSupportSlMapper.insert(collSupportSl) > 0 ?true:false;
	}

	@Override
	public List<CollSupportMp> getCollSupportMp(int schemeId) {
		//现在si表中找到方案id下的所有mpid
		CollSupportSiExample siExample = new CollSupportSiExample();
		org.akxy.zhky.manage.pojo.CollSupportSiExample.Criteria siCriteria = siExample.createCriteria();
		siCriteria.andSchemeIdEqualTo(schemeId);
		List<CollSupportSiKey> siList = collSupportSiMapper.selectByExample(siExample);

		//构建方案中的测点id list
		List<Integer> mpIdList = new ArrayList<>();
		for(CollSupportSiKey si:siList){
			mpIdList.add(si.getMeasurePointId());
		}
		CollSupportMpExample mpExample = new CollSupportMpExample();
		org.akxy.zhky.manage.pojo.CollSupportMpExample.Criteria mpCriteria = mpExample.createCriteria();
		mpCriteria.andIdIn(mpIdList);

		return collSupportMpMapper.selectByExample(mpExample);
	}

	@Override
	public List<CollSupportSiKey> getCollSupportSiKey(int schemeId) {
		CollSupportSiExample example = new CollSupportSiExample();
		org.akxy.zhky.manage.pojo.CollSupportSiExample.Criteria criteria = example.createCriteria();
		criteria.andSchemeIdEqualTo(schemeId);
		return collSupportSiMapper.selectByExample(example);
	}
	@Override
	public boolean addCollSupportSiKey(CollSupportSiKey collSupportSiKey){

		return collSupportSiMapper.insert(collSupportSiKey) > 0 ?true:false;

	}

	@Override
	public Map<String,Object> getCollSupportAllScheme(int areaId ,String stime ,String etime) throws ParseException {

		Map<String,Object> res = new HashMap<>();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		Config c1 = iConfigService.findConfigWithType("coll", "location", 0);
		Config c2 = iConfigService.findConfigWithType("coll", "position", 0);
		Config c3 = iConfigService.findConfigWithType("coll", "direction", 0);


		//获取所有的sensor类型
		List<Sensor> sensorList = iSensorService.getSensorByType("coll");
		sensorList.addAll(iSensorService.getSensorByType("expan"));
		sensorList.addAll(iSensorService.getSensorByType("angle"));

		//找区域下的所有巷道
		List<Tunnel> tunnelList = iTunnelService.findTunnelByAreaId(areaId);

		Map<String,Object> staticData = new HashMap<>();

		staticData.put("location", c1==null?"上部#中部#下部":c1.getStrValue());
		staticData.put("position", c2==null?"前柱#后柱":c2.getStrValue());
		staticData.put("direction", c3==null?"工作面方向#巷道方向":c3.getStrValue());
		staticData.put("tunnel", tunnelList);
		staticData.put("sensor", sensorList);


		//用于保存完整方案的ist
		List<Map<String,Object>> schemeList = new ArrayList<>();
		//先找到所有的方案
		List<CollSupportSl> slList = new ArrayList<>();
		if(stime == null || stime.equals("")){
			slList = collSupportSlMapper.getLastSchemes(areaId);
		}else{
			CollSupportSlExample example = new CollSupportSlExample();
			org.akxy.zhky.manage.pojo.CollSupportSlExample.Criteria criteria = example.createCriteria();
			criteria.andAreaIdEqualTo(areaId);
			criteria.andCreateTimeBetween(sdf.parse(stime), sdf.parse(etime));
			example.setOrderByClause("create_time");
			slList =  collSupportSlMapper.selectByExample(example);
		}

		//在找到所有的含方案id的所有测点
		List<SchemeCollSupportMp> mpList = collSupportMpMapper.selectSchemeMp(areaId);
		//根据方案和测点中的方案id，组装方案到map里，将组装好的多个方案添加到list中
		for(CollSupportSl sl:slList){
			//List<Map<String,Object>> scheme = new ArrayList<Map<String,Object>>();
			Map<String,Object> schemeSl = new HashMap<>();

			List<SchemeCollSupportMp> schemeMp = new ArrayList<>();
			for(SchemeCollSupportMp mp:mpList){
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
	public void addOrUpdateMps(List<CollSupportMp> collSupportMps) {
		CollSupportMp oldMp = null;
		for(CollSupportMp collSupportMp:collSupportMps)
		{
			oldMp = collSupportMpMapper.selectMpByName(collSupportMp.getName(),collSupportMp.getAreaId());
			//如果已存在该测点名称，就按照名称更新该测点
			if(oldMp!=null){
				CollSupportMpExample example = new CollSupportMpExample();
				Criteria criteria = example.createCriteria();
				criteria.andNameEqualTo(collSupportMp.getName());
				criteria.andAreaIdEqualTo(collSupportMp.getAreaId());
				if(!oldMp.toString().equals(collSupportMp.toString())){
					collSupportMp.setId(oldMp.getId());
					collSupportMpMapper.updateByExample(collSupportMp, example);
				}
			}else{
				//如果也不存在该测点名称，就新增
				collSupportMp.setId(null);
				collSupportMpMapper.insert(collSupportMp);
			}
		}
	}

	@Override
	public CollSupportSl getCollSupportSlByName(String name,int areaId) {
		CollSupportSlExample example = new CollSupportSlExample();
		org.akxy.zhky.manage.pojo.CollSupportSlExample.Criteria criteria = example.createCriteria();
		criteria.andNameEqualTo(name);
		criteria.andAreaIdEqualTo(areaId);
		return collSupportSlMapper.selectByExample(example).get(0);
	}

	@Override
	public CollSupportMp getCollSupportMpByName(String name,int areaId) {
		return collSupportMpMapper.selectMpByName(name,areaId);
	}

	@Override
	public boolean deleteCollSupportSiBySchemeId(int schemeId) {
		CollSupportSiExample example = new CollSupportSiExample();
		org.akxy.zhky.manage.pojo.CollSupportSiExample.Criteria criteria = example.createCriteria();
		criteria.andSchemeIdEqualTo(schemeId);
		collSupportSiMapper.deleteByExample(example);
		return collSupportSiMapper.deleteByExample(example) > 0 ?true:false;
	}

	@Override
	public boolean updateCollSupportSl(CollSupportSl collSupportSl) {
		return collSupportSlMapper.updateByPrimaryKey(collSupportSl) > 0 ?true:false;
	}

	@Override
	public List<Integer> getAllSchemeBySect(String stime, String etime, int areaId,int upTunnelId,int downTunnelId) {
		return collSupportSlMapper.getAllSchemeBySect(stime, etime,areaId, upTunnelId, downTunnelId);
	}

	@Override
	public List<CollSupportMpinfo> getOldestMeasurePointsByAreaId(int areaId,int upTunnelId,int downTunnelId) {
		CollSupportSl scheme = collSupportSlMapper.getLastScheme(areaId);

		List<CollSupportMpinfo> mps = new ArrayList<>();
		if(upTunnelId==0&&downTunnelId==0){
			mps = collSupportMpinfoMapper.getCollSupportMpinfoBySchemeId(scheme.getId());
		}else{
			mps = collSupportMpinfoMapper.getCollSupportMpinfoBySchemeIdAndTunnel(scheme.getId(),upTunnelId,downTunnelId);
		}
		return mps;
	}

	@Override
	public int deleteScheme(int schemeId) {
		/*删除_sl表*/
		int slFlag = collSupportSlMapper.deleteByPrimaryKey(schemeId);
		/*删除_si表*/
		CollSupportSiExample example = new CollSupportSiExample();
		org.akxy.zhky.manage.pojo.CollSupportSiExample.Criteria criteria = example.createCriteria();
		criteria.andSchemeIdEqualTo(schemeId);
		int siFlag = collSupportSiMapper.deleteByExample(example);
		if(slFlag > 0 || siFlag > 0){
			return 0;
		}else{
			return -1;
		}
	}

	@Override
	public String getMpStartTime(int areaId, int mpId) {
		String time = collSupportSlMapper.getMpStartTime(areaId, mpId);
		return time;
	}
}
