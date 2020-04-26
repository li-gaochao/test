package org.akxy.zhky.manage.area.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.akxy.zhky.manage.area.service.IAreaService;
import org.akxy.zhky.manage.area.service.IWorkFaceProgressService;
import org.akxy.zhky.manage.battery.service.IBatteryService;
import org.akxy.zhky.manage.collsupport.service.ICollSupportMpInfoService;
import org.akxy.zhky.manage.config.service.IConfigService;
import org.akxy.zhky.manage.dao.mapper.AreaMapper;
import org.akxy.zhky.manage.dao.mapper.IndexTableMapper;
import org.akxy.zhky.manage.dao.mapper.ViewMapper;
import org.akxy.zhky.manage.pojo.Area;
import org.akxy.zhky.manage.pojo.AreaExample;
import org.akxy.zhky.manage.pojo.AreaExample.Criteria;
import org.akxy.zhky.manage.pojo.Config;
import org.akxy.zhky.manage.pojo.IndexTable;
import org.akxy.zhky.manage.pojo.Line;
import org.akxy.zhky.manage.sensor.service.ISensorService;
import org.akxy.zhky.manage.singlesupport.service.ISingleSupportMpInfoService;
import org.akxy.zhky.manage.tunnel.service.ITunnelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: AreaServiceImpl.java
 * @Description:
 * @date: 2018年6月12日
 */
@Service
public class AreaServiceImpl implements IAreaService{
	@Autowired
	private AreaMapper areaMapper;
	@Autowired
	private ViewMapper viewMapper;
	@Autowired
	private IConfigService configService;
	@Autowired
	private ISensorService isensorService;
	@Autowired
	private ITunnelService iTunnelService;
	@Autowired
	private IBatteryService iBatteryService;
	@Autowired
	private IndexTableMapper indexTableMapper;
	@Autowired
	private IWorkFaceProgressService iWorkFaceProgressService;
	@Autowired
	private ICollSupportMpInfoService iCollSupportMpInfoService;
	@Autowired
	private ISingleSupportMpInfoService iSingleSupportMpInfoService;

	@Override
	public List<Object> getAllArea() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//用于存储返回值的list
		List<Object> list = new ArrayList<>();
		//获取所有的区域信息
		List<Area> areaList = areaMapper.selectAllUsedArea();
		//设置工作面位置
		for(int i = 0 ; i < areaList.size() ; i++){
			Line line = iWorkFaceProgressService.getWorkFaceLine(areaList.get(i).getId(),false);
			areaList.get(i).setX1(line.getStartVec3dPoint()!=null?line.getStartVec3dPoint().getX():null);
			areaList.get(i).setY1(line.getStartVec3dPoint()!=null?line.getStartVec3dPoint().getY():null);
			areaList.get(i).setX2(line.getEndVec3dPoint()!=null?line.getEndVec3dPoint().getX():null);
			areaList.get(i).setY2(line.getEndVec3dPoint()!=null?line.getEndVec3dPoint().getY():null);
		}
		//获取所有区域的子系统的预警数信息
		List<Map<String, Object>> mpList= viewMapper.getIndexAbnormity();

