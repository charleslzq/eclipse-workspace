package src.tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.util.Pair;

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
				if(op.getOperation().equals("W") || op.getOperation().equals("W*") || op.getOperation().equals("f")){
					//System.out.println(op.toString());
					if( tokens.get(i-1) instanceof PDFOperator){
						PDFOperator op2 = (PDFOperator) tokens.get(i-1);
						//System.out.println(((PDFOperator) tokens.get(i-1)).toString());
						if(op2.getOperation().equals("re")){
							RectangleObject ro = getRectangleObject(i);
							if(ro != null)
								rectangleMap.put(new Integer(i), ro);
						}
					}
				}
				/*else if(op.getOperation().equals("m")){
					RectangleObject ro = getRectangleObjectFromLines(i);
					if(ro != null)
						rectangleMap.put(new Integer(i), ro);
				}*/
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
						if(ro1.isInThisRegion(ro2))
							rectangleMap.remove(new Integer(j));
						if(ro2.isInThisRegion(ro1))
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
	
	private RectangleObject getRectangleObjectFromLines(int index) {
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
		//System.out.println(xs.size()+"");
		float x, y, width, height;
		ApproprixateComparison ac = new ApproprixateComparison(2,0.1);
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
					return new RectangleObject(x,y,width,height);
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
						//System.out.println(x+" "+y+" "+width+" "+height);
						return new RectangleObject(x,y,width,height);
					}
		}
		return null;
	}

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
		if( a[2] < 2 || a[3] < 2)
			return null;
		return new RectangleObject(a[0],a[1],a[2],a[3]);
	}
	
	public int getSize(){
		return tokens.size();
	}

}
