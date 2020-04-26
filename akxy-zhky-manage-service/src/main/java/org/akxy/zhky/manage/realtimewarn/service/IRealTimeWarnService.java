package org.akxy.zhky.manage.realtimewarn.service;

import java.util.Date;
import java.util.List;

import org.akxy.zhky.manage.pojo.RealtimeWarn;

/**
 * @Description:
 * @date: 2019年11月25日
 */
public interface IRealTimeWarnService {
	public RealtimeWarn getWarnByTypeMpIdNotCompleteNotDeal(int systemType,String warnType,int mpId,boolean complete);
	public void update(RealtimeWarn record);
	public void insert(RealtimeWarn record);
	public List<RealtimeWarn> getAll(String warnType,Boolean isDeal);
	public void statistics();
	public boolean isExist(int mpId, int systemType,String warnType,Date startTime);
	public boolean isExistWarn();
}
