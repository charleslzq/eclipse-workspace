package src.tool;

import java.awt.geom.Rectangle2D;

public class RectangleObject{
	private double XofLeftBottom;
	private double YofLeftBottom;
	private double width;
	private double height;
	
	public RectangleObject(double a, double b, double c, double d){
		XofLeftBottom = a;
		YofLeftBottom = b;
		width = c;
		height = d;
	}
	
	public void print(){
		System.out.print("Rectangle: ");
		System.out.print("X: " + XofLeftBottom +";");
		System.out.print("Y: " + YofLeftBottom+";");
		System.out.print("Width: " + width+";");
		System.out.print("Height: " + height+";");
		System.out.println();
	}
	
	public double getXofLeftUpper(){
		return XofLeftBottom;
	}
	
	public double getYofLeftUpper(){
		return YofLeftBottom + height;
	}
	
	public boolean isLine(double a){
		if(width < a)
			return true;
		if(height < a)
			return true;
		return false;
	}
	
	public double getWidth(){
		return width;
	}
	
	public double getHeight(){
		return height;
	}
	
	
	public boolean isSameArea(RectangleObject ro){
		double err = 0.01;
		if(!isSame(ro.getHeight(), height, err))
			return false;
		if(!isSame(ro.getWidth(), width, err))
			return false;
		if(!isSame(ro.getXofLeftUpper(), XofLeftBottom, err))
			return false;
		if(!isSame(ro.getYofLeftUpper(), YofLeftBottom + height, err))
			return false;
		return true;
		
	}
	
	public boolean isSameRow(RectangleObject ro){
		double err = 0.01;
		if( isSame(ro.getYofLeftUpper(), YofLeftBottom + height, err) )
			return true;
		return false;
	}
	
	public boolean isSameColumn(RectangleObject ro){
		double err = 0.01;
		if(isSame(ro.getXofLeftUpper(), XofLeftBottom, err))
			return true;
		return false;
	}
	
	public boolean isSame(double a, double b, double err){
		if(Math.abs(a-b) <= err )
			return true;
		return false;
	}
	
	public boolean nextColumnInTheSameRow(RectangleObject ro){
		double err = 1;
		if( isSame(ro.getYofLeftUpper(), YofLeftBottom + height, err) )
			if( isSame(ro.getXofLeftUpper(), XofLeftBottom + width, err))
				return true;
		return false;
	}
	
	public boolean nextRowInTheSameColumn(RectangleObject ro){
		double err = 1;
		if(isSame(ro.getXofLeftUpper(), XofLeftBottom, err))
			if( isSame(ro.getYofLeftUpper(), YofLeftBottom, err))
				return true;
		return false;
	}
	
	public boolean isInThisRectangle(double x, double y){
		if(x < this.XofLeftBottom)
			return false;
		if(x > this.XofLeftBottom + this.width)
			return false;
		if(y < this.YofLeftBottom)
			return false;
		if(y > this.YofLeftBottom + this.height)
			return false;
		return true;
	}
	
	public Rectangle2D getRectangle(){
		return new Rectangle2D.Double(XofLeftBottom, YofLeftBottom, width, height);
	}

}