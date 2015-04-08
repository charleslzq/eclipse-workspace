package src.tool;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.dom4j.Element;

public class ContentStreamParser {
	private int pageNo;
	private PDPage pdpage;
	private TokenParser tp;
	private List<PageTextArea> pta;
	private List<PageTextArea> ptaForLine;
	private List<PageTextArea> ptaForText;
	private TextStripperByPDFRectangle tsbro;
	private List<TableInformation> ti;
	private List<TableInformation> tiForLine;
	private List<TableInformation> tiForText;
	
	public ContentStreamParser(int no, PDPage pdp) throws IOException{
		pageNo = no;
		pdpage = pdp;
		
		tp = new TokenParser(pdpage.getContents().getStream().getStreamTokens());
		tsbro = new TextStripperByPDFRectangle();
		pta = new ArrayList<PageTextArea>();
		ptaForLine = new ArrayList<PageTextArea>();
		
		ti = new ArrayList<TableInformation>();
	}
	
	public void parse() throws IOException{
		pta = this.TextAreaConstructor();
		ptaForLine = this.LineAreaConstructor();
		ptaForText = this.TextPositionConstructor();
		/*for(int i = 0; i < pta.size(); i++)
			pta.get(i).print();*/
		ti = this.extractTableInformation(pta);
		tiForLine = this.extractTableInformation(ptaForLine);
		tiForText = this.extractTableInformation(ptaForText);
	}

	public List<PageTextArea> TextAreaConstructor() throws IOException{
		Map<Integer, PDFRectangle> areas = tp.getAreas();
		List<PageTextArea> textAreas = this.constructRegions(areas);
		
		removeUselessRegions(textAreas);

		for(int i = 0; i < tp.getSize(); i++){
			if(areas.containsKey(new Integer(i))){
				PDFRectangle pr = areas.get(new Integer(i));
				if(pr.getType() == RectangleType.PDF_CELL)
					tsbro.addRegion(i, pr);
			}
		}
		tsbro.extractRegions(pdpage);
		attachTexts(textAreas);
		this.buildConnectionsBetweenRegions(textAreas);
		checkMissingAreas(tsbro, textAreas);
		return textAreas;
	}
	
	public List<PageTextArea> LineAreaConstructor() throws IOException{
		Map<Integer, PDFRectangle> areas = tp.getAreas();
		List<PageTextArea> lineAreas = this.buildAreasFromLines(areas);
		this.buildConnectionsBetweenRegions(lineAreas);
		return lineAreas;
	}
	
	public void checkMissingAreas(TextStripperByPDFRectangle tsbro2,
			List<PageTextArea> textAreas) throws IOException {
		// TODO Auto-generated method stub
		int base = tp.getSize();
		int previousSize = textAreas.size();
		for(int i = 0; i < textAreas.size(); i++){
			PageTextArea now = textAreas.get(i);
			if( now.getRight() != null){
				if(now.getRight().getX() - now.getX() - now.getWidth()
						> PDFRectangle.getBlank()){
					PDFRectangle rect = new PDFRectangle( now.getX()+now.getWidth(), 
							now.getY(),
							now.getRight().getX()-now.getX()-now.getWidth(),
							now.getHeight());
					tsbro.addRegion(base+i, rect);
					PageTextArea pta = new PageTextArea(base+i,rect);
					pta.setReferenced(true);
					PageTextArea tmp = now.getRight();
					now.setRight(pta);
					pta.setRight(tmp);
					textAreas.add(pta);
				}
			}
		}
		tsbro.extractRegions(pdpage);
		for(int i = previousSize; i < textAreas.size(); i++ ){
			int no = textAreas.get(i).getAreaNo();
			List<PDFCharacter> cs = tsbro.getCharactersByRegion(no);
			textAreas.get(i).setCharacters(this.tsbro.getCharactersByRegion(no));
		}
		this.sort(textAreas);
		this.buildConnectionsBetweenRegions(textAreas);
		/*for(int i =0; i < previousSize; i++){
			PageTextArea pta1 = textAreas.get(i);
			for(int j =0; j < textAreas.size()-previousSize; j++){
				PageTextArea pta2 = textAreas.get(previousSize+j);
				if(pta1.isNextCellInTheSameColumn(pta2)){
					pta1.setDown(pta2);
					pta2.setReferenced(true);
				}
			}
		}
		for(int i =0; i < textAreas.size()-previousSize; i++){
			PageTextArea pta1 = textAreas.get(previousSize+i);
			for(int j=i+1; j < textAreas.size()-previousSize; j++){
				PageTextArea pta2 = textAreas.get(previousSize+j);
				if(pta1.isNextCellInTheSameColumn(pta2)){
					pta1.setDown(pta2);
					pta2.setReferenced(true);
				}
			}
		}*/
	}

