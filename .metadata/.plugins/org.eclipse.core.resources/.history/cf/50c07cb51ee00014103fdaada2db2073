package src.tool;

import java.util.ArrayList;
import java.util.List;

public class PDFTable {
	private List<PageTextArea> pta;
	
	public PDFTable(List<PageTextArea> p){
		pta = p;
	}
	
	public void print(){
		for(int i=0; i<pta.size(); i++)
			pta.get(i).print();
		System.out.println();
	}
	
	public void writeToXML(){
		
	}
	
	public List<Pair> getRowHeaders(){
		List<Pair> returnList = new ArrayList<Pair>();
		PageTextArea currentRowHeader = pta.get(0);
		while(currentRowHeader.getDown() != null){
			currentRowHeader = currentRowHeader.getDown();
			currentRowHeader.addToRowHeaderList(returnList, currentRowHeader.getString());
		}
		return returnList;
	}
	
	public List<PageTextArea> getRows(PageTextArea rowHeader){
		PageTextArea it = rowHeader;
		List<PageTextArea> returnList = new ArrayList<PageTextArea>();
		returnList.add(it);
		while(it.getRight() != null){
			it = it.getRight();
			returnList.add(it);
		}
		return returnList;
	}
}
