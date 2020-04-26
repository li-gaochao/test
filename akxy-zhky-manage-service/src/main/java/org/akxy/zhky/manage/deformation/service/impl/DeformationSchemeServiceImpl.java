package org.akxy.zhky.manage.deformation.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.akxy.zhky.manage.dao.mapper.DeformationMpMapper;
import org.akxy.zhky.manage.dao.mapper.DeformationMpinfoMapper;
import org.akxy.zhky.manage.dao.mapper.DeformationSiMapper;
import org.akxy.zhky.manage.dao.mapper.DeformationSlMapper;
import org.akxy.zhky.manage.deformation.service.IDeformationSchemeService;
import org.akxy.zhky.manage.pojo.DeformationMp;
import org.akxy.zhky.manage.pojo.DeformationMpExample;
import org.akxy.zhky.manage.pojo.DeformationMpExample.Criteria;
import org.akxy.zhky.manage.pojo.DeformationMpinfo;
import org.akxy.zhky.manage.pojo.DeformationSiExample;
import org.akxy.zhky.manage.pojo.DeformationSiKey;
import org.akxy.zhky.manage.pojo.DeformationSl;
import org.akxy.zhky.manage.pojo.DeformationSlExample;
import org.akxy.zhky.manage.pojo.SchemeDeformationMp;
import org.akxy.zhky.manage.pojo.Sensor;
import org.akxy.zhky.manage.pojo.Tunnel;
import org.akxy.zhky.manage.sensor.service.ISensorService;
import org.akxy.zhky.manage.tunnel.service.ITunnelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: IDeformationSchemeServiceImpl.java
 * @Description:
 * @date: 2018年8月27日
 */
@Service
public class DeformationSchemeServiceImpl implements IDeformationSchemeService{
	@Autowired
	private ITunnelService iTunnelService;
	@Autowired
	private ISensorService iSensorService;
	@Autowired
	private DeformationSlMapper deformationSlMapper;
	@Autowired
	private DeformationSiMapper deformationSiMapper;
	@Autowired
	private DeformationMpMapper deformationMpMapper;
	@Autowired
	private DeformationMpinfoMapper deformationMpinfoMapper;

	@Override
	public List<DeformationSl> getDeformationSl(int areaId) {
		DeformationSlExample example = new DeformationSlExample();
		org.akxy.zhky.manage.pojo.DeformationSlExample.Criteria criteria = example.createCriteria();
		criteria.andAreaIdEqualTo(areaId);
		return deformationSlMapper.selectByExample(example);
	}
	@Override
	public boolean addDeformationSl(DeformationSl deformationSl){
		return deformationSlMapper.insert(deformationSl) > 0 ?true:false;
	}

	@Override
	public List<DeformationMp> getDeformationMp(int schemeId) {
		//现在si表中找到方案id下的所有mpid
		DeformationSiExample siExample = new DeformationSiExample();
		org.akxy.zhky.manage.pojo.DeformationSiExample.Criteria siCriteria = siExample.createCriteria();
		siCriteria.andSchemeIdEqualTo(schemeId);
		List<DeformationSiKey> siList = deformationSiMapper.selectByExample(siExample);

		//构建方案中的测点id list
		List<Integer> mpIdList = new ArrayList<>();
		for(DeformationSiKey si:siList){
			mpIdList.add(si.getMeasurePointId());
		}
		DeformationMpExample mpExample = new DeformationMpExample();
		org.akxy.zhky.manage.pojo.DeformationMpExample.Criteria mpCriteria = mpExample.createCriteria();
		mpCriteria.andIdIn(mpIdList);

		return deformationMpMapper.selectByExample(mpExample);
	}

	@Override
	public List<DeformationSiKey> getDeformationSiKey(int schemeId) {
		DeformationSiExample example = new DeformationSiExample();
		org.akxy.zhky.manage.pojo.DeformationSiExample.Criteria criteria = example.createCriteria();
		criteria.andSchemeIdEqualTo(schemeId);
		return deformationSiMapper.selectByExample(example);
	}
	@Override
	public boolean addDeformationSiKey(DeformationSiKey deformationSiKey){

		return deformationSiMapper.insert(deformationSiKey) > 0 ?true:false;

	}

