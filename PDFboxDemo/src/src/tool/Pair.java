package src.tool;

public class Pair {
	private String content;
	private PageTextArea pta;
	
	public Pair(String s, PageTextArea p){
		content = s;
		pta = p;
	}

	public String getContent() {
		return content;
	}

	public PageTextArea getPta() {
		return pta;
	}
	
	

}
