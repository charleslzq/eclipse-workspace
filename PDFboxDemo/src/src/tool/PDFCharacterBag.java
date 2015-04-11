package src.tool;

import java.util.ArrayList;
import java.util.List;

public class PDFCharacterBag implements Comparable<PDFCharacterBag>{
	private static ApproximateCalculation ac= new ApproximateCalculation(1);
	private List<PDFCharacter> bag;
	private double x;
	private double y;
	private double width;
	private double height;
	
	public PDFCharacterBag(){
		x=-1;
		y=-1;
		width=-1;
		height=-1;
		bag = new ArrayList<PDFCharacter>();
	}
	
	public void addCharacter(PDFCharacter c){
		bag.add(c);
		if(bag.size() == 1){
			x = c.getX();
			y = c.getY();
			width = c.getWidth();
			height = c.getHeight();
		}
		else
			width += c.getWidth();
	}
	
	public boolean isSameRow(PDFCharacter c){
		if(bag.size() == 0)
			return true;
		return ac.alreadyEqual(y, c.getY());
	}

	public boolean isNextInTheSameRow(PDFCharacter c) {
		// TODO Auto-generated method stub
		if(isSameRow(c) == false)
			return false;
		if(ac.alreadyEqual(x+width, c.getX()))
			return true;
		return false;
	}

	public String getString() {
		// TODO Auto-generated method stub
		if(bag.size() == 0)
			return "";
		String s = "";
		for(int i=0; i<bag.size(); i++)
			s += bag.get(i).getCharacter();
		return s;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public List<PDFCharacter> getCharacters() {
		// TODO Auto-generated method stub
		return this.bag;
	}

	@Override
	public int compareTo(PDFCharacterBag b) {
		// TODO Auto-generated method stub
		if(this.getY()+this.getHeight() > b.getY()+b.getHeight())
			return 1;
		else if(this.getY()+this.getHeight() == b.getY()+b.getHeight())
			if(this.getX() <= b.getX())
				return -1;
		return 1;
	}
	
}