		//重新构造一个各个工作面包括所有子系统的
		List<Map<String, Object>> newMpList = new ArrayList<>();
		for(Area area:areaList){
			String types = area.getMonitorType();
			Map<String,Object> newYu1 = new HashMap<>();
			newYu1.put("areaId", area.getId());
			newYu1.put("type", 1);
			//如果该工作面有该子系统，异常数就给0，没有的话就给null
			if(types.indexOf('1')>-1){
				newYu1.put("total_number", 0);
				newYu1.put("blue_number", 0);
				newYu1.put("green_number", 0);
				newYu1.put("yellow_number", 0);
				newYu1.put("red_number", 0);
				newYu1.put("level", "E");
				newYu1.put("maxValue", 0);
				newYu1.put("acquisition_time", "");
			}else{
				newYu1.put("total_number", null);
				newYu1.put("blue_number", null);
				newYu1.put("green_number", null);
				newYu1.put("yellow_number", null);
				newYu1.put("red_number", null);
				newYu1.put("level", null);
				newYu1.put("maxValue", null);
				newYu1.put("acquisition_time", null);
			}

			Map<String,Object> newYu2 = new HashMap<>();
			newYu2.put("areaId", area.getId());
			newYu2.put("type", 2);
			if(types.indexOf('2')>-1){
				newYu2.put("total_number", 0);
				newYu2.put("blue_number", 0);
				newYu2.put("green_number", 0);
				newYu2.put("yellow_number", 0);
				newYu2.put("red_number", 0);
				newYu2.put("level", "E");
				newYu2.put("maxValue", 0);
				newYu2.put("acquisition_time", "");
			}else{
				newYu2.put("total_number", null);
				newYu2.put("blue_number", null);
				newYu2.put("green_number", null);
				newYu2.put("yellow_number", null);
				newYu2.put("red_number", null);
				newYu2.put("level", null);
				newYu2.put("maxValue", null);
				newYu2.put("acquisition_time", null);
			}

			Map<String,Object> newYu3 = new HashMap<>();
			newYu3.put("areaId", area.getId());
			newYu3.put("type", 3);
			if(types.indexOf('3')>-1){
				newYu3.put("total_number", 0);
				newYu3.put("blue_number", 0);
				newYu3.put("green_number", 0);
				newYu3.put("yellow_number", 0);
				newYu3.put("red_number", 0);
				newYu3.put("level", "E");
				newYu3.put("maxValue", 0);
				newYu3.put("acquisition_time", "");
			}else{
				newYu3.put("total_number", null);
				newYu3.put("blue_number", null);
				newYu3.put("green_number", null);
				newYu3.put("yellow_number", null);
				newYu3.put("red_number", null);
				newYu3.put("level", null);
				newYu3.put("maxValue", null);
				newYu3.put("acquisition_time", null);
			}

			Map<String,Object> newYu4 = new HashMap<>();
			newYu4.put("areaId", area.getId());
			newYu4.put("type", 4);
			if(types.indexOf('4')>-1){
				newYu4.put("total_number", 0);
				newYu4.put("blue_number", 0);
				newYu4.put("green_number", 0);
				newYu4.put("yellow_number", 0);
				newYu4.put("red_number", 0);
				newYu4.put("level", "E");
				newYu4.put("maxValue", 0);
				newYu4.put("acquisition_time", "");
			}else{
				newYu4.put("total_number", null);
				newYu4.put("blue_number", null);
				newYu4.put("green_number", null);
				newYu4.put("yellow_number", null);
				newYu4.put("red_number", null);
				newYu4.put("level", null);
				newYu4.put("maxValue", null);
				newYu4.put("acquisition_time", null);
			}

			Map<String,Object> newYu5 = new HashMap<>();
			newYu5.put("areaId", area.getId());
			newYu5.put("type", 5);
			if(types.indexOf('5')>-1){
				newYu5.put("total_number", 0);
				newYu5.put("blue_number", 0);
				newYu5.put("green_number", 0);
				newYu5.put("yellow_number", 0);
				newYu5.put("red_number", 0);
				newYu5.put("level", "E");
				newYu5.put("maxValue", 0);
				newYu5.put("acquisition_time", "");
			}else{
				newYu5.put("total_number", null);
				newYu5.put("blue_number", null);
				newYu5.put("green_number", null);
				newYu5.put("yellow_number", null);
				newYu5.put("red_number", null);
				newYu5.put("level", null);
				newYu5.put("maxValue", null);
				newYu5.put("acquisition_time", null);
			}

			Map<String,Object> newYu6 = new HashMap<>();
			newYu6.put("areaId", area.getId());
			newYu6.put("type", 6);
			if(types.indexOf('6')>-1){
				newYu6.put("total_number", 0);
				newYu6.put("blue_number", 0);
				newYu6.put("green_number", 0);
				newYu6.put("yellow_number", 0);
				newYu6.put("red_number", 0);
				newYu6.put("level", "E");
				newYu6.put("maxValue", 0);
				newYu6.put("acquisition_time", "");
			}else{
				newYu6.put("total_number", null);
				newYu6.put("blue_number", null);
				newYu6.put("green_number", null);
				newYu6.put("yellow_number", null);
				newYu6.put("red_number", null);
				newYu6.put("level", null);
				newYu6.put("maxValue", null);
				newYu6.put("acquisition_time", null);
			}

			newMpList.add(newYu1);
			newMpList.add(newYu2);
			newMpList.add(newYu3);
			newMpList.add(newYu4);
			newMpList.add(newYu5);
			newMpList.add(newYu6);

		}
		//把有预警的赋值
		for(int i = 0 ;i < newMpList.size() ; i ++){

			for(Map<String,Object> map:mpList){

				if((newMpList.get(i).get("areaId").toString().equals(map.get("areaId").toString())) && (newMpList.get(i).get("type").toString().equals(map.get("type").toString()))){
					if(newMpList.get(i).get("total_number")!=null){
						newMpList.get(i).replace("total_number", map.get("total_number"));
					}
					if(newMpList.get(i).get("blue_number")!=null){
						newMpList.get(i).replace("blue_number", map.get("blue_number"));
					}
					if(newMpList.get(i).get("green_number")!=null){
						newMpList.get(i).replace("green_number", map.get("green_number"));
					}
					if(newMpList.get(i).get("yellow_number")!=null){
						newMpList.get(i).replace("yellow_number", map.get("yellow_number"));
					}
					if(newMpList.get(i).get("red_number")!=null){
						newMpList.get(i).replace("red_number", map.get("red_number"));
					}
					if(newMpList.get(i).get("level")!=null){
						newMpList.get(i).replace("level", map.get("level"));
					}
					if(newMpList.get(i).get("maxValue")!=null){
						newMpList.get(i).replace("maxValue", map.get("maxValue"));
					}
					if(newMpList.get(i).get("acquisition_time")!=null){
						newMpList.get(i).replace("acquisition_time", sdf.format(map.get("acquisition_time")));
					}
				}
			}
		}

