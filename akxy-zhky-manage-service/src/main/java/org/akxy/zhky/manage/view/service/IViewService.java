package org.akxy.zhky.manage.view.service;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: IViewService.java
 * @Description: 视图操作类
 * @date: 2018年6月13日
 */
public interface IViewService {
	List<Map<String,Object>> getIndexAbnormity();
	Map<String,Object> getIndexAbnormityByType(int areaId,int type);
	List<Map<String, Object>> getIndexStressInfo();
}
