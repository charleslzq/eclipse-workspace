package src.tool;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javafx.scene.control.TextArea;
import javafx.util.Pair;

import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.util.PDFOperator;
import org.apache.pdfbox.util.PDFTextStripperByArea;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class PageStreamParser {
	private int pageNo;
	//private PDStream content;
	private ContentStreamParser csp;
	private PDPage page;
	private List<TableInformation> ti;
	//private boolean extraTexts;
	//private boolean mightContainTableInTheNextPage;
	
	public PageStreamParser(int a, PDPage pdp) throws IOException{
		pageNo = a;
		page = pdp;
		//content = pdp.getContents();

		csp = new ContentStreamParser(pdp);
		
		//ti = csp.extractTableInformation();
		
		//extraTexts = csp.extraTextsAfterLastTable();
		
		//this.mightContainTableInTheNextPage = false;
	}
	
/*	public PDStream getPDStream(){
		return content;
	}*/
	
	public List<TableInformation> getTi() {
		return ti;
	}

	/*public boolean isMightContainTableInTheNextPage() {
		return mightContainTableInTheNextPage;
	}

	public void setMightContainTableInTheNextPage(
			boolean mightContainTableInTheNextPage) {
		this.mightContainTableInTheNextPage = mightContainTableInTheNextPage;
	}*/

	/*public boolean isExtraTexts() {
		return extraTexts;
	}*/
	
	public boolean containTable(){
		if(ti.size() == 0)
			return false;
		return true;
	}

	public void writeXML(Element pageRoot){
		/*Element tableInformation = pageRoot.addElement("TableInformation");
		for(int i = 0 ; i < ti.size(); i++)
			ti.get(i).writeToXML(tableInformation);*/
		//Element m = pageRoot.addElement("MightContainTableInTheNextPage");
		//m.addText(this.mightContainTableInTheNextPage+"");
		List<PageTextArea> pta = csp.getPTA();
		Element currentTable = null;
		int count = 0;
		for(int i = 0; i < pta.size(); i++){
			if(!pta.get(i).isReferenced()){
				if(!pta.get(i).isIsolated()){
					currentTable = pageRoot.addElement("Table");
					count++;
					currentTable.addAttribute("No.", count+"");
				}
			}
			if(currentTable != null){
				pta.get(i).writeToXML(currentTable);
			}
		}
	}
	
	public List<PageTextArea> getTableHeader(){
		List<PageTextArea> header = new ArrayList<PageTextArea>();
		List<PageTextArea> pta = csp.getPTA();
		for(int i = 0; i < pta.size(); i++){
			if(!pta.get(i).isReferenced()){
				if(!pta.get(i).isIsolated()){
					header.add(pta.get(i));
				}
			}
		}
		return header;
	}
	
	public boolean nextTable(PageStreamParser psp){
		TableInformation last = ti.get(ti.size()-1);
		TableInformation first = psp.getTi().get(0);
		if(last.getRowNumInTheLastRow() != first.getRowNumInTheFirstRow())
			return false;
		if(last.getRowNumInTheLastRow() > 1){
			float offset = last.getXofCellInTheLastRow(0)-first.getXofCellInTheFirstRow(0);
			for(int i = 1; i < last.getRowNumInTheLastRow(); i++){
				if(almostEqual(last.getXofCellInTheLastRow(i), first.getXofCellInTheFirstRow(i)+offset, 0.2) == false){
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean almostEqual(double a, double b, double err){
		if(Math.abs(a-b) <= err)
			return true;
		return false;
	}
	
	public void constructTable() throws IOException{
		csp.constructTable();
	}
	
	@SuppressWarnings("unchecked")
	public Map<Pair<Float,Float>,String> getRowHeaderMap(PageTextArea header){
		Map<Pair<Float,Float>,String> rowMap = new HashMap<Pair<Float,Float>, String>();
		//List<PageTextArea> header = this.getTableHeader();
		if(header!=null){
			PageTextArea currentHeader = header;
			rowMap.put(new Pair(new Float(currentHeader.getX()),
					new Float(currentHeader.getWidth())), 
					currentHeader.getString());
			if(currentHeader.getRight() != null){
				PageTextArea currentRight = currentHeader.getRight();
				while(currentRight != null){
					currentRight.recursivelyPutInRowHeader(rowMap, "");
					currentRight = currentRight.getRight();
				}
			}
		}
		return rowMap;
	}
	
	@SuppressWarnings("unchecked")
	public Map<Pair<Float,Float>,String> getColumnHeaderMap(PageTextArea header){
		Map<Pair<Float,Float>,String> columnMap = new HashMap<Pair<Float,Float>, String>();
		//List<PageTextArea> header = this.getTableHeader();
		if(header!=null){
			PageTextArea currentHeader = header;
			columnMap.put(new Pair(new Float(currentHeader.getY()),
					new Float(currentHeader.getHeight())), 
					currentHeader.getString());
			if(currentHeader.getDown() != null){
				PageTextArea currentDown = currentHeader.getDown();
				while(currentDown != null){
					currentDown.recursivelyPutInColumnHeader(columnMap, "");
					currentDown = currentDown.getDown();
				}
			}
		}
		return columnMap;
	}
	
	public void printRowAndColumnMap(){
		List<PageTextArea> headers = this.getTableHeader();
		if(headers.size()>0){
			for(int i = 0; i < headers.size(); i++){
				PageTextArea header = headers.get(i);
				System.out.println("Table "+i+":");
				Map<Pair<Float,Float>,String> rowMap = this.getRowHeaderMap(header);
				Iterator<Pair<Float,Float>> it1 = rowMap.keySet().iterator();
				while(it1.hasNext()){
					Pair<Float,Float> tmp = it1.next();
					System.out.println(tmp.getKey()+" "+tmp.getValue()+" "+rowMap.get(tmp));
				}
				System.out.println("---");
				Map<Pair<Float,Float>,String> columnMap = this.getColumnHeaderMap(header);
				Iterator<Pair<Float,Float>> it2 = columnMap.keySet().iterator();
				while(it2.hasNext()){
					Pair<Float,Float> tmp = it2.next();
					System.out.println(tmp.getKey()+" "+tmp.getValue()+" "+columnMap.get(tmp));
				}
				System.out.println("--------");
			}
			System.out.println("---------");
		}
	}
	
	public List<Map<String,String>> getRows(PageTextArea header){
		if(header == null)
			return null;
		if(header.isIsolated() == true)
			return null;
		if(header.isReferenced() == true)
			return null;
		Map<Pair<Float,Float>,String> columnMap = this.getColumnHeaderMap(header);
		Map<Pair<Float,Float>,String> rowMap = this.getRowHeaderMap(header);
		List<Map<String,String>> returnList = new ArrayList<Map<String, String>>();
		Iterator<Pair<Float,Float>> it = columnMap.keySet().iterator();
		while(it.hasNext()){
			Pair<Float,Float> tmp = it.next();
			Map<String,String> map = new HashMap<String,String>();
			map.put("行头", columnMap.get(tmp));
			returnList.add(map);
		}
		header.recursivelyPutInRowList(returnList, rowMap, columnMap);
		return returnList;
	}
	
	public List<Pair<String,PageTextArea>> getRowHeaders(PageTextArea header){
		if(header == null)
			return null;
		if(header.isIsolated() == true)
			return null;
		if(header.isReferenced() == true)
			return null;
		List<Pair<String,PageTextArea>> returnList = new ArrayList<Pair<String,PageTextArea>>();
		PageTextArea currentRowHeader = header;
		returnList.add(new Pair(header.getString(), header));
		while(currentRowHeader.getDown() != null){
			currentRowHeader = currentRowHeader.getDown();
			currentRowHeader.addToRowHeaderList(returnList, currentRowHeader.getString());
		}
		return returnList;
	}
	
	public void writeTableToXML(Element root){
		List<PageTextArea> headers = this.getTableHeader();
		if(headers.size()>0){
			for(int i = 0; i < headers.size(); i++){
				Element table = root.addElement("Table ");
				PageTextArea header = headers.get(i);
				table.addAttribute("PageNo", this.pageNo+"");
				List<Pair<String, PageTextArea>> list = this.getRowHeaders(header);
				if(list.size() > 0){
					for(int j = 0; j < list.size() ;j++){
						Element row = table.addElement("row");
						row.addAttribute("行头", list.get(j).getKey());
						PageTextArea rowIt = list.get(j).getValue();
						int count = 0;
						rowIt = rowIt.getRight();
						while(rowIt != null){
							row.addAttribute("第"+count+"列", rowIt.getString());
							rowIt = rowIt.getRight();
							count++;
							//System.out.println(s+" "+list.get(j).get(s));
						}
					}
				}
			}
		}
	}

	public void print() {
		// TODO Auto-generated method stub
		List<PageTextArea> pta = csp.getPTA();
		for( int i = 0; i < pta.size(); i++)
			pta.get(i).print();
		
	}
}
