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
	private Map<Integer, TextObject> texts;
	
	public TokenParser(List t){
		tokens = t;
		areas = this.rectangleParser();
		texts = this.textParser();
	}
	
	public Map<Integer, RectangleObject> getAreas() {
		return areas;
	}

	public Map<Integer, TextObject> getTexts() {
		return texts;
	}

	public void print(){
		for(int i = 0 ; i < tokens.size(); i++)
			System.out.println(tokens.get(i).toString());
		Map<Integer, TextObject> textMap = textParser();
		Map<Integer, RectangleObject> rectangleMap = rectangleParser();
		for(int i = 0; i < tokens.size() ; i++){
			if(textMap.containsKey(new Integer(i))){
				System.out.println("Text at token "+i);
				textMap.get(new Integer(i)).print();
			}
			else if(rectangleMap.containsKey(new Integer(i))){
				System.out.println("Rectangle at token "+i);
				rectangleMap.get(new Integer(i)).print();
				if(textMap.containsKey(new Integer(i+2)))
					System.out.println("Here is a cell!");
			}
		}
	}
	
	public Map<Integer, TextObject> textParser(){
		int start = 0;
		Map<Integer, TextObject> textMap= new HashMap<Integer, TextObject>();
		for(int i = 0 ; i < tokens.size(); i++){
			if( tokens.get(i) instanceof PDFOperator ){
				PDFOperator op = (PDFOperator) tokens.get(i);
				if( op.getOperation().equals("BT"))
					start = i;
				else if( op.getOperation().equals("ET")){
					TextObject to = getTextObject(start, i);
					//if(to != null)
					//to.print();
					textMap.put(new Integer(start), to);
				}
				/*else if(op.getOperation().equals("f*") || op.getOperation().equals("W*")){
					if( tokens.get(i-1) instanceof PDFOperator){
						PDFOperator op2 = (PDFOperator) tokens.get(i-1);
						if(op2.getOperation().equals("re")){
							RectangleObject ro = getRectangleObject(i);
							ro.print();
						}
					}
				}*/
			}
		}
		return textMap;
	}
	
	public Map<Integer, RectangleObject> rectangleParser(){
		Map<Integer, RectangleObject> rectangleMap= new HashMap<Integer, RectangleObject>();
		for(int i = 0 ; i < tokens.size(); i++){
			if( tokens.get(i) instanceof PDFOperator ){
				PDFOperator op = (PDFOperator) tokens.get(i);
				if(/*op.getOperation().equals("f*") || */op.getOperation().equals("W*")){
					if( tokens.get(i-1) instanceof PDFOperator){
						PDFOperator op2 = (PDFOperator) tokens.get(i-1);
						if(op2.getOperation().equals("re")){
							RectangleObject ro = getRectangleObject(i);
							rectangleMap.put(new Integer(i), ro);
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
	
	private TextObject getTextObject(int start, int end){
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
	}
	
	private float getFloatValueFromCOS(Object o){
		if(o instanceof COSInteger)
			return ((COSInteger)o).floatValue();
		else if(o instanceof COSFloat)
			return ((COSFloat)o).floatValue();
		return (float) -9999.99999;
	}
	
	private void setSingleString(TextObject to, int index){
		COSString previous = (COSString) tokens.get(index - 1); 
		to.setString(previous);
	}
	
	private void setCompositeString(TextObject to, int index){
		COSArray previous = (COSArray) tokens.get(index - 1);
        to.addString(previous);
	}
	
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
