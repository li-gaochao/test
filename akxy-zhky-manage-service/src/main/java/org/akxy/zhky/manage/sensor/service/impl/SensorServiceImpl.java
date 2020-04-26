package org.akxy.zhky.manage.sensor.service.impl;

import java.util.List;

import org.akxy.zhky.manage.dao.mapper.SensorMapper;
import org.akxy.zhky.manage.pojo.Sensor;
import org.akxy.zhky.manage.pojo.SensorExample;
import org.akxy.zhky.manage.pojo.SensorExample.Criteria;
import org.akxy.zhky.manage.sensor.service.ISensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: SensorServiceImpl.java
 * @Description:
 * @date: 2019年1月15日
 */
@Service
public class SensorServiceImpl implements ISensorService{
	@Autowired
	private SensorMapper sensorMapper;
	@Override
	public List<Sensor> getSensorByType(String type) {
		SensorExample example = new SensorExample();
		Criteria criteria = example.createCriteria();
		criteria.andTypeNameEqualTo(type);
		return sensorMapper.selectByExample(example);
	}

	@Override
	public Sensor getSensorById(int id) {
		return sensorMapper.selectByPrimaryKey(id);
	}

	@Override
	public Sensor getSensorByName(String name) {
		SensorExample example = new SensorExample();
		Criteria criteria = example.createCriteria();
		criteria.andNameEqualTo(name);
		List<Sensor> sensors = sensorMapper.selectByExample(example);
		if(sensors!=null && sensors.size()>0){
			return sensors.get(0);
		}else{
			return null;
		}
	}

}
