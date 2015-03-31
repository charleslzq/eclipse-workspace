package src.tool;

public class ApproprixateComparison {
	private int precision;
	private double err;
	
	public ApproprixateComparison(int p, double e){
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

}
