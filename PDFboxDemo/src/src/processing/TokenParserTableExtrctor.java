package src.processing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.cos.COSFloat;
import org.apache.pdfbox.cos.COSInteger;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.util.PDFOperator;

import src.tool.ApproximateCalculation;
import src.tool.PDFRectangle;

/**
 * This abstract class should be the super class of any class representing a method to extract tables from a PDF page
 * by parsing the PDF stream for rectangles. Some general methods such as rectangleParser are implemented.
 * @author zqliu
 * @version 1.0
 */

public abstract class TokenParserTableExtrctor extends BasicTableExtractor {
	
	public List getTokens(PDPage page) throws IOException{
		return page.getContents().getStream().getStreamTokens();
	}
	
	/**
	 * This method tries to extract rectangles from
	 * a list of tokens generated from a PDF page.
	 * @param tokens The list of tokens.
	 * @return A hash map contains all the rectangles and their corresponding keys, which are the indexes of their operators "re" or "m" in the token list.
	 */
	protected Map<Integer, PDFRectangle> rectangleParser(List tokens){
		Map<Integer, PDFRectangle> rectangleMap = new HashMap<Integer, PDFRectangle>();
		PDFRectangle.setThreshold(3);
		PDFRectangle.setBlank(9.0);
		for(int i = 0 ; i < tokens.size(); i++){
			if( tokens.get(i) instanceof PDFOperator ){
				PDFOperator op = (PDFOperator) tokens.get(i);
				if(op.getOperation().equals("re")){
					PDFRectangle ro = getPDFRectangle(tokens, i+1);
					if(ro != null && ro.isLegal() == true)
						rectangleMap.put(new Integer(i), ro);
				}
				else if(op.getOperation().equals("m")){
					PDFRectangle ro = getPDFRectangleFromLines(tokens, i);
					if(ro != null && ro.isLegal() == true)
						rectangleMap.put(new Integer(i), ro);
				}
			}
		}
		for(int i = 0; i < tokens.size(); i++){
			if(rectangleMap.containsKey(new Integer(i))){
				PDFRectangle ro1 = rectangleMap.get(new Integer(i));
				for(int j = i+1 ; j < tokens.size() ; j++){
					if(rectangleMap.containsKey(new Integer(j))){
						PDFRectangle ro2 = rectangleMap.get(new Integer(j));
						if(ro1.isSameRectangle(ro2)){
							rectangleMap.remove(new Integer(i));
							continue;
						}
					}
				}
			}
		}
		return rectangleMap;
	}
	
	/**
	 * This method gets a float value from a COS object
	 * in a PDF stream.
	 * @param o A COS object, should be COSInteger or COSFloat
	 * @return the value the object represents. Return -9999.9999 if the input is out of expectation.
	 */
	private float getFloatValueFromCOS(Object o){
		if(o instanceof COSInteger)
			return ((COSInteger)o).floatValue();
		else if(o instanceof COSFloat)
			return ((COSFloat)o).floatValue();
		return (float) -9999.99999;
	}
	
	/**
	 * This method generates a rectangle 
	 * by parsing the "re" operator.
	 * @param tokens The list of tokens.
	 * @param index The index of "re" in the token list.
	 * @return The rectangle this "re" operator depicts.
	 */
	private PDFRectangle getPDFRectangle(List tokens, int index){
		double a[] ={0,0,0,0};
		int base = index - 5;
		for(int i = 0; i < 4; i++)
			a[i] = getFloatValueFromCOS(tokens.get(base+i));
		return new PDFRectangle(a[0],a[1],a[2],a[3]);
	}
	
	/**
	 * This method generates a rectangle
	 * by parsing a set of "m/l/h" operators
	 * @param tokens The list of tokens.
	 * @param index The index of "m" in the token list.
	 * @return The rectangle this set of operators depicts.
	 */
	private PDFRectangle getPDFRectangleFromLines(List tokens, int index) {
		// TODO Auto-generated method stub
		int p = index;
		List<Float> xs = new ArrayList<Float>();
		List<Float> ys = new ArrayList<Float>();
		xs.add(new Float(this.getFloatValueFromCOS(tokens.get(p-2))));
		ys.add(new Float(this.getFloatValueFromCOS(tokens.get(p-1))));
		p = p+3;
		while(p < tokens.size()){
			if( tokens.get(p) instanceof PDFOperator){
				PDFOperator op = (PDFOperator) tokens.get(p);
				if( op.getOperation().equals("m") || op.getOperation().equals("h") )
					break;
				else if(op.getOperation().equals("l")){
					xs.add(new Float(this.getFloatValueFromCOS(tokens.get(p-2))));
					ys.add(new Float(this.getFloatValueFromCOS(tokens.get(p-1))));
				}
			}
			p = p+3;
		}
		float x, y, width, height;
		ApproximateCalculation ac = new ApproximateCalculation(0.1);
		if(p < tokens.size() && xs.size() >= 4){
			if(ac.alreadyEqual(xs.get(0), xs.get(1)) && ac.alreadyEqual(xs.get(2), xs.get(3)))
				if( ac.alreadyEqual(ys.get(0), ys.get(3)) && ac.alreadyEqual(ys.get(1), ys.get(2))){
					if(xs.get(0) < xs.get(2)){
						x = xs.get(0);
						width = xs.get(2) - xs.get(0);
					}
					else{
						x = xs.get(2);
						width = xs.get(0) - xs.get(2);
					}
					if( ys.get(0) < ys.get(2)){
						y = ys.get(0);
						height = ys.get(2) - ys.get(0);
					}
					else{
						y = ys.get(2);
						height = ys.get(0) - ys.get(2);
					}
					//System.out.println(x+" "+y+" "+width+" "+height);
					return new PDFRectangle(x,y,width,height);
				}
				else if(ac.alreadyEqual(xs.get(0), xs.get(3)) && ac.alreadyEqual(xs.get(1), xs.get(2)))
					if( ac.alreadyEqual(ys.get(0), ys.get(1)) && ac.alreadyEqual(ys.get(2), ys.get(3))){
						if(xs.get(0) < xs.get(2)){
							x = xs.get(0);
							width = xs.get(2) - xs.get(0);
						}
						else{
							x = xs.get(2);
							width = xs.get(0) - xs.get(2);
						}
						if( ys.get(0) < ys.get(2)){
							y = ys.get(0);
							height = ys.get(2) - ys.get(0);
						}
						else{
							y = ys.get(2);
							height = ys.get(0) - ys.get(2);
						}
						return new PDFRectangle(x,y,width,height);
					}
		}
		return null;
	}

}
