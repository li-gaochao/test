package org.akxy.zhky.manage.log.service;

import java.text.ParseException;

import org.akxy.zhky.manage.pojo.User;

/**
 * @ClassName: IDyLogService.java
 * @Description: 日志Service
 * @date: 2018年4月12日
 */
public interface ILogService {
	public void addLog(User user,String type,String operation,String memo) throws ParseException;
	public void deleteLog();
}
