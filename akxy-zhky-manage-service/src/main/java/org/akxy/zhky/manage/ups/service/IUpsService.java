package org.akxy.zhky.manage.ups.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.akxy.zhky.manage.pojo.UpsMp;

/**
 * @Description:
 * @date: 2020年3月25日
 */
public interface IUpsService {
	public List<UpsMp> getAllUpsMp();
	public void addOrUpdateUps(UpsMp upsMp);
	public void deleteUps(int upsId);
	public boolean isExistWarn();
	public List<LinkedHashMap<String,Object>> getUpsTopData();
	public boolean isExistSameDeviceId(Integer upsId, Integer areaId,Integer deviceId);
}
