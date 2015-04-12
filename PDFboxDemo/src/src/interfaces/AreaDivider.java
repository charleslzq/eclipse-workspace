package src.interfaces;

import java.util.List;

import src.tool.LineSegments;
import src.tool.PDFRectangle;
import src.tool.PageTextArea;

/**
 * This interface provides methods to divide the page based on the texts and their positions extracted.
 * @author zqliu
 * @version 1.0
 */
public interface AreaDivider {
	
	/**
	 * This method will find the projection of a list of areas on 
	 * a vertical line or a horizontal line.
	 * @param areas The list of areas that will be processed. Should be sorted before.
	 * @param v Indicate what kind of LineSegments want to construct. True indicates vertical.
	 * @return The constructed LineSegment.
	 */
	public LineSegments project(List<PDFRectangle> areas, 
			double start, 
			double end, 
			boolean v);
	
	/**
	 * This method will divide a rectangle by a vertical line or a horizontal line.
	 * @param pr The rectangle that will be splitted.
	 * @param value The value of the line, x if the line is vertical, y if the line is horizontal.
	 * @param v The type of this line, true if the line is vertical.
	 * @return The list of rectangles that is generated, the size can only be 0,1,2.
	 */
	public List<PDFRectangle> divideArea(PDFRectangle pr, 
			double value, 
			boolean v);
	
	
	/**
	 * This method will count the number of areas that is contained by a rectangle.
	 * @param areas The list of areas that might be in the rectangle.
	 * @param rect The rectangle.
	 * @return The number of areas contained by this rectangle in this list.
	 */
	public int countAreas(List<PDFRectangle> areas, 
			PDFRectangle rect);
	
	
}
