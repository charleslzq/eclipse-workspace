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
		// TODO Auto-generated method stub
		JFileChooser jFileChooser = new JFileChooser();
        int i = jFileChooser.showOpenDialog(null);
        if(i== jFileChooser.APPROVE_OPTION){ //打开文件
        	File f = jFileChooser.getSelectedFile();
            FileInputStream fis = new FileInputStream(f);
    		BufferedInputStream bis = new BufferedInputStream(fis);
    		FileOutputStream fos = new FileOutputStream(f.getAbsolutePath()+".xml");
    		
    		PageParser pp = new PageParser(bis);
    		pp.output(fos);
    		bis.close();
    		fis.close();
    		fos.close();
    		
    		System.out.println("Done");
        }
		
	}

}
