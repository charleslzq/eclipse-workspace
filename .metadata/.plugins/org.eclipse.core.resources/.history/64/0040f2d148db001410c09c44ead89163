package test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.junit.Before;
import org.junit.Test;

import src.tool.PageParser;

public class CellExtractionText {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testPageParser() throws Exception {
		//fail("Not yet implemented");
		FileInputStream fis = new FileInputStream("000895_2014_n.pdf");
   		BufferedInputStream bis = new BufferedInputStream(fis);
   		FileOutputStream fos = new FileOutputStream("000895_2014_n.xml");
   		PageParser pp = new PageParser(bis);
		pp.output(fos);
		bis.close();
		fis.close();
		fos.close();
	}

}
