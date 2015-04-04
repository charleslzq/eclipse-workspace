package src.tool;

import java.math.BigDecimal;

public class ApproximateCalculation {
	private int precision;
	private double err;
	
	public ApproximateCalculation(int p, double e){
		precision = p;
		err = e;
	}

	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public double getErr() {
		return err;
	}

	public void setErr(double err) {
		this.err = err;
	}
	
	public boolean alreadyEqual(double a, double b){
		if(Math.abs(a-b) <= err)
			return true;
		return false;
	}
	
	public boolean approximateLess(double a, double b){
		if(a < b + err )
			return true;
		return false;
	}
	
	public boolean approximateMore(double a, double b){
		if(a+err > b)
			return true;
		return false;
	}
	
	public boolean approximateMoreEqual(double a, double b){
		if( a +err >= b )
			return true;
		return false;
	}
	
	public boolean approximateLessEqual(double a, double b){
		if( a <= b + err)
			return true;
		return false;
	}
	
	public double round(double a, int n){
		BigDecimal b = new BigDecimal(a);
		double f = b.setScale(n, BigDecimal.ROUND_HALF_UP).doubleValue();
		return f;
	}

}
