package src.tool;

import java.awt.geom.Rectangle2D;

public class PDFRectangle{
	private Rectangle2D rect;
	private static ApproximateCalculation ac = new ApproximateCalculation(0.1);
	private RectangleType rt;
	private static double threshold;
	private static double blank;
	
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
	
	public static double getBlank() {
		return blank;
	}

	public static void setBlank(double blank) {
		PDFRectangle.blank = blank;
	}

	public RectangleType getType(){
		return this.rt;
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
		if(ac.weakMore(x, this.getX())
				&& ac.weakMore(y, this.getY())
				&& ac.weakMore(this.getX()+this.getWidth(), x)
				&& ac.weakMore(this.getY()+this.getHeight(), y))
			return true;
		return false;
	}
	
	public boolean isInThisArea(PDFRectangle pr){
		return this.isInThisArea(pr.getX(), pr.getY()) 
				&& this.isInThisArea(pr.getX()+pr.getWidth(), pr.getY()+pr.getHeight());
		/*if( pr.getX() >= this.getX()
				&& pr.getY() >= this.getY()
				&& pr.getX()+pr.getWidth() <= this.getX()+this.getWidth()
				&& pr.getY()+pr.getHeight() <= this.getY()+this.getWidth())*/
		/*if( ac.approximateMoreEqual(pr.getX(), this.getX())
				&& ac.approximateMoreEqual(pr.getY(), this.getY())
				&& ac.approximateLessEqual(pr.getX()+pr.getWidth(), this.getX()+this.getWidth())
				&& ac.approximateLessEqual(pr.getY()+pr.getHeight(), this.getY()+this.getHeight()))*/
			/*return true;
		return false;*/
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
		return ac.weakLess(Math.abs(this.getY()+this.getHeight()-pr.getY()-pr.getHeight()), 
				PDFRectangle.threshold);
	}
	
	public boolean isSameColumn(PDFRectangle pr){
		return ac.weakLess(Math.abs(this.getX() - pr.getX()), PDFRectangle.threshold);
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
		if(ac.weakLess(Math.abs(this.getY()-pr.getY()-pr.getHeight()), 
				3*PDFRectangle.blank))
			return true;
		else
			return false;
	}
	
	public boolean isSameHeight(double h){
		return ac.alreadyEqual(this.getHeight(), h);
	}
	
	public boolean isSameWidth(double d){
		return ac.alreadyEqual(this.getWidth(), d);
	}
	
	public void print(){
		System.out.println("PDFRectangle:	X:"+this.getX()
				+ ";Y:"+(this.getY()/*+this.getHeight()*/)
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
		if(this.isSameRow(pr))
			return false;
		if( ac.weakMore(this.getY()+this.getHeight(), pr.getY()+pr.getHeight()))
			return true;
		return false;
	}
	
	public boolean isLefter(PDFRectangle pr){
		if( this.getX()< pr.getX())
			return true;
		return false;
	}
	
	public boolean isVerticalLine(){
		if(this.rt == RectangleType.PDF_LINE)
			if(this.getHeight() > this.getWidth())
				return true;
		return false;
	}
	
	public boolean isHorizontalLine(){
		if(this.rt == RectangleType.PDF_LINE)
			if(this.getHeight() < this.getWidth())
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
		return ac.weakMore(this.getMidX(), pr.getMidX());
	}
	
	public boolean isHigherLefter(PDFRectangle pr){
		if(this.isHigher(pr))
			return true;
		else if(pr.isHigher(this))
			return false;
		else{
			if(this.isLefter(pr))
				return true;
			else
				return false;
		}
	}
	
	public double getMeasure(){
		return rect.getWidth()*rect.getHeight();
	}
	
	public double intersect(PDFRectangle pr){
		double x = Math.max(this.getX(), pr.getX());
		double y = Math.max(this.getY(), pr.getY());
		double x2 = Math.min(this.getX()+this.getWidth(), pr.getX()+pr.getWidth());
		double y2 = Math.min(this.getY()+this.getHeight(), pr.getY()+pr.getHeight());
		if(x2 > x && y2 > y)
			return (x2-x)*(y2-y);
		else
			return -1;
	}
}
