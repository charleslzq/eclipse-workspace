package src.tool;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.util.TextPosition;

public class PDFCharacter extends TextPosition {
	private int areaNo;
	private TextPosition text;
	private static ApproximateCalculation ac = new ApproximateCalculation(3,0.1);
	
	public PDFCharacter(int n, TextPosition t){
		text = t;
		areaNo =n;
	}
	
	public int getAreaNo() {
		return areaNo;
	}
	
	public float getX(){
		return text.getX();
	}
	
	public float getY(){
		return text.getY();
	}
	
	public float getFontSize(){
		return text.getFontSize();
	}
	
	public float getWidth(){
		return text.getWidth();
	}
	
	public float getHeight(){
		return text.getHeight();
	}
	
	public float getSpacing(){
		return text.getWordSpacing();
	}
	
	public String getCharacter(){
		return text.getCharacter();
	}
	
	public PDFont getFont(){
		return text.getFont();
	}
	
	public boolean isInTheSameLine(PDFCharacter co){
		if( ac.alreadyEqual(this.getY(), co.getY() ) 
				&& ac.alreadyEqual(this.getHeight(), co.getHeight()) )
			return true;
		return false;
	}
	
	public boolean isNextInTheSameLine(PDFCharacter co){
		if( this.isInTheSameLine(co) )
			if(ac.alreadyEqual(this.getX()+this.getWidth(), co.getX()))
				return true;
		return false;
	}
	
	public void print(){
		System.out.println("Character:"+text.getCharacter()
				+";X:"+this.getX()
				+";Y:"+this.getY()
				+";Width:"+this.getWidth()
				+";Height:"+this.getHeight());
	}
}