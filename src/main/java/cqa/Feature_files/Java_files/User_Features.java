package cqa.Feature_files.Java_files;

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

public class User_Features                   //File for User related features
{
    public static void main(String args[])
    {
    	File inputFile = new File("/mnt/Titas/1_QA_MODEL/SemEval_Tasks/CQA/QASelection/src/main/java/cqa/test_set_A.xml");
		SAXReader reader = new SAXReader();
		try {
			Document document = reader.read(inputFile);
			get_users(document);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }
    
    public static void get_users(Document document)               
    {
    	
    	List<Node> nodes = document.selectNodes("xml/Thread");          //Get users involved in Question-comment thread
    	for(int i=0; i<nodes.size(); i++)
    	{
    		String q_id = nodes.get(i).selectSingleNode("RelQuestion").valueOf("@RELQ_ID");
    		System.out.println(q_id+" :"+ nodes.get(i).selectSingleNode("RelQuestion").valueOf("@RELQ_USERID"));
    		List<Node> comment = nodes.get(i).selectNodes("RelComment");
    		int[] user_list = new int[5000];
		    for(int j=0; j<comment.size(); j++)
		    {
		    	String c_user = comment.get(j).valueOf("@RELC_USERID");
		    	int m = Integer.parseInt(c_user.substring(1));
		    	user_list[m] = 1;
		    }
		    System.out.print("Users: ");
		    for(int j=0; j<user_list.length; j++)
		    {
		    	if(user_list[j] == 1)
		    	{
		    		System.out.print(j+" ");
		    	}
		    }
		    System.out.println();
    	}
    }
}
