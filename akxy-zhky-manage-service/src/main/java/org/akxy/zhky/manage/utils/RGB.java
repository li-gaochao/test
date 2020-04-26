package org.akxy.zhky.manage.utils;

/**
 * @Description:
 * @date: 2019年7月25日
 */
public class RGB{
	public int r;
	public int g;
	public int b;
	public RGB(){

	}
	public RGB(int r,int g, int b){
		this.r = r;
		this.g = g;
		this.b = b;
	}
	public int getR() {
		return r;
	}
	public void setR(int r) {
		this.r = r;
	}
	public int getG() {
		return g;
	}
	public void setG(int g) {
		this.g = g;
	}
	public int getB() {
		return b;
	}
	public void setB(int b) {
		this.b = b;
	}
	@Override
	public String toString() {
		return "[" + r + "," + g + "," + b + "]";
	}
}