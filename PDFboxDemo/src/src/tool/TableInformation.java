package src.tool;

import java.util.List;

import org.dom4j.Element;

public class TableInformation {
	private int head;
	private int rowNumInTheFirstRow;
	private int rowNumInTheLastRow;
	private List<Float> XofCellInTheFirstRow;
	private List<Float> XofCellInTheLastRow;
	private List<String> textsInTheFirstRow;
	private int max_row;
	
	public TableInformation(int h, int a, int b, List<Float> c, List<Float> d, List<String> texts, int m_row){
		head = h;
		rowNumInTheFirstRow = a;
		rowNumInTheLastRow = b;
		XofCellInTheFirstRow = c;
		XofCellInTheLastRow = d;
		textsInTheFirstRow = texts;
		max_row = m_row;
	}
	
	

	public int getHead() {
		return head;
	}



	public int getMax_row() {
		return max_row;
	}



	public int getRowNumInTheFirstRow() {
		return rowNumInTheFirstRow;
	}

	public int getRowNumInTheLastRow() {
		return rowNumInTheLastRow;
	}
	
	public float getXofCellInTheFirstRow(int i){
		return this.XofCellInTheFirstRow.get(i);
	}
	
	public float getXofCellInTheLastRow(int i){
		return this.XofCellInTheLastRow.get(i);
	}
	
	public String getText(int i){
		return this.textsInTheFirstRow.get(i);
	}

	public void writeToXML(Element tableInformation) {
		// TODO Auto-generated method stub
		Element thisTable = tableInformation.addElement("Table");
		Element n1 = thisTable.addElement("RowNumInTheFirstRow");
		n1.addText(this.rowNumInTheFirstRow+"");
		Element n2 = thisTable.addElement("RowNumInTheLastRow");
		n2.addText(this.rowNumInTheLastRow+"");
		Element XsInTheFirstRow = thisTable.addElement("XsInTheFirstRow");
		for(int i = 0; i < this.XofCellInTheFirstRow.size(); i++){
			Element x = XsInTheFirstRow.addElement("X");
			x.addText(this.XofCellInTheFirstRow.get(i)+"");
		}
		Element XsInTheLastRow = thisTable.addElement("XsInTheLastRow");
		for(int i = 0; i < this.XofCellInTheLastRow.size(); i++){
			Element x = XsInTheLastRow.addElement("X");
			x.addText(this.XofCellInTheLastRow.get(i)+"");
		}
		Element textInTheFirstRow = thisTable.addElement("TextsInTheFirstRow");
		for(int i = 0; i < this.textsInTheFirstRow.size(); i++){
			Element x = textInTheFirstRow.addElement("X");
			x.addText(this.textsInTheFirstRow.get(i));
		}
	}

}
