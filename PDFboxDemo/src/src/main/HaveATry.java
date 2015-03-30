package src.main;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;

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

public class HaveATry {

	public static void main(String[] args) throws Exception {
		test();
		// TODO Auto-generated method stub
		/*JFileChooser jFileChooser = new JFileChooser();
		jFileChooser.setMultiSelectionEnabled(true);
        int i = jFileChooser.showOpenDialog(null);
        if(i== jFileChooser.APPROVE_OPTION){ //���ļ�
            File[] fs = jFileChooser.getSelectedFiles();
            for(int j = 0; j < fs.length; j++){
            	File f = fs[j];
	            FileInputStream fis = new FileInputStream(f);
	    		BufferedInputStream bis = new BufferedInputStream(fis);
	    		//String path = f.getAbsolutePath();
	    		//path.replaceAll(".pdf", ".xml");
	    		FileOutputStream fos = new FileOutputStream(f.getAbsolutePath()+".xml");
	    		//BufferedWriter writer = new BufferedWriter(new FileWriter("430248_2014-04-29_63948159.xml"));
	    		
	    		//PageStreamGenerator psg = new PageStreamGenerator(fis);
	    		
	    		//PDFTextStripper tsp = new PDFTextStripper();
	    		
	    		//PDFStreamEngine pse = new PDFStreamEngine();
	    		//pse.registerOperatorProcessor("BT", new BeginText());
	    		//pse.registerOperatorProcessor("ET", new EndText());
	    		
	    		PageParser pp = new PageParser(bis);
	    		//pp.parse(30, 40);
	    		//pp.print();
	    		//pp.parse();
	    		pp.output(fos);
	    		//writer.write(fos);
	    		
	    		//BufferedWriter bw = new BufferedWriter();
	    		
	    		//System.out.println("123");
	    		bis.close();
	    		fis.close();
	    		fos.close();
	    		//writer.close();
            }
        }*/
		
	}
	
	public static void test() throws Exception{
		FileInputStream fis = new FileInputStream("000039_2014_n.pdf");
   		BufferedInputStream bis = new BufferedInputStream(fis);
   		//String path = f.getAbsolutePath();
   		//path.replaceAll(".pdf", ".xml");
   		FileOutputStream fos = new FileOutputStream("000039_2014_n.xml");
   		PageParser pp = new PageParser(bis);
		//pp.parse(30, 40);
		//pp.print();
		//pp.parse();
		pp.output(fos);
		//writer.write(fos);
		
		//BufferedWriter bw = new BufferedWriter();
		
		//System.out.println("123");
		bis.close();
		fis.close();
		fos.close();
   }

}
