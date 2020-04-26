package org.akxy.zhky.manage.battery.service;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: IBatteryService.java
 * @Description: 电池信息管理接口
 * @date: 2018年9月21日
 */
public interface IBatteryService {
	public List<Map<String,Object>> getBatteryData(int type);
	public boolean isBatteryWarn(int type, int mpId, String etime);
}
