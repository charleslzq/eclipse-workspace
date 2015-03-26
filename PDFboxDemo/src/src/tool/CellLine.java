package src.tool;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Element;

public class CellLine {
	int lineNo;
	List<CharacterGroup> groups = new ArrayList<CharacterGroup>();
	private float x;
	private float y;
	private float width;
	private float height;
	
	public CellLine(int no, CharacterGroup firstGroup){
		lineNo = no;
		x = firstGroup.getX();
		y = firstGroup.getY();
		width = firstGroup.getWidth();
		height = firstGroup.getHeight();
		groups.add(firstGroup);
	}
	
	public void addGroup(CharacterGroup cg){
		int last = groups.size()-1;
		//System.out.println(groups.size()+" "+last);
		groups.get(last).setSpacing(cg);
		float spacing = groups.get(last).getSpacing();
		groups.add(cg);
		width += spacing + cg.getWidth();
	}
	
	public void writeToXML(Element e){
		Element cellLine = e.addElement("CellLine");
		cellLine.addAttribute("LineNo", lineNo+"");
		cellLine.addAttribute("Content", this.getString());
		Element x = cellLine.addElement("X");
		x.addText(this.getX()+"");
		Element y = cellLine.addElement("Y");
		y.addText(this.getY()+"");
		Element w = cellLine.addElement("Width");
		w.addText(this.getWidth()+"");
		Element h = cellLine.addElement("Height");
		h.addText(this.getHeight()+"");
		Element gs = cellLine.addElement("Groups");
		for(int i = 0; i < groups.size(); i++){
			groups.get(i).writeToXML(gs);
		}
	}
	
	
	public String getString() {
		// TODO Auto-generated method stub
		String s = "";
		for(int i = 0; i < groups.size(); i++){
			s += groups.get(i).getString();
			if(i != groups.size()-1)
				s += " ";
		}
		return s;
	}

	public int getAreaNo() {
		return groups.get(0).getAreaNo();
	}
	
	public int getLineNo(){
		return this.lineNo;
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

}
