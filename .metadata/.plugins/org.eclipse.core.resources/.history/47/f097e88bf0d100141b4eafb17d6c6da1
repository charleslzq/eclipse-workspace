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
import org.apache.pdfbox.util.PDFTextStripperByArea;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class PageStreamParser {
	private int pageNo;
	private PDStream content;
	private ContentStreamParser csp;
	
	public PageStreamParser(int a, PDPage pdp) throws IOException{
		pageNo = a;
		content = pdp.getContents();

		csp = new ContentStreamParser(pdp);
	}
	
	public PDStream getPDStream(){
		return content;
	}
	
	public void writeXML(Element pageRoot){
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
				Element cellInformation = tmp.addElement("SimplifiedPosition");
				Element splitedRows = cellInformation.addElement("SimplifiedHeight");
				Element splitedCols = cellInformation.addElement("SimplifiedWidth");
				splitedRows.addText(pta.get(i).getSplitedRows()+"");
				splitedCols.addText(pta.get(i).getSplitedCols()+"");
				Element rowNo = cellInformation.addElement("SimplifedY");
				rowNo.addText(pta.get(i).getRowNo()+"");
				Element colNo = cellInformation.addElement("SimplifedX");
				colNo.addText(pta.get(i).getColNo()+"");
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
			}
		}
	}
	
	

}
