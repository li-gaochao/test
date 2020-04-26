package org.akxy.zhky.manage.anchor.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.akxy.zhky.manage.anchor.service.IAnchorSchemeService;
import org.akxy.zhky.manage.dao.mapper.AnchorMpMapper;
import org.akxy.zhky.manage.dao.mapper.AnchorMpinfoMapper;
import org.akxy.zhky.manage.dao.mapper.AnchorSiMapper;
import org.akxy.zhky.manage.dao.mapper.AnchorSlMapper;
import org.akxy.zhky.manage.pojo.AnchorMp;
import org.akxy.zhky.manage.pojo.AnchorMpExample;
import org.akxy.zhky.manage.pojo.AnchorMpExample.Criteria;
import org.akxy.zhky.manage.pojo.AnchorMpinfo;
import org.akxy.zhky.manage.pojo.AnchorSiExample;
import org.akxy.zhky.manage.pojo.AnchorSiKey;
import org.akxy.zhky.manage.pojo.AnchorSl;
import org.akxy.zhky.manage.pojo.AnchorSlExample;
import org.akxy.zhky.manage.pojo.SchemeAnchorMp;
import org.akxy.zhky.manage.pojo.Sensor;
import org.akxy.zhky.manage.pojo.Tunnel;
import org.akxy.zhky.manage.sensor.service.ISensorService;
import org.akxy.zhky.manage.tunnel.service.ITunnelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: IAnchorSchemeServiceImpl.java
 * @Description:
 * @date: 2018年8月27日
 */
@Service
public class AnchorSchemeServiceImpl implements IAnchorSchemeService{
	@Autowired
	private AnchorSlMapper anchorSlMapper;
	@Autowired
	private AnchorSiMapper anchorSiMapper;
	@Autowired
	private AnchorMpMapper anchorMpMapper;
	@Autowired
	private ITunnelService iTunnelService;
	@Autowired
	private ISensorService iSensorService;
	@Autowired
	private AnchorMpinfoMapper anchorMpinfoMapper;

	@Override
	public List<AnchorSl> getAnchorSl(int areaId) {
		AnchorSlExample example = new AnchorSlExample();
		org.akxy.zhky.manage.pojo.AnchorSlExample.Criteria criteria = example.createCriteria();
		criteria.andAreaIdEqualTo(areaId);
		return anchorSlMapper.selectByExample(example);
	}
	@Override
	public boolean addAnchorSl(AnchorSl anchorSl){
		return anchorSlMapper.insert(anchorSl) > 0 ?true:false;
	}

	@Override
	public List<AnchorMp> getAnchorMp(int schemeId) {
		//现在si表中找到方案id下的所有mpid
		AnchorSiExample siExample = new AnchorSiExample();
		org.akxy.zhky.manage.pojo.AnchorSiExample.Criteria siCriteria = siExample.createCriteria();
		siCriteria.andSchemeIdEqualTo(schemeId);
		List<AnchorSiKey> siList = anchorSiMapper.selectByExample(siExample);

		//构建方案中的测点id list
		List<Integer> mpIdList = new ArrayList<>();
		for(AnchorSiKey si:siList){
			mpIdList.add(si.getMeasurePointId());
		}
		AnchorMpExample mpExample = new AnchorMpExample();
		org.akxy.zhky.manage.pojo.AnchorMpExample.Criteria mpCriteria = mpExample.createCriteria();
		mpCriteria.andIdIn(mpIdList);

		return anchorMpMapper.selectByExample(mpExample);
	}

	@Override
	public List<AnchorSiKey> getAnchorSiKey(int schemeId) {
		AnchorSiExample example = new AnchorSiExample();
		org.akxy.zhky.manage.pojo.AnchorSiExample.Criteria criteria = example.createCriteria();
		criteria.andSchemeIdEqualTo(schemeId);
		return anchorSiMapper.selectByExample(example);
	}
	@Override
	public boolean addAnchorSiKey(AnchorSiKey anchorSiKey){

		return anchorSiMapper.insert(anchorSiKey) > 0 ?true:false;

	}