	@Override
	public Map<String,Object> getDeformationAllScheme(int areaId ,String stime ,String etime) throws ParseException {
		Map<String,Object> res = new HashMap<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		//获取所有的sensor类型
		List<Sensor> sensorList = iSensorService.getSensorByType("deformation");

		//找区域下的所有巷道
		List<Tunnel> tunnelList = iTunnelService.findTunnelByAreaId(areaId);

		Map<String,Object> staticData = new HashMap<>();

		staticData.put("tunnel", tunnelList);
		staticData.put("sensor", sensorList);

		//用于保存完整方案的ist
		List<Map<String,Object>> schemeList = new ArrayList<>();
		//先找到所有的方案
		List<DeformationSl> slList = new ArrayList<>();
		if(stime == null || stime.equals("")){
			slList = deformationSlMapper.getLastSchemes(areaId);
		}else{
			DeformationSlExample example = new DeformationSlExample();
			org.akxy.zhky.manage.pojo.DeformationSlExample.Criteria criteria = example.createCriteria();
			criteria.andAreaIdEqualTo(areaId);
			criteria.andCreateTimeBetween(sdf.parse(stime), sdf.parse(etime));
			example.setOrderByClause("create_time");
			slList =  deformationSlMapper.selectByExample(example);
		}

		//在找到所有的含方案id的所有测点
		List<SchemeDeformationMp> mpList = deformationMpMapper.selectSchemeMp(areaId);
		//根据方案和测点中的方案id，组装方案到map里，将组装好的多个方案添加到list中
		for(DeformationSl sl:slList){
			//List<Map<String,Object>> scheme = new ArrayList<Map<String,Object>>();
			Map<String,Object> schemeSl = new HashMap<>();

			List<SchemeDeformationMp> schemeMp = new ArrayList<>();
			for(SchemeDeformationMp mp:mpList){
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
	public void addOrUpdateMps(List<DeformationMp> deformationMps) {
		DeformationMp oldMp = null;
		for(DeformationMp deformationMp:deformationMps)
		{
			oldMp = deformationMpMapper.selectMpByName(deformationMp.getName(),deformationMp.getAreaId());
			//如果已存在该测点名称，就按照名称更新该测点
			if(oldMp!=null){
				DeformationMpExample example = new DeformationMpExample();
				Criteria criteria = example.createCriteria();
				criteria.andNameEqualTo(deformationMp.getName());
				criteria.andAreaIdEqualTo(deformationMp.getAreaId());
				if(!oldMp.toString().equals(deformationMp.toString())){
					deformationMp.setId(oldMp.getId());
					deformationMpMapper.updateByExample(deformationMp, example);
				}
			}else{
				//如果也不存在该测点名称，就新增
				deformationMp.setId(null);
				deformationMpMapper.insert(deformationMp);
			}
		}
	}

	@Override
	public DeformationSl getDeformationSlByName(String name,int areaId) {
		DeformationSlExample example = new DeformationSlExample();
		org.akxy.zhky.manage.pojo.DeformationSlExample.Criteria criteria = example.createCriteria();
		criteria.andNameEqualTo(name);
		criteria.andAreaIdEqualTo(areaId);
		return deformationSlMapper.selectByExample(example).get(0);
	}

	@Override
	public DeformationMp getDeformationMpByName(String name,int areaId) {
		return deformationMpMapper.selectMpByName(name,areaId);
	}

	@Override
	public boolean deleteDeformationSiBySchemeId(int schemeId) {
		DeformationSiExample example = new DeformationSiExample();
		org.akxy.zhky.manage.pojo.DeformationSiExample.Criteria criteria = example.createCriteria();
		criteria.andSchemeIdEqualTo(schemeId);
		deformationSiMapper.deleteByExample(example);
		return deformationSiMapper.deleteByExample(example) > 0 ?true:false;
	}

	@Override
	public boolean updateDeformationSl(DeformationSl deformationSl) {
		return deformationSlMapper.updateByPrimaryKey(deformationSl) > 0 ?true:false;
	}

	@Override
	public List<Integer> getAllSchemeBySect(String stime, String etime, int areaId,int upTunnelId,int downTunnelId) {
		return deformationSlMapper.getAllSchemeBySect(stime, etime,areaId,upTunnelId,downTunnelId);
	}

	@Override
	public List<DeformationMpinfo> getOldestMeasurePointsByAreaId(int areaId,int upTunnelId,int downTunnelId) {
		DeformationSl scheme = deformationSlMapper.getLastScheme(areaId);

		List<DeformationMpinfo> mps = new ArrayList<>();
		if(upTunnelId==0&&downTunnelId==0){
			mps = deformationMpinfoMapper.getDeformationMpinfoBySchemeId(scheme.getId());
		}else{
			mps = deformationMpinfoMapper.getDeformationMpinfoBySchemeIdAndTunnel(scheme.getId(),upTunnelId,downTunnelId);
		}
		return mps;
	}

	@Override
	public int deleteScheme(int schemeId) {
		/*删除_sl表*/
		int slFlag = deformationSlMapper.deleteByPrimaryKey(schemeId);
		/*删除_si表*/
		DeformationSiExample example = new DeformationSiExample();
		org.akxy.zhky.manage.pojo.DeformationSiExample.Criteria criteria = example.createCriteria();
		criteria.andSchemeIdEqualTo(schemeId);
		int siFlag = deformationSiMapper.deleteByExample(example);
		if(slFlag > 0 || siFlag > 0){
			return 0;
		}else{
			return -1;
		}
	}

	@Override
	public String getMpStartTime(int areaId, int mpId) {
		String time = deformationSlMapper.getMpStartTime(areaId, mpId);
		return time;
	}
}
