package org.akxy.zhky.manage.config.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.akxy.zhky.manage.config.service.IConfigService;
import org.akxy.zhky.manage.dao.mapper.ConfigMapper;
import org.akxy.zhky.manage.pojo.Config;
import org.akxy.zhky.manage.pojo.ConfigExample;
import org.akxy.zhky.manage.pojo.ConfigExample.Criteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: ConfigServiceImpl.java
 * @Description:
 * @date: 2018年6月4日
 */
@Service
public class ConfigServiceImpl implements IConfigService{
	@Autowired
	private ConfigMapper configMapper;
	@Override
	public Config findConfigWithType(String type, String name,int sequence) {
		ConfigExample configExample = new ConfigExample();
		Criteria criteria = configExample.createCriteria();
		criteria.andSequenceEqualTo(sequence);
		criteria.andTypeEqualTo(type);
		criteria.andNameEqualTo(name);
		List<Config> list = configMapper.selectByExample(configExample);
		return list.size()>0?list.get(0):null;
	}

	@Override
	public void updateConfigWithType(Config config) {
		String type = config.getType();
		String name = config.getName();
		int sequence = config.getSequence();
		int intValue = config.getIntValue()!=null?config.getIntValue():0;
		String strValue = config.getStrValue();
		configMapper.updateConfigWithType(type,name,sequence,intValue,strValue);
	}

	@Override
	public List<Config> getInitConfig() {
		List<Config> configs = new ArrayList<Config>();
		configs = configMapper.getAllConfigs();
		return configs;
	}

	@Override
	public void insertConfig(Config config) {
		configMapper.insert(config);
	}

	@Override
	public void insertOrUpdate(Config config) {
		//判断是否存在，如果存在就更新，不存在就插入
		ConfigExample configExample = new ConfigExample();
		Criteria criteria = configExample.createCriteria();
		criteria.andSequenceEqualTo(config.getSequence());
		criteria.andTypeEqualTo(config.getType());
		criteria.andNameEqualTo(config.getName());
		List<Config> list = configMapper.selectByExample(configExample);
		if(list!=null&&list.size()>0){
			configMapper.updateConfigWithType(config.getType(), config.getName(), config.getSequence(), config.getIntValue(), config.getStrValue());
		}else{
			configMapper.insert(config);
		}
	}

	@Override
	public int insertConfigs(List<Config> configs) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("list", configs);
		return configMapper.insertConfigs(map);
	}

	@Override
	public String[] getReportSectByTime(String time) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//起始时间，结束时间为其减1秒
		String report_end_str = this.findConfigWithType("report", "start", 0).getStrValue();
		//报表日的计算方式
		int report_differ = this.findConfigWithType("report", "differ", 0).getIntValue();

		String stime = "" ,etime = "";

		String tempTime = time + " " + report_end_str;

		//当天开始的
		if(report_differ == 0){
			stime = tempTime;

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sdf.parse(stime));
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			//calendar.add(Calendar.SECOND, -1);

			etime = sdf.format(calendar.getTime());

		}else if(report_differ == -1){//前一天
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sdf.parse(tempTime));
			//calendar.add(Calendar.SECOND, -1);

			etime = sdf.format(calendar.getTime());

			calendar.setTime(sdf.parse(tempTime));
			calendar.add(Calendar.DAY_OF_MONTH, -1);

			stime = sdf.format(calendar.getTime());

		}

		String[] times = new String[]{stime,etime};

		return times;
	}
}
