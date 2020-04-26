package org.akxy.zhky.manage.area.service.util;

import org.akxy.zhky.manage.pojo.Vec3dPoint;

/**
 * @Description: 求点
 * @date: 2019年3月22日
 */
public class GetPoint {
	/**
	 * @Title: getTunnelAreaPoint
	 * @Description: TODO
	 * @param: @param curPoint	开始点
	 * @param: @param nextPoint	结束点
	 * @param: @param L			距离开始点的距离
	 * @param: @return
	 * @return: Vec3dPoint
	 */
	public static Vec3dPoint getTunnelAreaPoint(Vec3dPoint curPoint,Vec3dPoint nextPoint,double L) {
		double x,y;
		if(curPoint.x == nextPoint.x){
			x = curPoint.x;
			if(curPoint.y > nextPoint.y){
				y = curPoint.y - L;
			}else{
				y = curPoint.y + L;
			}
		}else{
			// 第一步：求得直线方程相关参数y=kx+b
			double k = (curPoint.y - nextPoint.y) * 1.0
					/ (curPoint.x - nextPoint.x);// 坐标直线斜率k
			double b = curPoint.y - k * curPoint.x;// 坐标直线b

			// 第二步：求得在直线y=kx+b上，距离当前坐标距离为L的某点
			// 一元二次方程Ax^2+Bx+C=0中,
			// 一元二次方程求根公式：
			// 两根x1,x2= [-B±√(B^2-4AC)]/2A
			// ①(y-y0)^2+(x-x0)^2=L^2;
			// ②y=kx+b;
			// 式中x,y即为根据以上lenthUnit单位长度(这里就是距离L)对应点的坐标
			// 由①②表达式得到:(k^2+1)x^2+2[(b-y0)k-x0]x+[(b-y0)^2+x0^2-L^2]=0
			double A = Math.pow(k, 2) + 1;// A=k^2+1;
			double B = 2 * ((b - curPoint.y) * k - curPoint.x);// B=2[(b-y0)k-x0];

			// C=(b-y0)^2+x0^2-L^2
			double C = Math.pow(b - curPoint.y, 2) + Math.pow(curPoint.x, 2)
			- Math.pow(L, 2);
			// 两根x1,x2= [-B±√(B^2-4AC)]/2A
			double x1 = (-B + Math.sqrt(Math.pow(B, 2) - 4 * A * C)) / (2 * A);
			double x2 = (-B - Math.sqrt(Math.pow(B, 2) - 4 * A * C)) / (2 * A);
			x = 0;// 最后确定是在已知两点之间的某点
			if (x1 == x2) {
				x = x1;
			} else if (curPoint.x <= x1 && x1 <= nextPoint.x || nextPoint.x <= x1
					&& x1 <= curPoint.x) {
				x = x1;
			} else if (curPoint.x <= x2 && x2 <= nextPoint.x || nextPoint.x <= x2
					&& x2 <= curPoint.x) {
				x = x2;
			}
			y = k * x + b;
		}
		Vec3dPoint mPoint = new Vec3dPoint( x, y, 0);
		return mPoint;
	}

	public static void main(String[] arg){
		Vec3dPoint v1 = new Vec3dPoint(1.9382177E7,4321817,0);
		Vec3dPoint v2 = new Vec3dPoint(1.9382177E7,4322406,0);
		double distance = 20;

		Vec3dPoint res = getTunnelAreaPoint(v1,v2,distance);
		System.out.println(res.getX());
		System.out.println(res.getY());
	}
}
