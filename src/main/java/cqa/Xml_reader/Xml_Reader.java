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
		File inputFile = new File("/mnt/Titas/1_QA_MODEL/SemEval_Tasks/CQA/CQA_Updated/data/semeval2016-task3-cqa-ql-traindev-v3.2/v3.2/train/SemEval2016-Task3-CQA-QL-train-part1-subtaskA.xml");
		SAXReader reader = new SAXReader();
		get_users();
		try {
			Document document = reader.read(inputFile);
			List<Node> nodes = document.selectNodes("xml/Thread");
			PrintWriter writer = null;
			try {
				writer = new PrintWriter(new BufferedWriter(new FileWriter("/mnt/Titas/1_QA_MODEL/SemEval_Tasks/CQA/QASelection/src/main/java/cqa/Xml_reader/parsed_file.txt", false)));
			} catch (IOException e) {
				e.printStackTrace();
			}
			for(int i=0; i< nodes.size(); i++)
			{
				String l = nodes.get(i).selectSingleNode("RelQuestion/RelQBody").getText().trim().replaceAll("\\s+", " ");
				String q_id = nodes.get(i).selectSingleNode("RelQuestion").valueOf("@RELQ_ID");
				writer.println(q_id);
				writer.println(l);
				
			    List<Node> comment = nodes.get(i).selectNodes("RelComment");
			    for(int j=0; j<comment.size(); j++)
			    {
			    	l = comment.get(j).selectSingleNode("RelCText").getText().trim().replaceAll("\\s+", " ");
			    	String c_id = comment.get(j).valueOf("@RELC_ID");
			    	String label = comment.get(j).valueOf("@RELC_RELEVANCE2RELQ");
			    	writer.println(c_id+" "+label);
			    	writer.println(l);
			    }
			   
			}
			writer.close();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	public static void get_users()               
    {
		File inputFile = new File("/mnt/Titas/1_QA_MODEL/SemEval_Tasks/CQA/CQA_Updated/data/semeval2016-task3-cqa-ql-traindev-v3.2/v3.2/train/SemEval2016-Task3-CQA-QL-train-part1-subtaskA.xml");
		SAXReader reader = new SAXReader();
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter("/mnt/Titas/1_QA_MODEL/SemEval_Tasks/CQA/QASelection/src/main/java/cqa/Feature_files/Data_format_files/Thread_level/users_each_thread.txt", false)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			Document document = reader.read(inputFile);
			List<Node> nodes = document.selectNodes("xml/Thread");          //Get users involved in Question-comment thread
	    	for(int i=0; i<nodes.size(); i++)
	    	{
	    		String q_id = nodes.get(i).selectSingleNode("RelQuestion").valueOf("@RELQ_ID");
	    		String l = nodes.get(i).selectSingleNode("RelQuestion/RelQBody").getText().trim().replaceAll("\\s+", " ");
	    		String user_name = nodes.get(i).selectSingleNode("RelQuestion").valueOf("@RELQ_USERNAME");
	    		writer.println(q_id+" "+nodes.get(i).selectSingleNode("RelQuestion").valueOf("@RELQ_USERID")+" "+user_name);
	    		writer.println(l);
	    		List<Node> comment = nodes.get(i).selectNodes("RelComment");
			    for(int j=0; j<comment.size(); j++)
			    {
			    	l = comment.get(j).selectSingleNode("RelCText").getText().trim().replaceAll("\\s+", " ");
			    	String c_id = comment.get(j).valueOf("@RELC_ID");
			    	String label = comment.get(j).valueOf("@RELC_RELEVANCE2RELQ");
			    	String commenter_id = comment.get(j).valueOf("@RELC_USERID");
			    	String commenter_name = comment.get(j).valueOf("@RELC_USERNAME");
			    	writer.println(c_id+" "+commenter_id+" "+commenter_name+" "+label);
			    	writer.println(l);
			    }
	    	}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writer.close();
    }
}
