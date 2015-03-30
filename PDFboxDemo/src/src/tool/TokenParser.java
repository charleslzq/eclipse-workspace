package src.tool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.cos.COSArray;
import org.apache.pdfbox.cos.COSFloat;
import org.apache.pdfbox.cos.COSInteger;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.cos.COSString;
import org.apache.pdfbox.util.PDFOperator;

public class TokenParser {
	private List tokens;
	private Map<Integer, RectangleObject> areas;
	
	public TokenParser(List t){
		tokens = t;
		areas = this.rectangleParser();
	}
	
	public Map<Integer, RectangleObject> getAreas() {
		return areas;
	}


	public void print(){
		for(int i = 0 ; i < tokens.size(); i++)
			System.out.println(tokens.get(i).toString());
	}
	
	public Map<Integer, RectangleObject> rectangleParser(){
		Map<Integer, RectangleObject> rectangleMap= new HashMap<Integer, RectangleObject>();
		for(int i = 0 ; i < tokens.size(); i++){
			if( tokens.get(i) instanceof PDFOperator ){
				PDFOperator op = (PDFOperator) tokens.get(i);
				if(op.getOperation().equals("W") || op.getOperation().equals("W*")){
					System.out.println(op.toString());
					if( tokens.get(i-1) instanceof PDFOperator){
						PDFOperator op2 = (PDFOperator) tokens.get(i-1);
						//System.out.println(((PDFOperator) tokens.get(i-4)).toString());
						if(op2.getOperation().equals("re")){
							RectangleObject ro = getRectangleObject(i);
							rectangleMap.put(new Integer(i), ro);
						}
						else if(op2.getOperation().equals("h")){
							PDFOperator op3 = (PDFOperator) tokens.get(i-2);
							if(op3.getOperation().equals("re")){
								RectangleObject ro = getRectangleObject(i-1);
								rectangleMap.put(new Integer(i), ro);
							}
						}
					}
				}
			}
		}
		for(int i = 0; i < tokens.size(); i++){
			if(rectangleMap.containsKey(new Integer(i))){
				RectangleObject ro1 = rectangleMap.get(new Integer(i));
				for(int j = i+1 ; j < tokens.size() ; j++){
					if(rectangleMap.containsKey(new Integer(j))){
						RectangleObject ro2 = rectangleMap.get(new Integer(j));
						if(ro1.isSameArea(ro2))
							rectangleMap.remove(new Integer(i));
					}
				}
			}
		}
		return rectangleMap;
	}
	
	/*private TextObject getTextObject(int start, int end){
		TextObject to = new TextObject();
		for(int i = start+1; i < end ; i++){
			if( tokens.get(i) instanceof PDFOperator ){
				PDFOperator op = (PDFOperator) tokens.get(i);
				if(op.getOperation().equals("Tm"))
					setTextMatrix(to, i);
				else if(op.getOperation().equals("Tj"))
					setSingleString(to, i);
				else if(op.getOperation().equals("TJ"))
					setCompositeString(to, i);
			}
			else if( tokens.get(i) instanceof COSName){
				COSName fontName = (COSName) tokens.get(i);
				to.setFontName(fontName.getName());
			}
		}
		return to;
	}
	
	private void setTextMatrix(TextObject to, int index){
		int base = index - 6;
		float a[]={0,0,0,0,0,0};
		for(int i = 0; i < 6; i++)
			a[i] = getFloatValueFromCOS(tokens.get(i+base));
		to.setMatrix(a[0], a[1], a[2], a[3], a[4], a[5]);
	}*/
	
	private float getFloatValueFromCOS(Object o){
		if(o instanceof COSInteger)
			return ((COSInteger)o).floatValue();
		else if(o instanceof COSFloat)
			return ((COSFloat)o).floatValue();
		return (float) -9999.99999;
	}
	
	/*private void setSingleString(TextObject to, int index){
		COSString previous = (COSString) tokens.get(index - 1); 
		to.setString(previous);
	}
	
	private void setCompositeString(TextObject to, int index){
		COSArray previous = (COSArray) tokens.get(index - 1);
        to.addString(previous);
	}*/
	
	private RectangleObject getRectangleObject(int index){
		double a[] ={0,0,0,0};
		int base = index - 5;
		for(int i = 0; i < 4; i++)
			a[i] = getFloatValueFromCOS(tokens.get(base+i));
		return new RectangleObject(a[0],a[1],a[2],a[3]);
	}
	
	public int getSize(){
		return tokens.size();
	}

}
