package org.akxy.zhky.manage.config.service;

import java.text.ParseException;
import java.util.List;

import org.akxy.zhky.manage.pojo.Config;

/**
 * @ClassName: IConfigService.java
 * @Description:
 * @date: 2018年6月4日
 */
public interface IConfigService {
	public Config findConfigWithType(String type,String name,int sequence);

	public List<Config> getInitConfig();

	public void updateConfigWithType(Config config);

	public void insertConfig(Config config);

	public void insertOrUpdate(Config config);

	public int insertConfigs(List<Config> configs);

	/**
	 * @Title: getReportSectByTime
	 * @Description: 根据报表选择的日期（yyyy-MM-dd） ，查询report参数的起始时间和differ值，获取真正的起止时间
	 * @param: @param time
	 * @param: @return
	 * @return: String[]
	 * @throws ParseException
	 */
	public String[] getReportSectByTime(String time) throws ParseException;
}
