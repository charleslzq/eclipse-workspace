package src.tool;

import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.events.Characters;

import org.dom4j.Element;

public class CharacterGroup {
	private List<CharacterObject> group = new ArrayList<CharacterObject>();
	private float x;
	private float y;
	private float width;
	private float height;
	private float spacing;
	
	public CharacterGroup(CharacterObject firstObject){
		group.add(firstObject);
		x = firstObject.getX();
		y = firstObject.getY();
		width = firstObject.getWidth();
		height = firstObject.getHeight();
		spacing = -1;
	}
	
	public void addCharacter(CharacterObject co){
		group.add(co);
		width += co.getWidth();
	}
	
	public String getString(){
		String s = "";
		for(int i = 0; i < group.size(); i++)
			s += group.get(i).getCharacter();
		return s;
	}

	public int getAreaNo() {
		return group.get(0).getAreaNo();
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}

	public float getSpacing() {
		return spacing;
	}
	
	public boolean isInTheSameLine(CharacterGroup cg){
		if(this.getY() == cg.getY())
			return true;
		return false;
	}
	
	public void setSpacing(CharacterGroup cg){
		this.spacing = cg.getX()-this.getX()-this.getWidth();
	}

	public void writeToXML(Element text) {
		// TODO Auto-generated method stub
		Element g = text.addElement("Group");
		g.addAttribute("Content", getString());
		Element x = g.addElement("X");
		x.addText(this.getX()+"");
		Element y = g.addElement("Y");
		y.addText(this.getY()+"");
		Element w = g.addElement("Width");
		w.addText(this.getWidth()+"");
		Element h = g.addElement("Height");
		h.addText(this.getHeight()+"");
		Element s = g.addElement("Spacing");
		s.addText(this.getSpacing()+"");
		Element characters = g.addElement("Characters");
		for(int i = 0; i < group.size(); i++)
			group.get(i).writeToXML(characters);
	}

}
