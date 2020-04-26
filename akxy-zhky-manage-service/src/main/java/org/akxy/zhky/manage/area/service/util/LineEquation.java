package org.akxy.zhky.manage.area.service.util;

import org.akxy.zhky.manage.pojo.Vec3dPoint;

/**
 * @ClassName: PointInterPlane.java
 * @Description:获取直线方程
 */
public class LineEquation{
	public double a;
	public double b;
	public double c;
	public LineEquation(){

	}
	public LineEquation(Vec3dPoint vec3dPoint1, Vec3dPoint vec3dPoint2){
		//直线的一般式方程AX+BY+C=0
		//A = Y2 - Y1
		//B = X1 - X2
		//C = X2*Y1 - X1*Y2
		double a = vec3dPoint2.getY() - vec3dPoint1.getY();
		double b = vec3dPoint1.getX() - vec3dPoint2.getX();
		double c = vec3dPoint2.getX()*vec3dPoint1.getY() - vec3dPoint1.getX()*vec3dPoint2.getY();

		this.a = a;
		this.b = b;
		this.c = c;
	}
	public double getA() {
		return a;
	}
	public void setA(double a) {
		this.a = a;
	}
	public double getB() {
		return b;
	}
	public void setB(double b) {
		this.b = b;
	}
	public double getC() {
		return c;
	}
	public void setC(double c) {
		this.c = c;
	}


}
