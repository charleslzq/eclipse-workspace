package src.tool;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDPage;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class PageParser {
	private List<PDPage> pages;
	private List<ContentStreamParser> csp;
	private Document xml;
	
	@SuppressWarnings("unchecked")
	public PageParser(BufferedInputStream fis) throws IOException{
		PDFParser parser = new PDFParser(fis);
		parser.parse();
		
		xml = DocumentHelper.createDocument();
		Element root = xml.addElement("root");
		
		pages = parser.getPDDocument().getDocumentCatalog().getAllPages();
		csp = new ArrayList<ContentStreamParser>();
		for(int i = 0; i < pages.size(); i++){
			if(pages.get(i) != null){
				if(pages.get(i).getContents() != null){
					csp.add(new ContentStreamParser(i+1,pages.get(i)));
				}
			}
		}
		
		for(int i=0; i < csp.size() ; i++){
			csp.get(i).parse();
			csp.get(i).writeLineTableToXML(root);
			csp.get(i).writeCellTableToXML(root);
			//csp.get(i).writeXML(root);
		}
		
	}
	
	public void output(FileOutputStream fos) throws Exception{
		serializetoXML(fos);
	}
	
	 public void serializetoXML(OutputStream out) throws Exception {
		   OutputFormat outformat = OutputFormat.createPrettyPrint();
		   //outformat.setEncoding(aEncodingScheme);
		   XMLWriter writer = new XMLWriter(out, outformat);
		   writer.write(xml);
		   writer.flush();
	}
}
