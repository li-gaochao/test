package org.akxy.zhky.manage.log.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.akxy.zhky.manage.dao.mapper.LogMapper;
import org.akxy.zhky.manage.log.service.ILogService;
import org.akxy.zhky.manage.pojo.Log;
import org.akxy.zhky.manage.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogServiceImpl implements ILogService{
	@Autowired
	private LogMapper logMapper;
	@Override
	public void addLog(User user, String type, String operation, String memo) throws ParseException {
		if(user == null){
			user = new User();
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();

		Log dyLog = new Log();
		dyLog.setCreateUser(user.getName());
		dyLog.setType(type);
		if(operation.length()>500){
			operation.substring(0,operation.length()-1);
		}
		dyLog.setContent(operation);
		dyLog.setCreateTime(sdf.parse(sdf.format(calendar.getTime())));
		logMapper.insert(dyLog);
	}
	@Override
	public void deleteLog() {

	}

}
