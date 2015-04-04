package src.tool;

import java.awt.geom.Rectangle2D;

public class PDFRectangle{
	private Rectangle2D rect;
	private static ApproximateCalculation ac = new ApproximateCalculation(2,1);
	private RectangleType rt;
	private static double threshold;
	
	public PDFRectangle(double a, double b, 
			double c, double d){
		rect = new Rectangle2D.Double(a, b, c, d);
		this.setType();
	}
	
	private void setType(){
		if( rect.getWidth() <= threshold 
				&& rect.getHeight() <= threshold)
			rt = RectangleType.PDF_POINT;
		else if(rect.getWidth() <= threshold 
				|| rect.getHeight() <= threshold)
			rt = RectangleType.PDF_LINE;
		else
			rt = RectangleType.PDF_CELL;
	}
	
	public RectangleType getType(){
		return this.rt;
	}

	public static void setPrecision(int precision){
		ac.setPrecision(precision);
	}
	
	public static void setErr(double err){
		ac.setErr(err);
	}
	
	
	
	public static double getThreshold() {
		return threshold;
	}

	public static void setThreshold(double threshold) {
		PDFRectangle.threshold = threshold;
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
	
	public double getMidX(){
		return rect.getX()+rect.getWidth()/2;
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
		if( ac.approximateMoreEqual(pr.getX(), this.getX())
				&& ac.approximateMoreEqual(pr.getY(), this.getY())
				&& ac.approximateLessEqual(pr.getX()+pr.getWidth(), this.getX()+this.getWidth())
				&& ac.approximateLessEqual(pr.getY()+pr.getHeight(), this.getY()+this.getHeight()))
			return true;
		return false;
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
		if(ac.approximateLess(Math.abs(this.getY()-pr.getY()-pr.getHeight()), 
				PDFRectangle.threshold))
			return true;
		else
			return false;
	}
	
	public boolean isSameHeight(double h){
		return ac.alreadyEqual(this.getHeight(), h);
	}
	
	public boolean isSameWidth(PDFRectangle pr){
		return ac.alreadyEqual(this.getWidth(), pr.getWidth());
	}
	
	public void print(){
		System.out.println("PDFRectangle:	X:"+this.getX()
				+ ";Y:"+this.getY()
				+ ";Width:"+this.getWidth()
				+ ";Height:"+this.getHeight()
				+ ";Type:"+this.getType()+"/"+this.getThreshold());
	}
	
	public boolean isLegal(){
		if(this.getX() < 0)
			return false;
		if(this.getY() < 0)
			return false;
		return true;
	}
	
	public boolean isHigher(PDFRectangle pr){
		if( ac.approximateMore(this.getY()+this.getHeight(), pr.getY()+pr.getHeight()))
			return true;
		return false;
	}
	
	public boolean isLefter(PDFRectangle pr){
		if( ac.approximateLess(this.getMidX(), pr.getMidX()))
			return true;
		return false;
	}
	
	public boolean isVerticalLine(){
		if(this.rt == RectangleType.PDF_LINE)
			if(this.getHeight() > this.getWidth())
				return true;
		return false;
	}
	
	public boolean isSameX(double x){
		return ac.alreadyEqual(this.getX(), x);
	}
	
	public boolean isSameY(double y){
		return ac.alreadyEqual(this.getY()+this.getHeight(), y);
	}
	
	public boolean isRighter(PDFRectangle pr){
		return ac.approximateMore(this.getMidX(), pr.getMidX());
	}
	
	public boolean isHigherLefter(PDFRectangle pr){
		if(this.isHigher(pr))
			return true;
		else if(pr.isHigher(this))
			return false;
		else{
			if(this.isLefter(pr))
				return true;
			return false;
		}
	}
}
