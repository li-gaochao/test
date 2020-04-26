package org.akxy.zhky.manage.battery.service.impl;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.akxy.zhky.manage.battery.service.IBatteryService;
import org.akxy.zhky.manage.dao.mapper.TopDataInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: IBatteryServiceImpl.java
 * @Description:
 * @date: 2018年9月21日
 */
@Service
public class IBatteryServiceImpl implements IBatteryService{
	@Autowired
	private TopDataInfoMapper topDataInfoMapper;

	@Override
	public List<Map<String, Object>> getBatteryData(int type) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Map<String,Object>> res = topDataInfoMapper.selectBatteryData(type);
		for(int i = 0 ; i < res.size() ; i ++){
			res.get(i).replace("acquisitionTime", sdf.format(res.get(i).get("acquisitionTime")));
		}
		return res;
	}

	@Override
	public boolean isBatteryWarn(int type, int mpId, String etime) {
		String tableName = "stress_data";
		switch(type) {
		case 1:
			tableName = "stress_data";
			break;
		case 2:
			tableName = "coll_support_data";
			break;
		case 3:
			tableName = "single_support_data";
			break;
		case 4:
			tableName = "anchor_data";
			break;
		case 5:
			tableName = "absciss_layer_data";
			break;
		case 6:
			tableName = "deformation_data";
			break;
		default:
			tableName = "stress_data";
			break;
		}

		Integer count = topDataInfoMapper.getDataBatteryCount(tableName, mpId, etime);
		if(count >= 0) {
			//没预警
			return false;
		}else {
			//预警了
			return true;
		}

	}

}
