package org.akxy.zhky.manage.singlesupport.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.akxy.zhky.manage.dao.mapper.SingleSupportMpMapper;
import org.akxy.zhky.manage.dao.mapper.SingleSupportMpinfoMapper;
import org.akxy.zhky.manage.dao.mapper.SingleSupportSiMapper;
import org.akxy.zhky.manage.dao.mapper.SingleSupportSlMapper;
import org.akxy.zhky.manage.pojo.SchemeSingleSupportMp;
import org.akxy.zhky.manage.pojo.Sensor;
import org.akxy.zhky.manage.pojo.SingleSupportMp;
import org.akxy.zhky.manage.pojo.SingleSupportMpExample;
import org.akxy.zhky.manage.pojo.SingleSupportMpExample.Criteria;
import org.akxy.zhky.manage.pojo.SingleSupportMpinfo;
import org.akxy.zhky.manage.pojo.SingleSupportSiExample;
import org.akxy.zhky.manage.pojo.SingleSupportSiKey;
import org.akxy.zhky.manage.pojo.SingleSupportSl;
import org.akxy.zhky.manage.pojo.SingleSupportSlExample;
import org.akxy.zhky.manage.pojo.Tunnel;
import org.akxy.zhky.manage.sensor.service.ISensorService;
import org.akxy.zhky.manage.singlesupport.service.ISingleSupportSchemeService;
import org.akxy.zhky.manage.tunnel.service.ITunnelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: ISingleSupportSchemeServiceImpl.java
 * @Description:
 * @date: 2018年8月27日
 */
@Service
public class SingleSupportSchemeServiceImpl implements ISingleSupportSchemeService{
	@Autowired
	private ITunnelService iTunnelService;
	@Autowired
	private ISensorService iSensorService;
	@Autowired
	private SingleSupportSlMapper singleSupportSlMapper;
	@Autowired
	private SingleSupportSiMapper singleSupportSiMapper;
	@Autowired
	private SingleSupportMpMapper singleSupportMpMapper;
	@Autowired
	private SingleSupportMpinfoMapper singleSupportMpinfoMapper;

	@Override
	public List<SingleSupportSl> getSingleSupportSl(int areaId) {
		SingleSupportSlExample example = new SingleSupportSlExample();
		org.akxy.zhky.manage.pojo.SingleSupportSlExample.Criteria criteria = example.createCriteria();
		criteria.andAreaIdEqualTo(areaId);
		return singleSupportSlMapper.selectByExample(example);
	}
	@Override
	public boolean addSingleSupportSl(SingleSupportSl singleSupportSl){
		return singleSupportSlMapper.insert(singleSupportSl) > 0 ?true:false;
	}

	@Override
	public List<SingleSupportMp> getSingleSupportMp(int schemeId) {
		//现在si表中找到方案id下的所有mpid
		SingleSupportSiExample siExample = new SingleSupportSiExample();
		org.akxy.zhky.manage.pojo.SingleSupportSiExample.Criteria siCriteria = siExample.createCriteria();
		siCriteria.andSchemeIdEqualTo(schemeId);
		List<SingleSupportSiKey> siList = singleSupportSiMapper.selectByExample(siExample);

		//构建方案中的测点id list
		List<Integer> mpIdList = new ArrayList<>();
		for(SingleSupportSiKey si:siList){
			mpIdList.add(si.getMeasurePointId());
		}
		SingleSupportMpExample mpExample = new SingleSupportMpExample();
		org.akxy.zhky.manage.pojo.SingleSupportMpExample.Criteria mpCriteria = mpExample.createCriteria();
		mpCriteria.andIdIn(mpIdList);

		return singleSupportMpMapper.selectByExample(mpExample);
	}

	@Override
	public List<SingleSupportSiKey> getSingleSupportSiKey(int schemeId) {
		SingleSupportSiExample example = new SingleSupportSiExample();
		org.akxy.zhky.manage.pojo.SingleSupportSiExample.Criteria criteria = example.createCriteria();
		criteria.andSchemeIdEqualTo(schemeId);
		return singleSupportSiMapper.selectByExample(example);
	}
	@Override
	public boolean addSingleSupportSiKey(SingleSupportSiKey singleSupportSiKey){

		return singleSupportSiMapper.insert(singleSupportSiKey) > 0 ?true:false;

	}

