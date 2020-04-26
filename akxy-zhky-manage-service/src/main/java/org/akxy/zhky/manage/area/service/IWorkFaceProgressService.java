package org.akxy.zhky.manage.area.service;

import org.akxy.zhky.manage.pojo.Line;

/**
 * @Description:
 * @date: 2019年3月22日
 */
public interface IWorkFaceProgressService {
	public Line getWorkFaceLine(int areaId,boolean isSupport);
}
