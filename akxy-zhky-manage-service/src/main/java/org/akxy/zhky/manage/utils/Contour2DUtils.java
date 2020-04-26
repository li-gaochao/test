package org.akxy.zhky.manage.utils;

import java.util.ArrayList;
import java.util.List;

import org.akxy.zhky.manage.utils.wContour.Contour2d;
import org.akxy.zhky.manage.utils.wContour.Global.PointD;
import org.akxy.zhky.manage.utils.wContour.Global.PolyLine;
import org.akxy.zhky.manage.utils.wContour.Global.Polygon;

import com.lcp.dxf.base.AciColor;
import com.lcp.dxf.base.Vector2f;
import com.lcp.dxf.entities.Polyline;
import com.lcp.dxf.entities.PolylineVertex;


public class Contour2DUtils {
	public static Contour2d get2DContour(List<Polygon> polylines){

		Contour2d c2d = new Contour2d();
		List<Polyline> borders = new ArrayList<>();
		List<Polyline> holes = new ArrayList<>();
		List<Double> values = new ArrayList<>();
		int handleDeg = 0;
		for (Polygon polygon : polylines) {
			List<PolyLine> holeLines = polygon.HoleLines;

			for (PolyLine polyLine : holeLines) {
				List<PolylineVertex> vertexes = new ArrayList<>();

				Polyline polylineTmp = new Polyline();
				polylineTmp.setCodeName("POLYLINE");
				polylineTmp.setHandle(Integer.toHexString(handleDeg+1));
				polylineTmp.setLineType(polylineTmp.getLineType().setName("ByLayer"));
				polylineTmp.setColor(new AciColor((short) 30));
				polylineTmp.setElevation((float) ((polygon.HighValue+polygon.LowValue)/2));//alpha-value
				polylineTmp.setThickness(1.5f);
				for (PointD pointD : polyLine.PointList) {
					PolylineVertex vertexe = new PolylineVertex();
					Vector2f location = new Vector2f(pointD.X, pointD.Y);
					vertexe.setLocation(location);
					vertexes.add(vertexe);
				}
				try {
					polylineTmp.setVertexes(vertexes);
					holes.add(polylineTmp);
					values.add(polyLine.Value);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			List<PolylineVertex> vertexes = new ArrayList<>();
			List<PointD> outLines = polygon.OutLine.PointList;
			Polyline polylineOutLine = new Polyline();
			polylineOutLine.setElevation((float) ((polygon.HighValue+polygon.LowValue)/2));//alpha-value
			for (PointD pointD : outLines) {
				PolylineVertex vertexe = new PolylineVertex();
				Vector2f location = new Vector2f(pointD.X, pointD.Y);
				vertexe.setLocation(location);
				vertexes.add(vertexe);
			}
			try {
				polylineOutLine.setVertexes(vertexes);
				borders.add(polylineOutLine);
			} catch (Exception e) {
				e.printStackTrace();
			}

			c2d.setBorders(borders);

			c2d.setPolyLines(holes);

			c2d.setValue(values);
		}
		//return JsonUtils.objectToJson(c2d);
		return c2d;
	}
}