	@Override
	public Map<String,Object> getSingleSupportAllScheme(int areaId ,String stime ,String etime) throws ParseException {

		Map<String,Object> res = new HashMap<>();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		//获取所有的sensor类型
		List<Sensor> sensorList = iSensorService.getSensorByType("single");

		//找区域下的所有巷道
		List<Tunnel> tunnelList = iTunnelService.findTunnelByAreaId(areaId);

		Map<String,Object> staticData = new HashMap<>();

		staticData.put("tunnel", tunnelList);
		staticData.put("sensor", sensorList);

		//用于保存完整方案的ist
		List<Map<String,Object>> schemeList = new ArrayList<>();
		//先找到所有的方案
		List<SingleSupportSl> slList = new ArrayList<>();
		if(stime == null || stime.equals("")){
			slList = singleSupportSlMapper.getLastSchemes(areaId);
		}else{
			SingleSupportSlExample example = new SingleSupportSlExample();
			org.akxy.zhky.manage.pojo.SingleSupportSlExample.Criteria criteria = example.createCriteria();
			criteria.andAreaIdEqualTo(areaId);
			criteria.andCreateTimeBetween(sdf.parse(stime), sdf.parse(etime));
			example.setOrderByClause("create_time");
			slList =  singleSupportSlMapper.selectByExample(example);
		}

		//在找到所有的含方案id的所有测点
		List<SchemeSingleSupportMp> mpList = singleSupportMpMapper.selectSchemeMp(areaId);
		//根据方案和测点中的方案id，组装方案到map里，将组装好的多个方案添加到list中
		for(SingleSupportSl sl:slList){
			//List<Map<String,Object>> scheme = new ArrayList<Map<String,Object>>();
			Map<String,Object> schemeSl = new HashMap<>();
			List<SchemeSingleSupportMp> schemeMp = new ArrayList<>();
			for(SchemeSingleSupportMp mp:mpList){
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
	public void addOrUpdateMps(List<SingleSupportMp> singleSupportMps) {
		SingleSupportMp oldMp = null;
		for(SingleSupportMp singleSupportMp:singleSupportMps)
		{
			oldMp = singleSupportMpMapper.selectMpByName(singleSupportMp.getName(),singleSupportMp.getAreaId());
			//如果已存在该测点名称，就按照名称更新该测点
			if(oldMp!=null){
				SingleSupportMpExample example = new SingleSupportMpExample();
				Criteria criteria = example.createCriteria();
				criteria.andNameEqualTo(singleSupportMp.getName());
				criteria.andAreaIdEqualTo(singleSupportMp.getAreaId());
				if(!oldMp.toString().equals(singleSupportMp.toString())){
					singleSupportMp.setId(oldMp.getId());
					singleSupportMpMapper.updateByExample(singleSupportMp, example);
				}
			}else{
				//如果也不存在该测点名称，就新增
				singleSupportMp.setId(null);
				singleSupportMpMapper.insert(singleSupportMp);
			}
		}
	}

	@Override
	public SingleSupportSl getSingleSupportSlByName(String name,int areaId) {
		SingleSupportSlExample example = new SingleSupportSlExample();
		org.akxy.zhky.manage.pojo.SingleSupportSlExample.Criteria criteria = example.createCriteria();
		criteria.andNameEqualTo(name);
		criteria.andAreaIdEqualTo(areaId);
		return singleSupportSlMapper.selectByExample(example).get(0);
	}

	@Override
	public SingleSupportMp getSingleSupportMpByName(String name,int areaId) {
		return singleSupportMpMapper.selectMpByName(name,areaId);
	}

	@Override
	public boolean deleteSingleSupportSiBySchemeId(int schemeId) {
		SingleSupportSiExample example = new SingleSupportSiExample();
		org.akxy.zhky.manage.pojo.SingleSupportSiExample.Criteria criteria = example.createCriteria();
		criteria.andSchemeIdEqualTo(schemeId);
		singleSupportSiMapper.deleteByExample(example);
		return singleSupportSiMapper.deleteByExample(example) > 0 ?true:false;
	}

	@Override
	public boolean updateSingleSupportSl(SingleSupportSl singleSupportSl) {
		return singleSupportSlMapper.updateByPrimaryKey(singleSupportSl) > 0 ?true:false;
	}

	@Override
	public List<Integer> getAllSchemeBySect(String stime, String etime, int areaId,int upTunnelId,int downTunnelId) {
		return singleSupportSlMapper.getAllSchemeBySect(stime, etime,areaId,upTunnelId,downTunnelId);
	}

	@Override
	public List<SingleSupportMpinfo> getOldestMeasurePointsByAreaId(int areaId,int upTunnelId,int downTunnelId) {
		SingleSupportSl scheme = singleSupportSlMapper.getLastScheme(areaId);

		List<SingleSupportMpinfo> mps = new ArrayList<>();
		if(upTunnelId==0&&downTunnelId==0){
			mps = singleSupportMpinfoMapper.getSingleSupportMpinfoBySchemeId(scheme.getId());
		}else{
			mps = singleSupportMpinfoMapper.getSingleSupportMpinfoBySchemeIdAndTunnel(scheme.getId(),upTunnelId,downTunnelId);
		}
		return mps;
	}

	@Override
	public int deleteScheme(int schemeId) {
		/*删除_sl表*/
		int slFlag = singleSupportSlMapper.deleteByPrimaryKey(schemeId);
		/*删除_si表*/
		SingleSupportSiExample example = new SingleSupportSiExample();
		org.akxy.zhky.manage.pojo.SingleSupportSiExample.Criteria criteria = example.createCriteria();
		criteria.andSchemeIdEqualTo(schemeId);
		int siFlag = singleSupportSiMapper.deleteByExample(example);
		if(slFlag > 0 || siFlag > 0){
			return 0;
		}else{
			return -1;
		}
	}

	@Override
	public String getMpStartTime(int areaId, int mpId) {
		String time = singleSupportSlMapper.getMpStartTime(areaId, mpId);
		return time;
	}
}
