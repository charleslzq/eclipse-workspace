package src.processing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDPage;

import src.tool.PDFCharacter;
import src.tool.PDFRectangle;
import src.tool.PDFTable;
import src.tool.PageTextArea;
import src.tool.RectangleType;
import src.tool.TextStripperByPDFRectangle;

public class LineTableExtractor extends TokenParserTableExtrctor {
	
	private PDPage pdpage;
	private TextStripperByPDFRectangle tsbro;

	@Override
	public List<PageTextArea> getAreas(List tokens) {
		// TODO Auto-generated method stub
		List<PDFRectangle> rects = buildAreaFromLines(rectangleParser(tokens));
		List<PageTextArea> areas = new ArrayList<PageTextArea>();
		
		for(int i=0; i<rects.size(); i++){
			PageTextArea cell = new PageTextArea(tokens.size()+i,rects.get(i));
			areas.add(cell);	
		}
		
		sort(areas);
		return areas;
	}

	@Override
	public void removeUselessRegions(List<PageTextArea> textAreas) {
		// TODO Auto-generated method stub
		for(int i=0; i < textAreas.size()-1 ; i++){
			PDFRectangle pr1 = textAreas.get(i).getArea();
			for(int j=i+1; j < textAreas.size(); j++){
				PDFRectangle pr2 = textAreas.get(j).getArea();
				if( pr1.intersect(pr2)/pr2.getMeasure() >= 0.6){
					textAreas.remove(j);
					j--;
				}
				else if( pr1.intersect(pr2)/pr1.getMeasure() >= 0.6 ){
					textAreas.remove(i);
					if(i > 1)
						i--;
				}
			}
		}
	}

	@Override
	public void attachTexts(List<PageTextArea> areas) throws Exception{
		// TODO Auto-generated method stub
		for(int i = 0; i < areas.size(); i++){
			int no = areas.get(i).getAreaNo();
			tsbro.addRegion(no, areas.get(i).getArea());
		}
		tsbro.extractRegions(pdpage);
		for(int i = 0; i < areas.size(); i++){
			int no = areas.get(i).getAreaNo();
			areas.get(i).setCharacters(tsbro.getCharactersByRegion(no));
		}
	}

	@Override
	public List<PDFTable> getTables(List tokens) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	private List<PDFRectangle> getVerticalLines(Map<Integer, PDFRectangle> rects){
		List<PDFRectangle> result = new ArrayList<PDFRectangle>();
		Iterator<Integer> it = rects.keySet().iterator();
		while(it.hasNext()){
			PDFRectangle pr = rects.get(it.next());
			if(pr.isVerticalLine() == true)
				result.add(pr);
		}
		return result;
	}
	
	private List<PDFRectangle> getHorizontalLines(Map<Integer, PDFRectangle> rects){
		List<PDFRectangle> result = new ArrayList<PDFRectangle>();
		Iterator<Integer> it = rects.keySet().iterator();
		while(it.hasNext()){
			PDFRectangle pr = rects.get(it.next());
			if(pr.isHorizontalLine() == true)
				result.add(pr);
		}
		return result;
	}
	
	private List<PDFRectangle> buildAreaFromLines(Map<Integer, PDFRectangle> rects){
		List<PDFRectangle> lines =this.getVerticalLines(rects);
		List<PDFRectangle> result = new ArrayList<PDFRectangle>();
		for(int i=0; i<lines.size()-1 ; i++){
			PDFRectangle p = formArea(lines.get(i), lines.get(i+1));
			if(p != null
					&& p.getType() == RectangleType.PDF_CELL)
				result.add(p);
		}
		return result;
	}

	private PDFRectangle formArea(PDFRectangle p1,
			PDFRectangle p2) {
		// TODO Auto-generated method stub
		if(p1.getType() == RectangleType.PDF_LINE 
				&& p2.getType() == RectangleType.PDF_LINE){
			if(p2.getHeight() >= p1.getHeight()){
				double x = p1.getX() + p1.getWidth();
				double y = p2.getY();
				double width = p2.getX() - x;
				double height = p2.getHeight();
				if(p1.isSameY(y+height) 
						&& p1.isLefter(p2))
					return new PDFRectangle(x, y, width, height);
			}
			else{
				double x = p1.getX() + p1.getWidth();
				double y = p1.getY();
				double width = p2.getX() - x;
				double height = p1.getHeight();
				if(p1.isSameY(y+height)
						&& p1.isLefter(p2))
					return new PDFRectangle(x, y, width, height);
			}
		}
		return null;
	}

}