		//获取首页所有区域的表信息
		List<IndexTable> indexTableList = indexTableMapper.selectAll();
		//获取电量的预警值
		Config batteryWarn = configService.findConfigWithType("warn", "battery", 0);
		float warnBattery = (float) (batteryWarn!=null?Float.valueOf(batteryWarn.getStrValue()):3.3);

		Set<String> maxWarnLevels = new HashSet<>();
		//根据区域id，组合每个区域下的子系统的预警数
		for(Area area:areaList){
			Map<String,Object> areaMp = new HashMap<>();
			areaMp.put("area", area);
			String maxLevel = viewMapper.getMaxLevelByArea(area.getId());
			if(maxLevel!=null&&maxLevel.equals("C")){
				if(viewMapper.isExistMaxLevelByArea(area.getId(), "D") > 0){
					maxLevel = "D";
				}
			}
			maxWarnLevels.add(maxLevel);
			areaMp.put("maxLevel", maxLevel);
			List<Map<String,Object>> abnormityList = new ArrayList<>();
			List<IndexTable> tableList = new ArrayList<>();

			for(Map<String,Object> map:newMpList){

				if(map.get("areaId")!=null&&(area.getId().intValue() == (int)map.get("areaId"))){
					abnormityList.add(map);
				}
			}


			areaMp.put("abnormity", abnormityList);

			for(IndexTable indexTable:indexTableList){
				if(indexTable.getAreaId()!=null&&(area.getId().equals(indexTable.getAreaId()))){
					//对indexTable的电量进行判断
					if(indexTable.getBattery()!=null && indexTable.getBattery() >= warnBattery){
						indexTable.setBattery((float) 1 );
					}else{
						indexTable.setBattery((float) 0 );
					}

					if(indexTable.getDeviceLevel().equals("2")) {
						//重设indexTable的  device_level  device_status
						boolean isBatteryWarn = iBatteryService.isBatteryWarn(indexTable.getType(), indexTable.getMpId(), sdf.format(indexTable.getAcquisitionTime()));
						if(!isBatteryWarn) {
							indexTable.setDeviceLevel("1");
							indexTable.setDeviceStatus("正常");
						}
					}

					//单体支柱，按照KN显示
					if(indexTable.getType().equals(3)){
						double diameter = iSingleSupportMpInfoService.getSingleSupportMpById(indexTable.getMpId()).getDiameter();
						indexTable.setValue(new BigDecimal((indexTable.getValue1().doubleValue() * 3.14 * diameter * diameter)/1000).setScale(2, BigDecimal.ROUND_HALF_UP));
					}
					if(indexTable.getType().equals(2)) {
						int sensorId = iCollSupportMpInfoService.getCollSupportMpById(indexTable.getMpId()).getSensorId();
						String sensorType = isensorService.getSensorById(sensorId).getTypeName();
						if(sensorType.equals("expan")){
							indexTable.setValue1(indexTable.getValue1().setScale(0,BigDecimal.ROUND_HALF_UP));
							indexTable.setValue2(indexTable.getValue2().setScale(0,BigDecimal.ROUND_HALF_UP));
							indexTable.setValue(indexTable.getValue().setScale(0,BigDecimal.ROUND_HALF_UP));
						}
						if(sensorType.equals("angle")) {
							if(indexTable.getValue().compareTo(new BigDecimal(-10000)) == 0) {
								indexTable.setValue1(new BigDecimal(0));
								indexTable.setValue2(new BigDecimal(0));
								indexTable.setValue(new BigDecimal(0));
							}else {
								indexTable.setValue1(indexTable.getValue1().setScale(1,BigDecimal.ROUND_HALF_UP));
								indexTable.setValue2(indexTable.getValue2().setScale(1,BigDecimal.ROUND_HALF_UP));
								indexTable.setValue(indexTable.getValue().setScale(1,BigDecimal.ROUND_HALF_UP));
							}

						}

					}
					if(indexTable.getType().equals(5) || indexTable.getType().equals(6)) {
						indexTable.setValue1(indexTable.getValue1().setScale(0,BigDecimal.ROUND_HALF_UP));
						indexTable.setValue2(indexTable.getValue2().setScale(0,BigDecimal.ROUND_HALF_UP));
						indexTable.setValue(indexTable.getValue().setScale(0,BigDecimal.ROUND_HALF_UP));
					}

					tableList.add(indexTable);
				}
			}

			//对tableList排序

			//1、按照level降序
			Comparator<IndexTable> levelDesc = Comparator.comparing(IndexTable::getLevel).reversed();

			//2、按照device_level降序
			Comparator<IndexTable> deviceLevelDesc = Comparator.comparing(IndexTable::getDeviceLevel).reversed();

			//3、按照distance升序
			Comparator<IndexTable> distanceAsc = Comparator.comparing(IndexTable::getDistance);

			Comparator<IndexTable> finalComparator = levelDesc.thenComparing(deviceLevelDesc).thenComparing(distanceAsc);

			tableList = tableList.stream().sorted(finalComparator).collect(Collectors.toList());

			areaMp.put("table", tableList);


			//是否合并
			/*上下巷云图是否合并*/
			Config combineGram = configService.findConfigWithType("stress", "combine", 0);
			int isCombineGram = combineGram==null?0:combineGram.getIntValue();

			boolean isUpAndDown = iTunnelService.isAreaUpAndDown(area.getId());

			if(isCombineGram==1&&isUpAndDown){
				areaMp.put("isCombineGram", true);
			}else{
				areaMp.put("isCombineGram", false);
			}

			tableList = null;

			list.add(areaMp);

			areaMp = null;

		}

