package src.processing;

import java.io.FileOutputStream;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import src.interfaces.XMLWriterInterface;


/**
 * This class implements some basic methods about writing something into a XML file.
 * @author zqliu
 * @version 1.0
 */
public class BasicXMLWriter implements XMLWriterInterface {
	Document xml;
	String path;
	
	
	/**
	 * Use setup to initialize this class.
	 * @param p The file path.
	 */
	public BasicXMLWriter(String p){
		setup(p);
	}
	
	@Override
	public Document setup(String p){
		xml = DocumentHelper.createDocument();
		path = p;
		return xml;
	}
	
	@Override
	public void addAttribute(Element root, String attributeName, String text){
		root.addAttribute(attributeName, text);
	}
	
	@Override
	public void write() throws Exception{
		FileOutputStream fos = new FileOutputStream(path);
		OutputFormat outformat = OutputFormat.createPrettyPrint();
	   //outformat.setEncoding(aEncodingScheme);
	   XMLWriter writer = new XMLWriter(fos, outformat);
	   writer.write(xml);
	   writer.flush();
	}
}
