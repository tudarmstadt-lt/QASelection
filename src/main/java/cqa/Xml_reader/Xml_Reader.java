package cqa.Xml_reader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;
public class Xml_Reader                    // File for reading XML files 
{
	public static void main(String args[])    
	{
		File inputFile = new File("/mnt/Titas/1_QA_MODEL/SemEval_Tasks/CQA/CQA_Updated/data/semeval2016_task3_tests/SemEval2016_task3_test/English/SemEval2016-Task3-CQA-QL-test-subtaskA.xml");
		SAXReader reader = new SAXReader();
		try {
			Document document = reader.read(inputFile);
			List<Node> nodes = document.selectNodes("xml/Thread");
			//System.out.println(nodes.size());
			PrintWriter writer = null;
			try {
				writer = new PrintWriter(new BufferedWriter(new FileWriter("/mnt/Titas/1_QA_MODEL/SemEval_Tasks/CQA/QASelection/src/main/java/cqa/Feature_files/parsed_file.txt", false)));
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(int i=0; i< nodes.size(); i++)
			{
				System.out.println("QUESTION:");
				String l = nodes.get(i).selectSingleNode("RelQuestion/RelQBody").getText().trim().replaceAll("\\s+", " ");
				String q_id = nodes.get(i).selectSingleNode("RelQuestion").valueOf("@RELQ_ID");
				writer.println(q_id);
				writer.println(l);
				//System.out.println("-----------------------------------------------------------------");

				System.out.println("COMMENTS");
			    List<Node> comment = nodes.get(i).selectNodes("RelComment");
			    for(int j=0; j<comment.size(); j++)
			    {
			    	l = comment.get(j).selectSingleNode("RelCText").getText().trim().replaceAll("\\s+", " ");
			    	String c_id = comment.get(j).valueOf("@RELC_ID");
			    	String label = comment.get(j).valueOf("@RELC_RELEVANCE2RELQ");
			    	//System.out.println(l);
			    	//System.out.println("-----------------------------------");
			    	writer.println(c_id+" "+label);
			    	writer.println(l);
			    }
			   
			}
			writer.close();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
