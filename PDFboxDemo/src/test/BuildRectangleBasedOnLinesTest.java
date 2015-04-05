package test;

import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDPage;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.junit.Before;
import org.junit.Test;

import src.tool.ContentStreamParser;
import src.tool.PDFCharacter;
import src.tool.PDFCharacterBag;
import src.tool.PDFRectangle;
import src.tool.PageTextArea;
import src.tool.TableInformation;
import src.tool.TextStripperByPDFRectangle;
import src.tool.TokenParser;

public class BuildRectangleBasedOnLinesTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGetPDFRectangleFromLines() throws IOException {
		fail("Not yet implemented");
		FileInputStream fis = new FileInputStream("000063_2014_n.pdf");
   		BufferedInputStream bis = new BufferedInputStream(fis);
   		
   		
   		PDFParser parser = new PDFParser(fis);
		parser.parse();
		
		List<PDPage> pages = parser.getPDDocument().getDocumentCatalog().getAllPages();
		int index = 15;
		PDFRectangle.setThreshold(3);
		ContentStreamParser csp = new ContentStreamParser(index+1,pages.get(index));
		TokenParser tp = new TokenParser(pages.get(index).getContents().getStream().getStreamTokens());
		Map<Integer, PDFRectangle> areas = tp.rectangleParser();
	
		List<PDFRectangle> lines = tp.getVerticalLines(areas);
	}

	@Test
	public void testGetVerticalLines() throws IOException {
		fail("Not yet implemented");
		FileInputStream fis = new FileInputStream("000063_2014_n.pdf");
   		BufferedInputStream bis = new BufferedInputStream(fis);
   		
   		
   		PDFParser parser = new PDFParser(fis);
		parser.parse();
		
		List<PDPage> pages = parser.getPDDocument().getDocumentCatalog().getAllPages();
		int index = 15;
		PDFRectangle.setThreshold(3);
		ContentStreamParser csp = new ContentStreamParser(index+1,pages.get(index));
		TokenParser tp = new TokenParser(pages.get(index).getContents().getStream().getStreamTokens());
		Map<Integer, PDFRectangle> areas = tp.rectangleParser();
	
		List<PDFRectangle> lines = tp.getVerticalLines(areas);
		
		for(int i=0; i<lines.size(); i++){
			lines.get(i).print();
		}
	}

	@Test
	public void testBuildAreaFromLines() throws IOException {
		//fail("Not yet implemented");
		FileInputStream fis = new FileInputStream("000039_2014_n.pdf");
   		BufferedInputStream bis = new BufferedInputStream(fis);
   		
   		
   		PDFParser parser = new PDFParser(fis);
		parser.parse();
		
		List<PDPage> pages = parser.getPDDocument().getDocumentCatalog().getAllPages();
		int index = 82;
		PDFRectangle.setThreshold(3);
		ContentStreamParser csp = new ContentStreamParser(index+1,pages.get(index));
		List<PDFCharacter> chars = csp.getAllCharacters();
		List<PDFCharacterBag> bags = csp.formBags(chars);
		for(int i=0; i<bags.size();i++){
			System.out.println(bags.get(i).getString());
		}
		/*TokenParser tp = new TokenParser(pages.get(index).getContents().getStream().getStreamTokens());
		Map<Integer, PDFRectangle> as = tp.rectangleParser();
	
		List<PDFRectangle> rects = tp.buildAreaFromLines(as);
		List<PageTextArea> areas = new ArrayList<PageTextArea>();
		
		for(int i=0; i<rects.size(); i++){
			areas.add(new PageTextArea(tp.getSize()+i,rects.get(i)));
		}
		csp.sort(areas);
		
		csp.removeUselessRegions(areas);
		
		TextStripperByPDFRectangle tsbro = new TextStripperByPDFRectangle();
		
		for(int i = 0; i < areas.size(); i++){
			int no = areas.get(i).getAreaNo();
			tsbro.addRegion(no, areas.get(i).getArea());
		}
		
		tsbro.extractRegions(pages.get(index));
		
		for(int i = 0; i < areas.size(); i++){
			int no = areas.get(i).getAreaNo();
			List<PDFCharacter> cs = tsbro.getCharactersByRegion(no);
			areas.get(i).setCharacters(tsbro.getCharactersByRegion(no));
			
		}
	
		
		csp.buildConnectionsBetweenRegions(areas);
		List<TableInformation> ti = csp.extractTableInformation(areas);
		
		for(int i = 0; i < areas.size(); i++){
			System.out.print(i+":");
			areas.get(i).print();
		}
		System.out.println("Headers");
		for(int i = 0; i < ti.size(); i++){
			PageTextArea header = areas.get(ti.get(i).getHead());
			header.print();
			System.out.println(header.isReferenced());
			System.out.println(header.isIsolated());
		}
		System.out.println("????");
		areas.get(3).print();
		areas.get(5).print();
		System.out.println(areas.get(3).getArea().isSameRow(areas.get(5).getArea()));
		System.out.println(areas.get(3).getArea().isSameRectangle(areas.get(5).getArea()));
		System.out.println(areas.get(3).getArea().isInThisArea(areas.get(5).getArea()));
		System.out.println(areas.get(5).getArea().isInThisArea(areas.get(3).getArea()));
		System.out.println(areas.get(3).getArea().isNextCellInTheSameRow(areas.get(5).getArea()));*/
		
	}

}
