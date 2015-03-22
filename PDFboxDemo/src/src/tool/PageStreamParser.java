package src.tool;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.scene.control.TextArea;

import org.apache.pdfbox.pdfparser.PDFStreamParser;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.util.PDFOperator;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class PageStreamParser {
	private int pageNo;
	private PDStream content;
	private TokenParser tp;
	private Map<Integer, RectangleObject> areas;
	private Map<Integer, TextObject> texts;
	private List<PageTextArea> pta;
	private int precision;
	
	public PageStreamParser(int a, PDPage pdp) throws IOException{
		pageNo = a;
		content = pdp.getContents();
		
		PDFStreamParser psp = new PDFStreamParser(content.getStream());
		psp.parse();
		tp = new TokenParser(psp.getTokens());
		
		areas = tp.rectangleParser();
		texts = tp.textParser();
		
		pta = PageAreaConstructor();
		
		precision = 2;
		
	}
	
	public PDStream getPDStream(){
		return content;
	}
	
	public void print(){
		System.out.println("Page "+pageNo+" :");
		tp.print();
		/*for(int i = 0; i < pta.size(); i++){
			System.out.print(i + ": ");
			pta.get(i).print();
		}*/
		/*ArrayList<ArrayList<Integer>> table = restoreTable();
		for(int i = 0; i < table.size(); i++){
			ArrayList<Integer> it = table.get(i);
			for(int j = 0; j < it.size(); j++){
				System.out.print(it.get(j) +"\t");
			}
			System.out.println();
		}*/
	}
	
	/*public ArrayList<ArrayList<Integer>> restoreTable(){
		ArrayList<ArrayList<Integer>> table = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> currentLine = new ArrayList<Integer>();
		for(int i = 0; i < pta.size(); i++){
			if(i == 0){
				currentLine.add(new Integer(pta.get(0).getAreaNo()));
				table.add(currentLine);
			}
			else{
				if( pta.get(i-1).isSameRow(pta.get(i)))
					currentLine.add(pta.get(i).getAreaNo());
				else{
					currentLine = new ArrayList<Integer>();
					currentLine.add(pta.get(i).getAreaNo());
					table.add(currentLine);
				}
			}
		}
		return table;
	}*/
	
	public List<PageTextArea> PageAreaConstructor(){
		List<PageTextArea> textAreas = new ArrayList<PageTextArea>();
		for(int i = 0; i < tp.getSize(); i++)
			if(areas.containsKey(new Integer(i)))
				textAreas.add(new PageTextArea(i, areas.get(new Integer(i))));
		for(int i = 0; i < textAreas.size(); i++)
			for(int j = 0; j < tp.getSize(); j++)
				if(texts.containsKey(new Integer(j)))
					if(textAreas.get(i).isInThisArea(texts.get(new Integer(j))))
						textAreas.get(i).putText(texts.get(new Integer(j)));
		for(int i = 0; i < textAreas.size(); i++)
			for(int j = i+1 ; j < textAreas.size(); j++){
				if( textAreas.get(i).getRight() == null && textAreas.get(i).nextColumnInTheSameRow(textAreas.get(j))){
					textAreas.get(i).setRight(textAreas.get(j));
					textAreas.get(j).setReferenced(true);
				}
				else if( textAreas.get(i).getDown() == null && textAreas.get(i).nextRowInTheSameColumn(textAreas.get(j))){
					textAreas.get(i).setDown(textAreas.get(j));
					textAreas.get(j).setReferenced(true);
				}
			}
		for(int i = 0; i < textAreas.size(); i++){
			PageTextArea current = textAreas.get(i);
			if( current.getRight() != null){
				double err = 0.1;
				int n = 2;
				if(alreadyEqual(current.getHeight(), current.getRight().getHeight(), err))
					current.getRight().setSplitedRows(current.getSplitedRows());
				else{
					double sum = current.getRight().getHeight()/current.getHeight();
					current.getRight().setSplitedRows(round(current.getSplitedRows()*sum,n));
					if( sum < 1){
						PageTextArea next = current.getRight();
						while( sum < 1){
							if(next.getDown() != null){
								double tmp = next.getDown().getHeight()/current.getHeight();
								sum += tmp;
								if(alreadyEqual(sum,1,Math.exp(-(n-1)))){
									next.getDown().setSplitedRows(round(1-sum+tmp,n)*current.getSplitedRows());
									break;
								}
								next.getDown().setSplitedRows(round(tmp*current.getSplitedRows(),n));
								next = next.getDown();
							}
							else
								break;
						}
					}
				}
			}
			if( current.getDown() != null){
				double err = 0.1;
				int n = 2;
				if(alreadyEqual(current.getWidth(), current.getDown().getWidth(), err))
					current.getDown().setSplitedCols(current.getSplitedCols());
				else{
					double sum = current.getDown().getWidth()/current.getWidth();
					current.getDown().setSplitedCols(round(current.getSplitedCols()*sum,n));
					if(sum < 1){
						PageTextArea next = current.getDown();
						while(sum < 1){
							if(next.getRight() != null){
								double tmp = next.getRight().getWidth()/current.getWidth();
								sum += tmp;
								if(alreadyEqual(sum, 1, Math.exp(-(n-1)))){
									next.getRight().setSplitedCols(round(1-sum+tmp,n)*current.getSplitedCols());
									break;
								}
								next.getRight().setSplitedCols(round(tmp*current.getSplitedCols(),n));
								next = next.getRight();
							}
							else
								break;
						}
					}
				}
			}
		}
			/*PageTextArea current = textAreas.get(i);
			if( current.getRight() != null)
				if( current.getHeight() > current.getRight().getHeight() ){
					PageTextArea tmp = current.getRight();
					int rows = 1;
					double sum = tmp.getHeight();
					while( tmp.getDown() != null &&sum + tmp.getDown().getHeight() < current.getHeight() ){
						sum += tmp.getDown().getHeight();
						tmp = tmp.getDown();
						rows += tmp.getSplitedRows();
					}
					current.setSplitedRows(rows);
				}
				else
					current.setSplitedRows(current.getRight().getSplitedRows());//current.getRight().getSplitedRows());
			else
				current.setSplitedRows(1);
			if( current.getDown() != null)
				if( current.getWidth() > current.getDown().getWidth() ){
					PageTextArea tmp = current.getDown();
					int cols = 1;
					double sum = tmp.getWidth();
					while( tmp.getRight() != null && sum + tmp.getRight().getWidth() < current.getWidth() ){
						sum += tmp.getRight().getWidth();
						tmp = tmp.getRight();
						cols += tmp.getSplitedCols();
					}
					current.setSplitedCols(cols);
				}
				else
					current.setSplitedCols(current.getDown().getSplitedCols());
			else
				current.setSplitedCols(1);
		}*/
		int col = 0;
		int row = 0;
		for(int i = 0; i < textAreas.size() ; i++){
			if(textAreas.get(i).nullColRow()){
				textAreas.get(i).recursiveSetColAndRowNo(col, row, 2);
				if(textAreas.get(i).isIsolated() == true)
					//row += textAreas.get(i).getSplitedRows();
				//else
					row = 0;
			}
		}
		return textAreas;
	}
	
	/*public void test(){
			areas.get(new Integer(100122)).print();
			areas.get(new Integer(100239)).print();
			areas.get(new Integer(100302)).print();
			areas.get(new Integer(100821)).print();
			areas.get(new Integer(100888)).print();
	}*/
	
	public void writeXML(Element pageRoot){
		//int currentRow = 0;
		Element currentTable = null;
		int count = 0;
		//System.out.println("Page :"+pageNo);
		for(int i = 0; i < pta.size(); i++){
			/*if(pta.get(i).getRowNo() != currentRow || currentLine == null){
				currentLine = pageRoot.addElement("Row");
				currentRow = pta.get(i).getRowNo();
				currentLine.addAttribute("Num", currentRow + "");
			}*/
			if(!pta.get(i).isReferenced()){
				if(!pta.get(i).isIsolated()){
					currentTable = pageRoot.addElement("Table");
					count++;
					currentTable.addAttribute("No.", count+"");
					//System.out.println("Head: "+pta.get(i).getAreaNo());
				}
			}
			if(currentTable != null){
				Element tmp = currentTable.addElement("Cell");
				tmp.addAttribute("No", pta.get(i).getAreaNo()+"");
				Element areaPosition = tmp.addElement("AreaPosition");
				Element xOfLeftUpper = areaPosition.addElement("XofLeftUpper");
				xOfLeftUpper.addText(pta.get(i).getX()+"");
				Element yOfLeftUpper = areaPosition.addElement("YofLeftUpper");
				yOfLeftUpper.addText(pta.get(i).getY()+"");
				Element width = areaPosition.addElement("Width");
				width.addText(pta.get(i).getWidth()+"");
				Element height = areaPosition.addElement("Height");
				height.addText(pta.get(i).getHeight()+"");
				Element cellInformation = tmp.addElement("CellInformation");
				Element splitedRows = cellInformation.addElement("SplitedRows");
				Element splitedCols = cellInformation.addElement("SplitedColumns");
				splitedRows.addText(pta.get(i).getSplitedRows()+"");
				splitedCols.addText(pta.get(i).getSplitedCols()+"");
				Element rowNo = cellInformation.addElement("RowNo");
				rowNo.addText(pta.get(i).getRowNo()+"");
				Element colNo = cellInformation.addElement("ColumnNo");
				colNo.addText(pta.get(i).getColNo()+"");
				/*Element rowNo = tmp.addElement("Row No");
				rowNo.addText(pta.get(i).getRowNo()+"");
				Element colNo = tmp.addElement("Column No");
				colNo.addText(pta.get(i).getColNo()+"");
				Element sRow = tmp.addElement("Splited Rows");
				sRow.addText(pta.get(i).getSplitedRows()+"");
				Element sCol = tmp.addElement("Splited Columns");
				sCol.addText(pta.get(i).getSplitedCols()+"");*/
				Element links = tmp.addElement("Links");
				Element rightLink = links.addElement("NextCellInTheSameRow");
				if(pta.get(i).getRight()!=null)
					rightLink.addText(pta.get(i).getRight().getAreaNo()+"");
				Element downLink = links.addElement("NextCellInTheSameColumn");
				if(pta.get(i).getDown()!=null)
					downLink.addText(pta.get(i).getDown().getAreaNo()+"");
				if(pta.get(i).nullText() == false)
					for(int j = 0; j < pta.get(i).textSize(); j++){
						TextObject to = pta.get(i).getText(j);
						Element text = tmp.addElement("Text");
						Element content = text.addElement("Content");
						content.addText(to.getString());
						Element position = text.addElement("TextPosition");
						Element x = position.addElement("X");
						x.addText(to.getX()+"");
						Element y = position.addElement("Y");
						y.addText(to.getY()+"");
					}
				//tmp.addText("Column :" + (new Integer(pta.get(i).getColNo())).toString());
			}
		}
		//System.out.println();
	}
	
	public double round(double a, int n){
		BigDecimal b = new BigDecimal(a);
		double f = b.setScale(n, BigDecimal.ROUND_HALF_UP).doubleValue();
		return f;
	}
	
	public boolean alreadyEqual(double a, double b, double err){
		if(Math.abs(a-b) <= err)
			return true;
		return false;
	}

}
