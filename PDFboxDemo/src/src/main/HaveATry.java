package src.main;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.pdfparser.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.util.Matrix;
import org.apache.pdfbox.util.PDFOperator;
import org.apache.pdfbox.util.PDFStreamEngine;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.util.PDFTextStripperByArea;
import org.apache.pdfbox.util.operator.BeginText;
import org.apache.pdfbox.util.operator.EndText;
import org.apache.pdfbox.util.operator.OperatorProcessor;

import src.tool.PageParser;
import src.tool.PageStreamGenerator;

public class HaveATry {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		FileInputStream fis = new FileInputStream("430248_2014-04-29_63948159.pdf");
		FileOutputStream fos = new FileOutputStream("430248_2014-04-29_63948159.xml");
		//BufferedWriter writer = new BufferedWriter(new FileWriter("430248_2014-04-29_63948159.xml"));
		
		//PageStreamGenerator psg = new PageStreamGenerator(fis);
		
		//PDFStreamEngine pse = new PDFStreamEngine();
		//pse.registerOperatorProcessor("BT", new BeginText());
		//pse.registerOperatorProcessor("ET", new EndText());
		
		/*if(psg.getSize() > 0){
			for(int i = 0 ; i < psg.getSize() ; i++){
				writer.write("Page "+i+":\n");
				//pse.processStream(pages.get(i), pages.get(i).getResources(), ps.getStream());
				//Matrix m = pse.getTextMatrix();
				//System.out.println(m.toString());
				PDStream pps = psg.getStream(i);
				if( pps!=null)
					writer.write(pps.getInputStreamAsString());
			}
		}*/
		
		/*psg.parse();
		psg.translate();
		psg.tableFinder();
		psg.printTokens();*/
		
		PageParser pp = new PageParser(fis);
		//pp.print();
		pp.xmlBuilder();
		pp.output(fos);
		//writer.write(fos);
		
		//BufferedWriter bw = new BufferedWriter();
		
		//System.out.println("123");
		
		fis.close();
		fos.close();
		//writer.close();
	}

}