		indexTableList = null;
		newMpList = null;

		Config warnFlag = configService.findConfigWithType("warn", "allmine", 0);
		if(maxWarnLevels.contains("A")||maxWarnLevels.contains("B")||maxWarnLevels.contains("D")){
			//报警标志设为1
			warnFlag.setIntValue(1);
			configService.updateConfigWithType(warnFlag);
		}else{
			//报警标志设为0
			warnFlag.setIntValue(0);
			configService.updateConfigWithType(warnFlag);
		}

		return list;
	}

	//	private String getDesc(int abnormity){
	//		Config config = configMapper.findConfigWithType("abnormity", "mp");
	//		String[] level = config.getStrValue().split("#");
	//		if (abnormity <= Integer.parseInt(level[0]))
	//			return "C";	//预警级别最低
	//		else if (abnormity <= Integer.parseInt(level[1]))
	//			return "B";
	//		else
	//			return "A";	//预警级别最高
	//	}

	@Override
	public List<Area> getSubSystemArea(int subSystemType) {
		String monitorType = "%" + subSystemType + "%" ;
		return areaMapper.selectSubSystemArea(monitorType);
	}

	@Override
	public Area getAreaById(int areaId) {
		return areaMapper.selectByPrimaryKey(areaId);
	}

	@Override
	public List<Area> getInitAllArea() {

		return areaMapper.selectAllArea();
	}

	@Override
	public Area getAreaByName(String areaName) {
		AreaExample example = new AreaExample();
		Criteria criteria = example.createCriteria();
		criteria.andAreaNameEqualTo(areaName);
		List<Area> list = areaMapper.selectByExample(example);
		return list.size() > 0?list.get(0):null;
	}

	@Override
	public void insertArea(Area area) {
		areaMapper.insert(area);
	}

	@Override
	public void updateArea(Area area) {
		areaMapper.updateByPrimaryKeySelective(area);
	}

	@Override
	public void updateAreaSelective(Area area) {
		areaMapper.updateByPrimaryKeySelective(area);
	}

	@Override
	public int deleteAreaByIdAresaId(int areaId) {
		return areaMapper.deleteByPrimaryKey(areaId);
	}

	@Override
	public List<Area> getAllUsedArea() {
		return areaMapper.selectAllUsedArea();
	}
}
