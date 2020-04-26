package org.akxy.zhky.manage.sensor.service;

import java.util.List;

import org.akxy.zhky.manage.pojo.Sensor;

/**
 * @ClassName: ISensorService.java
 * @Description:
 * @date: 2019年1月15日
 */
public interface ISensorService {
	public List<Sensor> getSensorByType(String type);
	public Sensor getSensorById(int id);
	public Sensor getSensorByName(String name);
}
