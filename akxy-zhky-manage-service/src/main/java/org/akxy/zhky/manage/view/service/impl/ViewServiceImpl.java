package org.akxy.zhky.manage.view.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.akxy.zhky.manage.dao.mapper.ConfigMapper;
import org.akxy.zhky.manage.dao.mapper.ViewMapper;
import org.akxy.zhky.manage.pojo.Config;
import org.akxy.zhky.manage.view.service.IViewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: ViewServiceImpl.java
 * @Description:
 * @date: 2018年6月13日
 */
@Service
public class ViewServiceImpl implements IViewService{
	@Autowired
	private ViewMapper viewMapper;
	@Autowired
	private ConfigMapper configMapper;
	@Override
	public List<Map<String, Object>> getIndexAbnormity() {
		return viewMapper.getIndexAbnormity();
	}

	@Override
	public Map<String, Object> getIndexAbnormityByType(int areaId,int type) {
		return viewMapper.getIndexAbnormityByType(areaId,type);
	}

	@Override
	public List<Map<String, Object>> getIndexStressInfo() {

		List<Integer> allArea = viewMapper.getAllArea();
		List<Integer> allSubSystem = viewMapper.getAllSubSystem();

		Config c = configMapper.findConfigWithType("warn", "adjoindistance");
		Float warnDistance = c==null?(float)20:Float.parseFloat(c.getStrValue());

		List<Map<String, Object>> stressInfo = viewMapper.getIndexStressInfo();
		List<LinkedHashMap<String,Object>> adjoin = viewMapper.getIndexStressAdjoin();
		List<Map<String, Object>> res = new ArrayList<Map<String,Object>>();
		for(Integer areaId:allArea){
			Map<String,Object> temp = new HashMap<String,Object>();
			temp.put("area_id", areaId);

			List<Map<String, Object>> subSystem = new ArrayList<Map<String,Object>>();
			for(Integer subS:allSubSystem){
				Map<String,Object> subTemp = new HashMap<String,Object>();
				subTemp.put("type", subS);
				List<String> info = new ArrayList<String>();

				for(Map<String,Object> map:stressInfo){
					if(Integer.parseInt(map.get("area_id").toString()) == areaId.intValue() && Integer.parseInt(map.get("type").toString())==subS.intValue()){
						if(map.get("value_warn")!=null&&!map.get("value_warn").equals("")){
							info.add(map.get("value_warn").toString());
						}
						if(map.get("all_increment")!=null&&!map.get("all_increment").equals("")){
							info.add(map.get("all_increment").toString());
						}
						if(map.get("hour_increment")!=null&&!map.get("hour_increment").equals("")){
							info.add(map.get("hour_increment").toString());
						}
						if(map.get("day_increment")!=null&&!map.get("day_increment").equals("")){
							info.add(map.get("day_increment").toString());
						}
					}
				}
				if(subS.equals(1)){
					for(int i = 0 ; i < adjoin.size() ; i++){

						if(adjoin.get(i).get("area_id").equals(areaId)){
							if(Integer.parseInt(adjoin.get(i).get("i_zong").toString()) > 0){
								if(	i+1<adjoin.size()
										&& adjoin.get(i+1).get("area_id").equals(areaId)
										&& adjoin.get(i+1).get("location").equals(adjoin.get(i).get("location"))
										&& Float.parseFloat(adjoin.get(i+1).get("distance").toString()) - Float.parseFloat(adjoin.get(i).get("distance").toString()) <= warnDistance
										&& Integer.parseInt(adjoin.get(i+1).get("i_zong").toString()) > 0
										)
								{
									info.add("相邻");
									break;
								}else if(i+2<adjoin.size()
										&& adjoin.get(i+2).get("area_id").equals(areaId)
										&& adjoin.get(i+2).get("location").equals(adjoin.get(i).get("location"))
										&& Float.parseFloat(adjoin.get(i+2).get("distance").toString()) - Float.parseFloat(adjoin.get(i).get("distance").toString()) <= warnDistance
										&& Integer.parseInt(adjoin.get(i+2).get("i_zong").toString()) > 0
										)
								{
									info.add("相邻");
									break;
								}else if(i-1>=0
										&& i-1<adjoin.size()
										&& adjoin.get(i-1).get("area_id").equals(areaId)
										&& adjoin.get(i-1).get("location").equals(adjoin.get(i).get("location"))
										&& Float.parseFloat(adjoin.get(i).get("distance").toString()) - Float.parseFloat(adjoin.get(i-1).get("distance").toString()) <= warnDistance
										&& Integer.parseInt(adjoin.get(i-1).get("i_zong").toString()) > 0
										)
								{
									info.add("相邻");
									break;
								}else if(i-2>=0
										&& i-2<adjoin.size()
										&& adjoin.get(i-2).get("area_id").equals(areaId)
										&& adjoin.get(i-2).get("location").equals(adjoin.get(i).get("location"))
										&& Float.parseFloat(adjoin.get(i).get("distance").toString()) - Float.parseFloat(adjoin.get(i-2).get("distance").toString()) <= warnDistance
										&& Integer.parseInt(adjoin.get(i-2).get("i_zong").toString()) > 0
										)
								{
									info.add("相邻");
									break;
								}
							}
						}
					}
				}
				subTemp.put("info", info);
				subSystem.add(subTemp);
			}

			temp.put("info", subSystem);
			res.add(temp);
		}
		return res;
	}
}
