package src.tool;

import java.awt.geom.Rectangle2D;

public class PDFRectangle{
	private Rectangle2D rect;
	private static ApproximateCalculation ac = new ApproximateCalculation(2,1);
	
	public PDFRectangle(double a, double b, 
			double c, double d){
		rect = new Rectangle2D.Double(a, b, c, d);
	}

	public static void setPrecision(int precision){
		ac.setPrecision(precision);
	}
	
	public static void setErr(double err){
		ac.setErr(err);
	}
	
	public PDFRectangle union(PDFRectangle pr){
		if(this.isInThisArea(pr))
			return null;
		if(this.isNextCellInTheSameColumn(pr))
			return new PDFRectangle(pr.getX(),
					pr.getY(),
					pr.getWidth(),
					this.getHeight()+pr.getHeight());
		if(this.isNextCellInTheSameRow(pr))
			return new PDFRectangle(this.getX(),
					this.getY(),
					this.getHeight(),
					this.getWidth()+pr.getWidth());
		return null;
	}

	public double getHeight() {
		// TODO Auto-generated method stub
		return rect.getHeight();
	}

	public double getWidth() {
		// TODO Auto-generated method stub
		return rect.getWidth();
	}

	public double getX() {
		// TODO Auto-generated method stub
		return rect.getMinX();
	}

	public double getY() {
		// TODO Auto-generated method stub
		return rect.getMinY();
	}

	
	public boolean isInThisArea(double x, double y){
		return rect.contains(x, y);
	}
	
	public boolean isInThisArea(PDFRectangle pr){
		return rect.contains(pr.getX(), pr.getY(), pr.getWidth(), pr.getHeight());
	}
	
	public boolean isSameRectangle(PDFRectangle pr){
		if(ac.alreadyEqual(this.getX(), pr.getX()) == false)
			return false;
		if(ac.alreadyEqual(this.getY(), pr.getY()) == false)
			return false;
		if(ac.alreadyEqual(this.getWidth(), pr.getWidth()) == false)
			return false;
		if(ac.alreadyEqual(this.getHeight(), pr.getHeight()) == false)
			return false;
		return true;
	}
	
	public boolean isSameRow(PDFRectangle pr){
		return ac.alreadyEqual(this.getY()+this.getHeight(), 
				pr.getY()+pr.getHeight());
	}
	
	public boolean isSameColumn(PDFRectangle pr){
		return ac.alreadyEqual(this.getX(), pr.getX());
	}
	
	public boolean isNextCellInTheSameRow(PDFRectangle pr){
		if(this.isSameRectangle(pr) == true)
			return false;
		if(this.isSameRow(pr) == false)
			return false;
		if(ac.alreadyEqual(this.getX()+this.getWidth() , pr.getX()))
			return true;
		else
			return false;
	}
	
	public boolean isNextCellInTheSameColumn(PDFRectangle pr){
		if(this.isSameRectangle(pr) == true)
			return false;
		if(this.isSameColumn(pr) == false)
			return false;
		if(ac.alreadyEqual(this.getY(), pr.getY()+pr.getHeight()))
			return true;
		else
			return false;
	}
	
	public boolean isSameHeight(PDFRectangle pr){
		return ac.alreadyEqual(this.getHeight(), pr.getHeight());
	}
	
	public boolean isSameWidth(PDFRectangle pr){
		return ac.alreadyEqual(this.getWidth(), pr.getWidth());
	}
	
	public void print(){
		System.out.println("PDFRectangle:	X:"+this.getX()
				+ ";Y:"+this.getY()
				+ ";Width:"+this.getWidth()
				+ ";Height:"+this.getHeight());
	}
}
