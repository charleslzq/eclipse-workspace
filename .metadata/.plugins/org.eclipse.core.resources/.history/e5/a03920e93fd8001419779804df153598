package src.tool;

import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.util.TextPosition;
import org.dom4j.Element;

public class CharacterObject {
	private int areaNo;
	private TextPosition text;
	
	public CharacterObject(int no, TextPosition tt){
		areaNo = no;
		text = tt;
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
	
	public boolean isInTheSameLine(CharacterObject co){
		double err = 0.1;
		if( almostEqual(this.getY(), co.getY(), err) && almostEqual(this.getHeight(), co.getHeight(), err) )
			return true;
		return false;
	}
	
	public boolean isNextInTheSameLine(CharacterObject co){
		double err = 0.2;
		if( this.isInTheSameLine(co) )
			if(almostEqual(this.getX()+this.getWidth(), co.getX(), err))
				return true;
		return false;
	}
	
	public void print(){
		System.out.println(getCharacter());
		//System.out.println(text.toString());
		//System.out.println(getX());
		//System.out.println(getY());
		//System.out.println(getHeight());
		//System.out.println(getWidth());
		//System.out.println(getSpacing());
		//System.out.println(getFontSize());
	}
	
	public void writeToXML(Element e){
		Element character = e.addElement("Character");
		character.addAttribute("Char", text.getCharacter());
		Element x = character.addElement("X");
		x.addText(text.getX()+"");
		Element y = character.addElement("Y");
		y.addText(text.getY()+"");
		Element w = character.addElement("Width");
		w.addText(text.getWidth()+"");
		Element h = character.addElement("Height");
		h.addText(text.getHeight()+"");
		Element f = character.addElement("Font");
		if(text.getFont().getBaseFont() != null)
			f.addText(text.getFont().getBaseFont());
		else
			f.addText("");
		Element fs = character.addElement("FontSize");
		fs.addText(this.getFontSize()+"");
	}
	
	public boolean almostEqual(double a,double b,double err){
		if(b-a <= err )
			return true;
		return false;
	}

}
