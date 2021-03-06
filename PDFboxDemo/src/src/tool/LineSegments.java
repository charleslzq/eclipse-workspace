package src.tool;

import java.util.ArrayList;
import java.util.List;


/**
 * This class stores the projections of text areas on a vertical or a horizontal line.
 * @author zqliu
 * @version 1.0
 */
public class LineSegments {
	boolean vertical;
	double startPoint;
	double endPoint;
	List<Double> positions;
	
	/**
	 * The type of this class (horizontal or vertical) must be specified when it is initialized.
	 * @param v true, if want to project areas on a vertical line.
	 * @param sp The value that will be assigned to start point.
	 * @param ep The value that will be assigned to end point.
	 */
	public LineSegments(boolean v, double sp, double ep){
		vertical = v;
		startPoint = sp;
		endPoint = ep;
		positions = new ArrayList<Double>();
	}

	/**
	 * Get method of vertical
	 * @return true, if this class is constructed for a vertical line.
	 */
	public boolean isVertical() {
		return vertical;
	}

	/**
	 * Set the start point of this line.
	 * @param startPoint The value of the start point. Min X or Max Y
	 */
	public void setStartPoint(double startPoint) {
		this.startPoint = startPoint;
	}

	/**
	 * Set the end point of this line.
	 * @param endPoint The value of the end point. Max X or Min Y.
	 */
	public void setEndPoint(double endPoint) {
		this.endPoint = endPoint;
	}
	
	/**
	 * Add a new position into the list. 
	 * Ensure that this value is between 
	 * last inserted position( or the 
	 * start point) and end point. 
	 * blank,  
	 * @param d The point want to insert.
	 */
	public void addPosition(double d){
		double a;
		if(positions.size() == 0 )
			a = startPoint;
		else
			a = positions.get(positions.size()-1);
		if(vertical == false){
			if(a < d 
					&& d < endPoint)
				positions.add(new Double(d));
		}
		else
			if(a > d
					&& d > endPoint)
				positions.add(new Double(d));
	}
	
	/**
	 * This method will return the positions 
	 * of midpoints of those line segments that are blank,
	 * which means there is no area that 
	 * will be projected to this segment.
	 * Usually the line formed by the start point and
	 * the first element of the list will be blank. But 
	 * there is the possibility that they are equal.
	 * @return
	 */
	public List<Double> getLine(){
		List<Double> returnList = new ArrayList<Double>();
		if( startPoint != positions.get(0))
			returnList.add(new Double((startPoint+positions.get(0))/2));
		for(int i=1; i<positions.size()-2; i+=2){
			returnList.add(new Double((positions.get(i)+positions.get(i+1))/2));
		}
		return returnList;
	}
	
	
	/**
	 * This method will return the last inserted number.
	 * @return The last inserted number if there is one. Return start point otherwise.
	 */
	public double getLast(){
		if(positions.size() == 0)
			return startPoint;
		return positions.get(positions.size()-1);
	}
	
	
	/**
	 * This method would set the last value of the list to the new value.
	 * @param d
	 */
	public void replace(double d){
		positions.set(positions.size()-1, new Double(d));
	}
	
	/**
	 * test output
	 */
	public void print(){
		System.out.println("Start:"+startPoint
				+";End:"+endPoint+";"+positions.size());
		for(int i=0; i<positions.size()-1; i+=2)
			System.out.println(positions.get(i)+";"+positions.get(i+1));
	}
}