	public void removeUselessRegions(List<PageTextArea> textAreas) {
		// TODO Auto-generated method stub
		for(int i=0; i < textAreas.size()-1 ; i++){
			PDFRectangle pr1 = textAreas.get(i).getArea();
			for(int j=i+1; j < textAreas.size(); j++){
				PDFRectangle pr2 = textAreas.get(j).getArea();
				if(pr1.isInThisArea(pr2))
					//if(textAreas.get(i).getString() != null)
						/*if(textAreas.get(j).getString() == null
							|| textAreas.get(j).getString().trim().isEmpty()
							|| textAreas.get(j).getString().trim().equals(
									textAreas.get(i).getString().trim()))*/{
										textAreas.remove(j);
										j--;
					//continue;
				}
				else if(pr2.isInThisArea(pr1))
					/*if(textAreas.get(j).getString() != null)
						if(textAreas.get(i).getString().trim().isEmpty()
								|| textAreas.get(j).getString().trim().equals(
										textAreas.get(i).getString().trim()))*/{
											//System.out.println(i+"/"+textAreas.size());
											textAreas.remove(i);
											if(i > 1)
												i--;
					//continue;
				}
				else if( pr1.intersect(pr2)/pr2.getMeasure() >= 0.6){
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

	public List<PageTextArea> constructRegions(Map<Integer, PDFRectangle> areas){
		List<PageTextArea> textAreas = new ArrayList<PageTextArea>();
		for(int i=0; i<tp.getSize(); i++)
			if(areas.containsKey(new Integer(i))){
				PDFRectangle pr = areas.get(new Integer(i));
				if(pr.getType() == RectangleType.PDF_CELL)
					textAreas.add(new PageTextArea(i,pr));
			}
		int base = tp.getSize()*2;
		List<PDFRectangle> rects = tp.buildAreaFromLines(areas);
		/*for(int i = 0; i < rects.size(); i++){
			PDFRectangle pr = rects.get(i);
			if(pr.getType() == RectangleType.PDF_CELL)
				textAreas.add(new PageTextArea(base+i+1,pr));
		}*/
		this.sort(textAreas);
		return textAreas;
	}
	
	public void buildConnectionsBetweenRegions(List<PageTextArea> textAreas){
		for(int i = 0; i < textAreas.size(); i++){
			PageTextArea p1 = textAreas.get(i);
			for(int j = i+1 ; j < textAreas.size(); j++){
				PageTextArea p2 = textAreas.get(j);
				if( p1.isSameRow(p2)){
					if(p1.isLefter(p2)){
						if(p1.getRight() == null){
							p1.setRight(textAreas.get(j));
						}
					}
				}
				else if( p1.isNextCellInTheSameColumn(p2)){
					if(p1.isHigher(p2)){
						if(p1.getDown() == null ){
							p1.setDown(textAreas.get(j));
						}
					}
				}
			}
		}
		for(int i=0; i<textAreas.size();i++){
			PageTextArea p1 = textAreas.get(i);
			if(p1.getRight() != null)
				p1.getRight().setReferenced(true);
			if(p1.getDown() != null){
				p1.getDown().setReferenced(true);
			}
		}
	}
	
	public void attachTexts(List<PageTextArea> textAreas){
		for(int i = 0; i < textAreas.size(); i++){
			int no = textAreas.get(i).getAreaNo();
			List<PDFCharacter> cs = tsbro.getCharactersByRegion(no);
			textAreas.get(i).setCharacters(this.tsbro.getCharactersByRegion(no));
		}
	}
	
	public List<PageTextArea> getPTA(){
		return pta;
	}
	
	public List<TableInformation> extractTableInformation(List<PageTextArea> p){
		List<TableInformation> t = new ArrayList<TableInformation>();
		for(int i = 0; i < p.size(); i++){
			if(!p.get(i).isReferenced()){
				if(!p.get(i).isIsolated()){
					PageTextArea head = p.get(i);
					PageTextArea current = head;
					List<Float> ls1 = new ArrayList<Float>();
					List<Float> ls2 = new ArrayList<Float>();
					List<String> ls3 = new ArrayList<String>();
					int count1 = 0;
					int count2 = 0;
					while(current != null){
						count1++;
						ls1.add(new Float(current.getX()));
						ls3.add(current.getString());
						current = current.getRight();
					}
					current = head;
					int max_row = current.cellsOnTheRight();
					while(current.getDown()!=null){
						current = current.getDown();
						int tmp = current.cellsOnTheRight();
						if( tmp > max_row)
							max_row = tmp;
					}
					while(current!=null){
						count2++;
						ls2.add(new Float(current.getX()));
						PageTextArea tmp = current.getRight();
						if(tmp!=null){
							while(tmp.getDown() != null)
								tmp = tmp.getDown();
							current = tmp;
						}
						else
							break;
					}
					t.add(new TableInformation(i,count1,count2,ls1,ls2,ls3, max_row));
				}
			}
		}
		return t;
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
	
	@SuppressWarnings("unchecked")
	public List<Pair> getRowHeaders(PageTextArea header){
		if(header == null)
			return null;
		if(header.isIsolated() == true)
			return null;
		if(header.isReferenced() == true)
			return null;
		List<Pair> returnList = new ArrayList<Pair>();
		PageTextArea currentRowHeader = header;
		//returnList.add(new Pair(header.getString(), header));
		/*if(currentRowHeader.getRight() != null){
			if(currentRowHeader.getRight().getHeight() < currentRowHeader.getHeight())
				currentRowHeader.addToRowHeaderList(returnList, currentRowHeader.getString());
			else
				returnList.add(new Pair(header.getString(), header));
		}*/
		while(currentRowHeader.getDown() != null){
			currentRowHeader = currentRowHeader.getDown();
			currentRowHeader.addToRowHeaderList(returnList, currentRowHeader.getString());
		}
		return returnList;
	}
	
	public List<Pair> getColumnHeaders(PageTextArea header){
		if(header == null)
			return null;
		if(header.isIsolated() == true)
			return null;
		if(header.isReferenced() == true)
			return null;
		List<Pair> returnList = new ArrayList<Pair>();
		PageTextArea cHeader = header;
		while(cHeader.getRight() != null){
			cHeader = cHeader.getRight();
			cHeader.addToColumnHeaderList(returnList, cHeader.getString());
		}
		return returnList;
	}
	
	public void writeLineTableToXML(Element root){
		if(ptaForLine.size()>0){
			this.writeTableToXML(root, ptaForLine, tiForLine,"Line");
		}
	}
	
	public void writeCellTableToXML(Element root){
		if(pta.size()>0){
			this.writeTableToXML(root, pta, ti, "ClippingPath");
		}
	}
	
	public void writeTextTableToXML(Element root){
		if(ptaForText.size()>0){
			this.writeTableToXML(root, ptaForText, tiForText, "TextPosition");
		}
	}
	
	public void writeTableToXML(Element root, List<PageTextArea> p, List<TableInformation> tt, String method){
		if(tt.size()>0){
			for(int i = 0; i < tt.size(); i++){
				PageTextArea header = p.get(tt.get(i).getHead());
				TableInformation t = tt.get(i);
				
				if(t.getMax_row() != 1){
					Element table = root.addElement("Table");
					table.addAttribute("PageNo", this.pageNo+"");
					table.addAttribute("Method", method);
					List<Pair> lt = this.getRowHeaders(header);
					List<Pair> cl = this.getColumnHeaders(header);
					Element firstRow = table.addElement("FirstRow");
					firstRow.addAttribute("行头", header.getString());
					if(cl.size() == 0)
						firstRow.addAttribute("列数", "0");
					else{
						for(int j=0 ; j<cl.size(); j++)
							firstRow.addAttribute("第"+(j+1)+"列", cl.get(j).getContent());
					}
					if(lt.size() > 0){
						for(int j=0 ; j<lt.size(); j++){
							String s = lt.get(j).getContent();
							Element row = table.addElement("row");
							row.addAttribute("行头", s);
							PageTextArea rowIt = lt.get(j).getPta();
							int count = 1;
							rowIt = rowIt.getRight();
							if( rowIt == null){
								row.addAttribute("列数", "0");
								continue;
							}
							while(rowIt != null){
								String st = "第"+count+"列";
								if(count-1 < cl.size())
									st = cl.get(count-1).getContent();
								row.addAttribute(st, rowIt.getString());
								rowIt = rowIt.getRight();
								count++;
							}
						}
					}
				}
			}
		}
	}
	
	public void writeXML(Element pageRoot){
		this.writeXML(pageRoot, pta, ti);
		this.writeXML(pageRoot, ptaForLine, tiForLine);
	}
	
	public void writeXML(Element pageRoot, List<PageTextArea> p, List<TableInformation> t){
		Element currentTable = null;
		int count = 0;
		List<PageTextArea> headers = this.getTableHeader(p);
		for(int i = 0; i < headers.size(); i++){
			if(t.get(i).getMax_row()!=1){
				currentTable = pageRoot.addElement("Table");
				count++;
				currentTable.addAttribute("No.", count+"");
				headers.get(i).writeToXML(currentTable);
			}
		}
		Element cells = pageRoot.addElement("Cells");
		for(int i=0; i < p.size(); i++)
			p.get(i).writeToXML(cells);
	}
	
	public void print(){
		for(int i=0; i<pta.size(); i++)
			ptaForLine.get(i).print();
	}
	
	public void sort(List<PageTextArea> pList){
		for(int i=0; i<pList.size()-1; i++){
			PageTextArea min = pList.get(i);
			int index = i;
			for(int j=i+1; j<pList.size(); j++){
				PageTextArea it = pList.get(j);
				if(it.isHigherLeft(min) == true){
					min = it;
					index = j;
				}
			}
			if(i != index){
				pList.set(index, pList.get(i));
				pList.set(i, min);
			}
		}
	}
	
	public void sortChar(List<PDFCharacter> cs){
		for(int i=0; i<cs.size()-1; i++){
			PDFCharacter min = cs.get(i);
			int index = i;
			for(int j=i+1; j<cs.size();j++){
				PDFCharacter it = cs.get(j);
				if(it.isHigherLefter(min)){
					min = it;
					index = j;
				}
			}
			if(i != index){
				cs.set(index, cs.get(i));
				cs.set(i, min);
			}
		}
	}
	
	public List<PDFCharacterBag> formBags(List<PDFCharacter> cs){
		this.sortChar(cs);
		List<PDFCharacterBag> bags = new ArrayList<PDFCharacterBag>();
		PDFCharacterBag now = null;
		for(int i=0; i<cs.size(); i++){
			if(i == 0){
				now = new PDFCharacterBag();
				bags.add(now);
				now.addCharacter(cs.get(i));
				continue;
			}
			if(now.isNextInTheSameRow(cs.get(i)))
				now.addCharacter(cs.get(i));
			else{
				now = new PDFCharacterBag();
				bags.add(now);
				now.addCharacter(cs.get(i));
			}
			
		}
		return bags;
	}
	
	public List<PageTextArea> TextPositionConstructor() throws IOException{
		List<PDFCharacter> cs = this.getAllCharacters();
		List<PDFCharacterBag> bags = this.formBags(cs);
		List<PageTextArea> areas = this.formTextAreaFromBags(bags);
		
		this.buildConnectionsBetweenRegions(areas);
		return areas;
	}
	
	public List<PageTextArea> formTextAreaFromBags(List<PDFCharacterBag> bags){
		List<PageTextArea> areas = new ArrayList<PageTextArea>();
		for(int i=0; i<bags.size(); i++)
			areas.add(convertToTextArea(i, bags.get(i)));
		this.sort(areas);
		return areas;
	}
	
	private PageTextArea convertToTextArea(int no, PDFCharacterBag bag) {
		// TODO Auto-generated method stub
		if(bag == null)
			return null;
		PageTextArea p = new PageTextArea(2*tp.getSize()+no,
				new PDFRectangle(bag.getX(),
						bag.getY(),
						bag.getWidth(),
						bag.getHeight()));
		p.setCharacters(bag.getCharacters());
		return p;
	}

	public List<PageTextArea> buildAreasFromLines(Map<Integer, PDFRectangle> as) throws IOException{
		List<PDFRectangle> rects = tp.buildAreaFromLines(as);
		tsbro = new TextStripperByPDFRectangle();
		
		List<PageTextArea> areas = new ArrayList<PageTextArea>();
		
		for(int i=0; i<rects.size(); i++){
			PageTextArea cell = new PageTextArea(tp.getSize()+i,rects.get(i));
			areas.add(cell);	
		}
		this.sort(areas);
		this.removeUselessRegions(areas);
		
		for(int i=0; i<rects.size(); i++){
			tsbro.addRegion(tp.getSize()+i, rects.get(i));
		}
		
		tsbro.extractRegions(pdpage);
		
		for(int i = 0; i < areas.size(); i++){
			int no = areas.get(i).getAreaNo();
			List<PDFCharacter> cs = tsbro.getCharactersByRegion(no);
			areas.get(i).setCharacters(tsbro.getCharactersByRegion(no));
		}
		return areas;
	}
	
	public List<PDFCharacter> getAllCharacters() throws IOException{
		TextStripperByPDFRectangle ts = new TextStripperByPDFRectangle();
		PDRectangle pd = pdpage.getMediaBox();
		PDFRectangle p = new PDFRectangle(pd.getLowerLeftX(),pd.getLowerLeftY(),pd.getWidth(),pd.getHeight());
		
		ts.addRegion(-1, p);
		ts.extractRegions(pdpage);
		return ts.getCharactersByRegion(-1);
	}
}
