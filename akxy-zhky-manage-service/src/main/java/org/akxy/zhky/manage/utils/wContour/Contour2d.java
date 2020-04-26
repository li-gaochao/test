package org.akxy.zhky.manage.utils.wContour;

import java.util.List;

import com.lcp.dxf.entities.Polyline;

public class Contour2d {
	List<Double> value;
	List<Polyline> borders;
	List<Polyline> polyLines;

	public List<Polyline> getPolyLines() {
		return polyLines;
	}
	public List<Double> getValue() {
		return value;
	}
	public List<Polyline> getBorders() {
		return borders;
	}
	public void setPolyLines(List<Polyline> polyLines) {
		this.polyLines = polyLines;
	}
	public void setValue(List<Double> value) {
		this.value = value;
	}
	public void setBorders(List<Polyline> borders) {
		this.borders = borders;
	}

}
