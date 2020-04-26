package org.akxy.zhky.manage.area.service.util;

import java.math.BigDecimal;

import org.akxy.zhky.manage.pojo.Vec3dPoint;

/**
 * @ClassName: TwoVec3dPointDistance.java
 * @Description: 计算二维两点之间的距离
 */
public class GetTwoPointDistance {
	public GetTwoPointDistance(){

	}
	public static double getDistance(Vec3dPoint p1,Vec3dPoint p2){
		double distance =  Math.sqrt((p2.getX()-p1.getX())*(p2.getX()-p1.getX())+(p2.getY()-p1.getY())*(p2.getY()-p1.getY()));
		return new BigDecimal(distance).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public static void main(String[] arg){
		Vec3dPoint p1 = new Vec3dPoint(40765, 9613, 0);
		Vec3dPoint p2 = new Vec3dPoint(40803.67291022947, 9623.217926114141, 0);
		System.out.println(getDistance(p1,p2));
	}

}
