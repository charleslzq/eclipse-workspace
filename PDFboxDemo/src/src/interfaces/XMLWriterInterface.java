/**
 * 
 */
package src.interfaces;

import java.io.FileNotFoundException;

import org.dom4j.Document;
import org.dom4j.Element;

/** This interface will contain the methods to write tables into XML files.
 * @author zqliu
 * @version 1.0
 */
public interface XMLWriterInterface {
	/**
	 * This method will setup a XML file to output.
	 * @param path The path of the XML file.
	 * @return The XMl file.
	 */
	public Document setup(String path);
	
	/**
	 * This method would add an attribute to the element root.
	 * @param root The element that the attribute will be added on.
	 * @param attributeName The name of attribute.
	 * @param text The content of the attribute.
	 */
	public void addAttribute(Element root, String attributeName, String text);
	
	/**
	 * This method executes the job of writing.
	 * @throws FileNotFoundException 
	 * @throws Exception 
	 */
	public void write() throws Exception;
}
