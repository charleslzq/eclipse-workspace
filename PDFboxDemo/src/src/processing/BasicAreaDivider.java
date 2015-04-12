package src.processing;

import java.util.ArrayList;
import java.util.List;

import src.interfaces.AreaDivider;
import src.tool.LineSegments;
import src.tool.PDFRectangle;

public class BasicAreaDivider implements AreaDivider {

	@Override
	public LineSegments project(List<PDFRectangle> areas, 
			double start, 
			double end,
			boolean v) {
		// TODO Auto-generated method stub
		if(v == true){
			if(start <= end)
				return null;
			LineSegments ls = new LineSegments(v, start, end);
			for(int i=0; i<areas.size(); i++){
				double y1 = areas.get(i).getY()+areas.get(i).getHeight();
				double y2 = areas.get(i).getY();
				double y = ls.getLast();
				if(y1 == start)
					ls.addPosition(y1);
				else{
					if(y1 < y){
						ls.addPosition(y1);
						ls.addPosition(y2);
					}
					else if(y1 >= y && y2 <= y)
						ls.replace(y2);
				}
			}
			return ls;
		}
		else{
			if(start >= end)
				return null;
			LineSegments ls = new LineSegments(v, start, end);
			for(int i=0; i<areas.size(); i++){
				double x1 = areas.get(i).getX();
				double x2 = areas.get(i).getX()+areas.get(i).getWidth();
				double x = ls.getLast();
				if(x1 == start)
					ls.addPosition(x1);
				else{
					if(x1 > x){
						ls.addPosition(x1);
						ls.addPosition(x2);
					}
					else if(x1 <= x && x2 >= x)
						ls.replace(x2);
				}
			}
			return ls;
		}
	}

	@Override
	public List<PDFRectangle> divideArea(PDFRectangle pr, double value,
			boolean v) {
		// TODO Auto-generated method stub
		if(v == true){
			double y1 = pr.getY()+pr.getHeight();
			double y2 = pr.getY();
			double x = pr.getX();
			double w = pr.getWidth();
			List<PDFRectangle> list = new ArrayList<PDFRectangle>();
			if(y1 > value && value > y2){
				PDFRectangle pr1 = new PDFRectangle(x,y2,w,value-y2);
				PDFRectangle pr2 = new PDFRectangle(x,value,w,y1-value);
				list.add(pr2);
				list.add(pr1);
				return list;
			}
			else{
				list.add(pr);
				return list;
			}
		}
		else{
			double x1 = pr.getX();
			double x2 = pr.getX()+pr.getWidth();
			double y = pr.getY();
			double h = pr.getHeight();
			List<PDFRectangle> list = new ArrayList<PDFRectangle>();
			if(x1 < value && value < x2){
				PDFRectangle pr1 = new PDFRectangle(x1,y,value-x1,h);
				PDFRectangle pr2 = new PDFRectangle(value,y,x2-value,h);
				list.add(pr1);
				list.add(pr2);
				return list;
			}
			else{
				list.add(pr);
				return list;
			}
		}
	}

	@Override
	public int countAreas(List<PDFRectangle> areas, PDFRectangle rect) {
		// TODO Auto-generated method stub
		int count = 0;
		for(int i=0; i<areas.size(); i++){
			if(rect.isInThisArea(areas.get(i)) == true)
				count++;
		}
		return count;
	}

}
