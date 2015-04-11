package src.processing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import src.interfaces.TableExtractor;
import src.tool.PDFTable;
import src.tool.PageTextArea;

/**
 * This class implements TableExtractor and any class represents a method to extract
 * table from a PDF page should be sub class of it. General methods such as sort are
 * implemented.
 * @author zqliu
 * @version 1.0
 */
public abstract class BasicTableExtractor implements TableExtractor {

	
	public void buildConnectionsBetweenRegions(List<PageTextArea> areas) {
		// TODO Auto-generated method stub
		for(int i = 0; i < areas.size(); i++){
			PageTextArea p1 = areas.get(i);
			for(int j = i+1 ; j < areas.size(); j++){
				PageTextArea p2 = areas.get(j);
				if( p1.isSameRow(p2)){
					if(p1.isLefter(p2)){
						if(p1.getRight() == null){
							p1.setRight(areas.get(j));
							p2.setReferenced(true);
						}
					}
				}
				else if( p1.isNextCellInTheSameColumn(p2)){
					if(p1.isHigher(p2)){
						if(p1.getDown() == null ){
							p1.setDown(areas.get(j));
							p2.setReferenced(true);
						}
					}
				}
			}
		}
	}
	
	public List<PageTextArea> getTableHeader(List<PageTextArea> p){
		List<PageTextArea> header = new ArrayList<PageTextArea>();
		for(int i = 0; i < p.size(); i++){
			if(!p.get(i).isReferenced()){
				if(!p.get(i).isIsolated()){
					header.add(p.get(i));
				}
			}
		}
		return header;
	}
	
	public PDFTable getTableFromHeader(PageTextArea header){
		List<PageTextArea> list = new ArrayList<PageTextArea>();
		header.addToList(list);
		Collections.sort(list);
		return new PDFTable(list);
	}

}