	@Override
	public Map<String,Object> getAnchorAllScheme(int areaId ,String stime ,String etime) throws ParseException {
		Map<String,Object> res = new HashMap<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		//获取所有的sensor类型
		List<Sensor> sensorList = iSensorService.getSensorByType("anchor");

		//找区域下的所有巷道
		List<Tunnel> tunnelList = iTunnelService.findTunnelByAreaId(areaId);

		Map<String,Object> staticData = new HashMap<>();

		staticData.put("tunnel", tunnelList);
		staticData.put("sensor", sensorList);

		//用于保存完整方案的ist
		List<Map<String,Object>> schemeList = new ArrayList<>();
		//先找到所有的方案
		List<AnchorSl> slList = new ArrayList<>();
		if(stime == null || stime.equals("")){
			slList = anchorSlMapper.getLastSchemes(areaId);
		}else{
			AnchorSlExample example = new AnchorSlExample();
			org.akxy.zhky.manage.pojo.AnchorSlExample.Criteria criteria = example.createCriteria();
			criteria.andAreaIdEqualTo(areaId);
			criteria.andCreateTimeBetween(sdf.parse(stime), sdf.parse(etime));
			example.setOrderByClause("create_time");
			slList =  anchorSlMapper.selectByExample(example);
		}

		//在找到所有的含方案id的所有测点
		List<SchemeAnchorMp> mpList = anchorMpMapper.selectSchemeMp(areaId);
		//根据方案和测点中的方案id，组装方案到map里，将组装好的多个方案添加到list中
		for(AnchorSl sl:slList){
			//List<Map<String,Object>> scheme = new ArrayList<Map<String,Object>>();
			Map<String,Object> schemeSl = new HashMap<>();

			List<SchemeAnchorMp> schemeMp = new ArrayList<>();
			for(SchemeAnchorMp mp:mpList){
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
	public void addOrUpdateMps(List<AnchorMp> anchorMps) {
		AnchorMp oldMp = null;
		for(AnchorMp anchorMp:anchorMps)
		{
			oldMp = anchorMpMapper.selectMpByName(anchorMp.getName(),anchorMp.getAreaId());
			//如果已存在该测点名称，就按照名称更新该测点
			if(oldMp!=null){
				AnchorMpExample example = new AnchorMpExample();
				Criteria criteria = example.createCriteria();
				criteria.andNameEqualTo(anchorMp.getName());
				criteria.andAreaIdEqualTo(anchorMp.getAreaId());
				if(!oldMp.toString().equals(anchorMp.toString())){
					anchorMp.setId(oldMp.getId());
					anchorMpMapper.updateByExample(anchorMp, example);
				}
			}else{
				//如果也不存在该测点名称，就新增
				anchorMp.setId(null);
				anchorMpMapper.insert(anchorMp);
			}
		}
	}

	@Override
	public AnchorSl getAnchorSlByName(String name,int areaId) {
		AnchorSlExample example = new AnchorSlExample();
		org.akxy.zhky.manage.pojo.AnchorSlExample.Criteria criteria = example.createCriteria();
		criteria.andNameEqualTo(name);
		criteria.andAreaIdEqualTo(areaId);
		return anchorSlMapper.selectByExample(example).get(0);
	}

	@Override
	public AnchorMp getAnchorMpByName(String name,int areaId) {
		return anchorMpMapper.selectMpByName(name,areaId);
	}

	@Override
	public boolean deleteAnchorSiBySchemeId(int schemeId) {
		AnchorSiExample example = new AnchorSiExample();
		org.akxy.zhky.manage.pojo.AnchorSiExample.Criteria criteria = example.createCriteria();
		criteria.andSchemeIdEqualTo(schemeId);
		anchorSiMapper.deleteByExample(example);
		return anchorSiMapper.deleteByExample(example) > 0 ?true:false;
	}

	@Override
	public boolean updateAnchorSl(AnchorSl anchorSl) {
		return anchorSlMapper.updateByPrimaryKey(anchorSl) > 0 ?true:false;
	}

	@Override
	public List<Integer> getAllSchemeBySect(String stime, String etime, int areaId, int upTunnelId,int downTunnelId) {
		return anchorSlMapper.getAllSchemeBySect(stime, etime,areaId,upTunnelId,downTunnelId);
	}

	@Override
	public List<AnchorMpinfo> getOldestMeasurePointsByAreaId(int areaId,int upTunnelId,int downTunnelId) {
		AnchorSl scheme = anchorSlMapper.getLastScheme(areaId);
		List<AnchorMpinfo> mps = new ArrayList<>();
		if(upTunnelId==0&&downTunnelId==0){
			mps = anchorMpinfoMapper.getAnchorMpinfoBySchemeId(scheme.getId());
		}else{
			mps = anchorMpinfoMapper.getAnchorMpinfoBySchemeIdAndTunnel(scheme.getId(),upTunnelId,downTunnelId);
		}
		return mps;
	}

	@Override
	public int deleteScheme(int schemeId) {
		/*删除_sl表*/
		int slFlag = anchorSlMapper.deleteByPrimaryKey(schemeId);
		/*删除_si表*/
		AnchorSiExample example = new AnchorSiExample();
		org.akxy.zhky.manage.pojo.AnchorSiExample.Criteria criteria = example.createCriteria();
		criteria.andSchemeIdEqualTo(schemeId);
		int siFlag = anchorSiMapper.deleteByExample(example);
		if(slFlag > 0 || siFlag > 0){
			return 0;
		}else{
			return -1;
		}
	}

	@Override
	public String getMpStartTime(int areaId, int mpId) {
		String time = anchorSlMapper.getMpStartTime(areaId, mpId);
		return time;
	}
}
