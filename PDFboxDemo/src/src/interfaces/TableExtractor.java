package src.interfaces;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDPage;

import src.tool.PDFTable;
import src.tool.PageTextArea;

/**
 * This interface should be implemented in those classes handling the extraction of tables from a page of PDF file.
 * @author zqliu
 * @version 1.0
 */
public interface TableExtractor {
	
	/**
	 * This method is supposed to remove useless regions
	 * like overlapped rectangles.
	 * @param areas The list of rectangles.
	 */
	public void removeUselessRegions(List<PageTextArea> areas);
	
	/**
	 * This method is supposed to find the text content for
	 * each cell.
	 * @param areas The list of rectangles representing cells.
	 * @throws Exception
	 */
	public void attachTexts(List<PageTextArea> areas) throws Exception ;
	
	/**
	 * This method should build connections between rectangles. 
	 * A rectangle has two links, one links to the rectangle next
	 * to it on the right, the other one links to the rectangle
	 * considered to be next to it downside.
	 * @param areas The list of sorted rectangles.
	 */
	public void buildConnectionsBetweenRegions(List<PageTextArea> areas);
	
	/**
	 * This method is supposed to use other methods
	 * in this interface to generate tables in the
	 * PDF page. It will also set the internal 
	 * variable represent the processed page, 
	 * if there is one, to variable "page".
	 * @param page The page that would be processed.
	 * @return A list of tables this PDF page may contain.
	 * @throws Exception
	 */
	public List<PDFTable> getTables(PDPage page) throws Exception;
	
	/**
	 * This method should find out the first cell for each
	 * table in a list of rectangles. These rectangles should
	 * be sorted and their connections should be built before
	 * this method is called. 
	 * @param areas The list of sorted and connected rectangles
	 * @return The list of cells, each of which is the first cell of a table in this PDF page.
	 */
	public List<PageTextArea> getTableHeader(List<PageTextArea> areas);
	
	/**
	 * This method should extract the entire table from 
	 * the first cell of this table. The cells in this 
	 * table are connected so through the first cell, it
	 * is possible to get all the cells.
	 * @param header The first cell of the table
	 * @return The list of all the rectangles in this table.
	 */
	public PDFTable getTableFromHeader(PageTextArea header);
}
