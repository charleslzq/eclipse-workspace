package src.processing;

import java.util.List;

import src.interfaces.TableExtractor;
import src.tool.PDFTable;
import src.tool.PageTextArea;

public abstract class BasicTableExtractor implements TableExtractor {

	public void sort(List<PageTextArea> areas) {
		// TODO Auto-generated method stub
		for(int i=0; i<areas.size()-1; i++){
			PageTextArea min = areas.get(i);
			int index = i;
			for(int j=i+1; j<areas.size(); j++){
				PageTextArea it = areas.get(j);
				if(it.isHigherLeft(min) == true){
					min = it;
					index = j;
				}
			}
			if(i != index){
				areas.set(index, areas.get(i));
				areas.set(i, min);
			}
		}
	}

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
						}
					}
				}
				else if( p1.isNextCellInTheSameColumn(p2)){
					if(p1.isHigher(p2)){
						if(p1.getDown() == null ){
							p1.setDown(areas.get(j));
						}
					}
				}
			}
		}
		for(int i=0; i<areas.size();i++){
			PageTextArea p1 = areas.get(i);
			if(p1.getRight() != null)
				p1.getRight().setReferenced(true);
			if(p1.getDown() != null){
				p1.getDown().setReferenced(true);
			}
		}
	}

}