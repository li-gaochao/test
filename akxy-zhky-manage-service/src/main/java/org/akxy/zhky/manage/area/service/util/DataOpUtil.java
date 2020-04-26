package org.akxy.zhky.manage.area.service.util;

import org.akxy.zhky.manage.pojo.Vec3dPoint;

/**
 * @Description:
 * @date: 2019年3月26日
 */
public class DataOpUtil {
	private static double getLenght3D(double xStart, double yStart, double xEnd, double yEnd) {
		double L = Math.sqrt((xStart - xEnd) * (xStart - xEnd) + (yStart - yEnd) * (yStart - yEnd));
		return L;
	}

	public static Vec3dPoint getXY(Vec3dPoint start, Vec3dPoint end, double H) {
		Vec3dPoint vec3dPoint = new Vec3dPoint();
		if (start!=null && end !=null)
		{
			double xStart = start.getX();
			double yStart = start.getY();

			double xEnd = end.getX();
			double yEnd = end.getY();

			double L = getLenght3D(xStart, yStart, xEnd, yEnd);
			double x = getX(xStart, xEnd, H, L);
			double y = getY(yStart, yEnd, H, L);

			vec3dPoint.setX(x);
			vec3dPoint.setY(y);
		}

		return vec3dPoint;
	}
	private static double getX(double xStart, double xEnd, double h, double l) {
		double x = xStart - h * (xStart - xEnd) / l;
		return x;
	}
	private static double getY(double yStart, double yEnd, double h, double l) {
		double y = yStart + h * (yEnd - yStart) / l;
		return y;
	}
}
