package src.tool;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PageTextArea {
	private int areaNo;
	private double colNo;
	private double rowNo;
	
	private RectangleObject area;
	private List<TextObject> texts;
	
	private PageTextArea right;
	private PageTextArea down;
	
	double splitedRows;
	double splitedCols;
	
	boolean referenced;
	
	public PageTextArea(int n, RectangleObject ro){
		areaNo = n;
		colNo = 0;
		rowNo = 0;
		
		splitedRows = 1;
		splitedCols = 1;
		
		area = ro;
		texts = new ArrayList<TextObject>();
		
		right = null;
		down = null;
		
		referenced = false;
	}
	
	
	
	public boolean isReferenced() {
		return referenced;
	}



	public void setReferenced(boolean referenced) {
		this.referenced = referenced;
	}



	public void putText(TextObject to){
		texts.add(to);
	}
	
	public void setRight(PageTextArea r){
		right = r;
	}
	
	public void setDown(PageTextArea d){
		down = d;
	}
	
	public PageTextArea getRight(){
		return right;
	}
	
	public PageTextArea getDown(){
		return down;
	}
	
	public boolean isSameRow(PageTextArea pta){
		if(area.isSameRow(pta.getArea()))
			return true;
		return false;
	}
	
	public boolean isSameColumn(PageTextArea pta){
		if(area.isSameColumn(pta.getArea()))
			return true;
		return false;
	}
	
	public PageTextArea nextRight(){
		return right;
	}
	
	public RectangleObject getArea(){
		return area;
	}
	
	public void print(){
		System.out.println("Area No: " + areaNo);
		System.out.println("Column No: " + colNo);
		System.out.println("Row No: " + rowNo);
		//area.print();
		if( right != null)
			System.out.println("Right: " + right.getAreaNo());
		if( down != null)
			System.out.println("Down: " + down.getAreaNo());
	}
	
	public int getAreaNo(){
		return areaNo;
	}
	
	public void setColNo(int n){
		colNo = n;
	}
	
	public void setRowNo(int n){
		rowNo = n;
	}
	
	public double getColNo(){
		return colNo;
	}
	
	public double getRowNo(){
		return rowNo;
	}
	
	public void recursiveSetColAndRowNo(double c, double r, int n){
		colNo = c;
		rowNo = r;
		if(this.getRight() != null)
			this.getRight().recursiveSetColAndRowNo(round(c+this.splitedCols,n), r, n);
		if(this.getDown() != null)
			this.getDown().recursiveSetColAndRowNo(c, round(r+this.splitedRows,n),n);
		
	}
	

	public boolean nullColRow(){
		if( colNo != 0)
			return false;
		if( rowNo != 0)
			return false;
		return true;
	}
	

	public double getSplitedRows() {
		return splitedRows;
	}

	public void setSplitedRows(double splitedRows) {
		this.splitedRows = splitedRows;
	}

	public double getSplitedCols() {
		return splitedCols;
	}

	public void setSplitedCols(double splitedCols) {
		this.splitedCols = splitedCols;
	}

	public double getHeight(){
		return area.getHeight();
	}
	
	public double getWidth(){
		return area.getWidth();
	}

	public boolean nextColumnInTheSameRow(PageTextArea pageTextArea) {
		// TODO Auto-generated method stub
		if( area.nextColumnInTheSameRow(pageTextArea.getArea()))
			return true;
		return false;
	}

	public boolean nextRowInTheSameColumn(PageTextArea pageTextArea) {
		// TODO Auto-generated method stub
		if( area.nextRowInTheSameColumn(pageTextArea.getArea()))
			return true;
		return false;
	}
	
	public boolean isIsolated(){
		if( right != null)
			return false;
		if( down != null)
			return false;
		return true;
	}
	
	public boolean isInThisArea(double x, double y){
		return area.isInThisRectangle(x, y);
	}
	
	public boolean isInThisArea(TextObject to){
		return area.isInThisRectangle(to.getX(), to.getY());
	}
	
	public boolean nullText(){
		if(texts.size() == 0)
			return true;
		return false;
	}
	
	public int textSize(){
		return texts.size();
	}
	
	public TextObject getText(int i){
		return texts.get(i);
	}
	
	public double getX(){
		return area.getXofLeftUpper();
	}
	
	public double getY(){
		return area.getYofLeftUpper();
	}
	
	public double round(double a, int n){
		BigDecimal b = new BigDecimal(a);
		double f = b.setScale(n, BigDecimal.ROUND_HALF_UP).doubleValue();
		return f;
	}
	
}
