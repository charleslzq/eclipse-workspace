package test;

import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDPage;
import org.junit.Before;
import org.junit.Test;

import src.tool.ContentStreamParser;
import src.tool.PDFCharacter;
import src.tool.PDFRectangle;
import src.tool.PageTextArea;
import src.tool.RectangleType;
import src.tool.TextStripperByPDFRectangle;
import src.tool.TokenParser;

public class AreaTextTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testRemoveUselessRegions() throws IOException {
		//fail("Not yet implemented");
		FileInputStream fis = new FileInputStream("000039_2014_n.pdf");
   		BufferedInputStream bis = new BufferedInputStream(fis);
   		
   		
   		PDFParser parser = new PDFParser(fis);
		parser.parse();
		
		List<PDPage> pages = parser.getPDDocument().getDocumentCatalog().getAllPages();
		int index = 10;
		ContentStreamParser csp = new ContentStreamParser(index+1,pages.get(index));
		TokenParser tp = new TokenParser(pages.get(index).getContents().getStream().getStreamTokens());
		Map<Integer, PDFRectangle> areas = tp.rectangleParser();
		List<PageTextArea> textAreas = csp.constructRegions(areas);
		TextStripperByPDFRectangle tsbro = new TextStripperByPDFRectangle();
		
		for(int i = 0; i < tp.getSize(); i++){
			if(areas.containsKey(new Integer(i))){
				PDFRectangle pr = areas.get(new Integer(i));
				if(pr.getType() == RectangleType.PDF_CELL)
					tsbro.addRegion(i, pr);
			}
		}
		
		tsbro.extractRegions(pages.get(0));
		
		for(int i = 0; i < textAreas.size(); i++){
			int no = textAreas.get(i).getAreaNo();
			List<PDFCharacter> cs = tsbro.getCharactersByRegion(no);
			textAreas.get(i).setCharacters(tsbro.getCharactersByRegion(no));
		}
		
		csp.removeUselessRegions(textAreas);
		
		for(int i=0; i<textAreas.size(); i++)
			textAreas.get(i).print();
	}

	@Test
	public void testConstructRegions() throws IOException {
		fail("Not yet implemented");
		FileInputStream fis = new FileInputStream("000039_2014_n.pdf");
   		BufferedInputStream bis = new BufferedInputStream(fis);
   		
   		
   		PDFParser parser = new PDFParser(fis);
		parser.parse();
		
		List<PDPage> pages = parser.getPDDocument().getDocumentCatalog().getAllPages();
		int index = 10;
		ContentStreamParser csp = new ContentStreamParser(index+1,pages.get(index));
		TokenParser tp = new TokenParser(pages.get(index).getContents().getStream().getStreamTokens());
		Map<Integer, PDFRectangle> areas = tp.rectangleParser();
		List<PageTextArea> textAreas = csp.constructRegions(areas);
		
		for(int i=0; i<textAreas.size(); i++)
			textAreas.get(i).print();
	}

	@Test
	public void testBuildConnectionsBetweenRegions() {
		fail("Not yet implemented");
	}

	@Test
	public void testAttachTexts() throws IOException {
		fail("Not yet implemented");
		FileInputStream fis = new FileInputStream("000039_2014_n.pdf");
   		BufferedInputStream bis = new BufferedInputStream(fis);
   		
   		
   		PDFParser parser = new PDFParser(fis);
		parser.parse();
		
		List<PDPage> pages = parser.getPDDocument().getDocumentCatalog().getAllPages();
		int index = 10;
		ContentStreamParser csp = new ContentStreamParser(index+1,pages.get(index));
		TokenParser tp = new TokenParser(pages.get(index).getContents().getStream().getStreamTokens());
		Map<Integer, PDFRectangle> areas = tp.rectangleParser();
		List<PageTextArea> textAreas = csp.constructRegions(areas);
		TextStripperByPDFRectangle tsbro = new TextStripperByPDFRectangle();
		
		for(int i = 0; i < tp.getSize(); i++){
			if(areas.containsKey(new Integer(i))){
				PDFRectangle pr = areas.get(new Integer(i));
				if(pr.getType() == RectangleType.PDF_CELL)
					tsbro.addRegion(i, pr);
			}
		}
		
		tsbro.extractRegions(pages.get(0));
		
		for(int i = 0; i < textAreas.size(); i++){
			int no = textAreas.get(i).getAreaNo();
			List<PDFCharacter> cs = tsbro.getCharactersByRegion(no);
			textAreas.get(i).setCharacters(tsbro.getCharactersByRegion(no));
		}
		
		for(int i=0; i<textAreas.size(); i++)
			textAreas.get(i).print();
	}

}
