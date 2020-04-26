package org.akxy.zhky.manage.indextable.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.akxy.zhky.manage.config.service.IConfigService;
import org.akxy.zhky.manage.dao.mapper.IndexTableMapper;
import org.akxy.zhky.manage.indextable.service.IBaseTableService;
import org.akxy.zhky.manage.pojo.Config;
import org.akxy.zhky.manage.pojo.IndexTable;
import org.akxy.zhky.manage.pojo.IndexTableExample;
import org.akxy.zhky.manage.pojo.IndexTableExample.Criteria;
import org.akxy.zhky.manage.view.service.IViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: BaseTableServiceImpl.java
 * @Description:
 * @date: 2018年6月11日
 */
@Service
public class BaseTableServiceImpl implements IBaseTableService{
	@Autowired
	private IViewService iViewServie;
	@Autowired
	private IConfigService iConfigService;
	@Autowired
	private IndexTableMapper indexTableMapper;
	@Override
	public List<IndexTable> getAllIndexTable() {
		return indexTableMapper.selectAll();
	}
	@Override
	public List<IndexTable> getIndexTableByAreaIdAndType(int areaId, int type) {
		return indexTableMapper.getIndexTableByAreaAndType(areaId, type);
	}
	@Override
	public List<IndexTable> getIndexTable(int areaId) {
		//获取区域的子系统
		IndexTableExample example = new IndexTableExample();
		Criteria criteria = example.createCriteria();
		criteria.andAreaIdEqualTo(areaId);

		return indexTableMapper.selectByExample(example);
	}
	@Override
	public Map<String, Object> getBorderByAreaAndTypeAndTunnel(int areaId, int type, int tunnelId) {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("areaId", areaId);
		param.put("type", type);
		param.put("tunnelId", tunnelId);

		return indexTableMapper.getBorderByAreaAndTypeAndTunnel(param);
	}
	@Override
	public Map<String, Object> getIndexMpInfo(int areaId,int type) {
		Map<String,Object> res = new HashMap<String,Object>();
		Map<String,Object> info = iViewServie.getIndexAbnormityByType(areaId,type);
		StringBuilder strInfo = new StringBuilder();

		if(info==null){
			res.put("info", strInfo);

			res.put("table", null);
			return res;
		}


		int warnType = 1;
		Config config = iConfigService.findConfigWithType("index", "warntype", 0);
		if(config==null){
			warnType = 1;
		}else if(config.getIntValue().equals(1)){
			warnType = 1;
		}else if(config.getIntValue().equals(2)){
			warnType = 2;
		}

		List<Map<String,Object>> table = new ArrayList<Map<String,Object>>();

		switch (type) {
		case 1:
			strInfo.append("应力在线系统");
			table = indexTableMapper.getAllStressWarnMp(areaId);
			break;
		case 2:
			strInfo.append("综采支架系统");
			table = indexTableMapper.getAllCollSupportWarnMp(areaId);
			break;
		case 3:
			strInfo.append("单体支柱系统");
			table = indexTableMapper.getAllSingleSupportWarnMp(areaId);
			break;
		case 4:
			strInfo.append("锚杆索系统");
			table = indexTableMapper.getAllAnchorWarnMp(areaId);
			break;
		case 5:
			strInfo.append("顶板离层系统");
			table = indexTableMapper.getAllAbscissLayerWarnMp(areaId);
			break;
		case 6:
			strInfo.append("巷道变形系统");
			table = indexTableMapper.getAllDeformationWarnMp(areaId);
			break;
		default:
			break;
		}

		strInfo.append("共有测点：").append(info.get("total_number").toString()).append("个，其中");
		strInfo.append("正常测点：").append(info.get("green_number").toString()).append("个；");
		switch (type) {
		case 1:
			if(warnType==1){
				strInfo.append("亚预警测点：").append(info.get("blue_number").toString()).append("个；");
			}else if(warnType==2){
				strInfo.append("欠压预警测点：").append(info.get("blue_number").toString()).append("个；");
			}
			break;
		case 2:
			strInfo.append("蓝色预警测点：").append(info.get("blue_number").toString()).append("个；");
			break;
		default:
			break;
		}
		strInfo.append("黄色预警测点：").append(info.get("yellow_number").toString()).append("个；");
		strInfo.append("红色预警测点：").append(info.get("red_number").toString()).append("个；");

		res.put("info", strInfo);

		res.put("table", table);

		return res;
	}

}
