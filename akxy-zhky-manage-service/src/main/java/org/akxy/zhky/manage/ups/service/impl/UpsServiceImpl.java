package org.akxy.zhky.manage.ups.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.akxy.zhky.manage.config.service.IConfigService;
import org.akxy.zhky.manage.dao.mapper.UpsMpMapper;
import org.akxy.zhky.manage.dao.mapper.UpsTopDataMapper;
import org.akxy.zhky.manage.pojo.Config;
import org.akxy.zhky.manage.pojo.UpsMp;
import org.akxy.zhky.manage.pojo.UpsMpExample;
import org.akxy.zhky.manage.pojo.UpsTopDataExample;
import org.akxy.zhky.manage.pojo.UpsTopDataExample.Criteria;
import org.akxy.zhky.manage.ups.service.IUpsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @date: 2020年3月25日
 */
@Service
public class UpsServiceImpl implements IUpsService{
	@Autowired
	private UpsMpMapper upsMpMapper;
	@Autowired
	private IConfigService iconfigService;
	@Autowired
	private UpsTopDataMapper upsTopDataMapper;

	@Override
	public List<UpsMp> getAllUpsMp() {
		UpsMpExample example = new UpsMpExample();
		org.akxy.zhky.manage.pojo.UpsMpExample.Criteria criteria = example.createCriteria();
		criteria.andIdGreaterThan(0);
		return upsMpMapper.selectByExample(example);
	}

	@Override
	public void addOrUpdateUps(UpsMp upsMp) {
		if(upsMp.getId() == null) {
			upsMpMapper.insert(upsMp);
		}else {
			upsMpMapper.updateByPrimaryKey(upsMp);
		}
	}

	@Override
	public void deleteUps(int upsId) {
		upsMpMapper.deleteByPrimaryKey(upsId);
	}

	@Override
	public boolean isExistWarn() {
		UpsTopDataExample dataExample = new UpsTopDataExample();
		Criteria criteria = dataExample.createCriteria();
		List<Integer> warnNumList = new ArrayList<>();
		Config config = iconfigService.findConfigWithType("ups", "warntype", 0);
		String warnStr = config!=null?config.getStrValue():"2#4#7";
		String[] warnArray = warnStr.split("#");
		for(String s:warnArray) {
			warnNumList.add(Integer.valueOf(s));
		}
		criteria.andBatteryStatusIn(warnNumList);
		int count = upsTopDataMapper.countByExample(dataExample);
		return count>0?true:false;
	}

	@Override
	public List<LinkedHashMap<String,Object>> getUpsTopData() {
		return upsTopDataMapper.getAllTopData();
	}

	@Override
	public boolean isExistSameDeviceId(Integer upsId, Integer areaId, Integer deviceId) {
		UpsMpExample example = new UpsMpExample();
		org.akxy.zhky.manage.pojo.UpsMpExample.Criteria criteria = example.createCriteria();
		criteria.andAreaIdEqualTo(areaId);
		criteria.andDeviceIdEqualTo(deviceId);
		if(upsId!=null) {
			criteria.andIdNotEqualTo(upsId);
		}
		int count = upsMpMapper.countByExample(example);
		return count>0?true:false;
	}
}
