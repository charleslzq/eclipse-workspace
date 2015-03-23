package src.tool;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
	private List<PageStreamParser> psp;
	private Document xml;
	
	public PageParser(FileInputStream fis) throws IOException{
		PDFParser parser = new PDFParser(fis);
		parser.parse();
		
		List<PDPage> pages = parser.getPDDocument().getDocumentCatalog().getAllPages();
		psp = new ArrayList<PageStreamParser>();
		
		for(int i = 0; i < pages.size(); i++){
			psp.add(new PageStreamParser(i, pages.get(i)));
		}
		
		xml = DocumentHelper.createDocument();
	}
	
	public void output(FileOutputStream fos) throws Exception{
		//for(int i = 0; i < psp.size(); i++)
			//bw.write(psp.get(i).getPDStream().getInputStreamAsString());
		//OutputStream os = new FileOutputStream("456789.xml");
		serializetoXML(fos);
		//bw.write(xml.asXML());
	}
	
	public void xmlBuilder(){
		Element root = xml.addElement("root");
		for(int i = 0; i < psp.size(); i++){
			Element page = root.addElement("Page"+(new Integer(i)).toString());
			psp.get(i).writeXML(page);
		}
	}
	
	 public void serializetoXML(OutputStream out) throws Exception {
		   OutputFormat outformat = OutputFormat.createPrettyPrint();
		   //outformat.setEncoding(aEncodingScheme);
		   XMLWriter writer = new XMLWriter(out, outformat);
		   writer.write(xml);
		   writer.flush();
		 }
}
