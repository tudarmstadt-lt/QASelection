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
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;

public class Thread_level 
{
	public static void main(String args[])
	{
		File file = new File("/mnt/Titas/1_QA_MODEL/SemEval_Tasks/CQA/QASelection/src/main/java/cqa/Xml_reader/parsed_file.txt");
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			try {
				String q_id = reader.readLine();
				get_users();
				do
				{
					String question = reader.readLine();
					for(int i=0; i<10; i++)
					{
						String str = reader.readLine();
						String[] splited = str.split("\\s+");
						String c_id = splited[0];
						String label = splited[1];
						String comment = reader.readLine();
						//special_word_matcher("yes", comment);
						//special_word_matcher("sure", comment);
						//special_word_matcher("no", comment);
						//special_word_matcher("neither", comment);
						//special_word_matcher("okay", comment);
						//special_character_matcher("@", comment);
						special_character_matcher("?", comment);
						//begin_matcher("yes", comment);
						//length_matcher(comment);
						
					}					
				}
				while((q_id = reader.readLine())!=null);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public static int URL_matcher(String comment, String c_id)
	{
		Pattern p = Pattern.compile("\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]",Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(comment);
        boolean result = m.find();
        if(result)
        {
        	System.out.println(c_id);
        	System.out.println(m.group());
        	return 1;
        }
        return 0;
	}
	public static int email_matcher(String comment, String c_id)
	{
		Matcher m = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(comment);
        boolean result = m.find();
        if(result)
        {
        	System.out.println(c_id);
        	System.out.println(m.group());
        	return 1;
        }
        return 0;
	}
	public static int special_word_matcher(String to_match, String comment)
	{
		String[] str = comment.replaceAll("[^a-zA-Z0-9 ]", "").toLowerCase().split("\\s+");
		for(String words: str)
		{
			if(words.equals(to_match))
			{
				System.out.println(comment);
				return 1;
			}
		}
		return 0;
	}
	public static int special_character_matcher(String to_match, String comment)
	{
		if(comment.contains(to_match))
		{
			System.out.println(comment);
			return 1;
		}
			
		return 0;
	}
	public static int begin_matcher(String to_match, String comment)
	{
		String[] str = comment.replaceAll("[^a-zA-Z0-9 ]", "").toLowerCase().split("\\s+");
		if(str[0].equals(to_match))
		{
			System.out.println(comment);
			return 1;
		}
		return 0;
	}
	public static int length_matcher(String comment)
	{
		String[] splited = comment.toLowerCase().split("\\s+");
		for(String words: splited)
		{
			if(words.length() > 15)
			{
				System.out.println(words);
				return 1;
			}
		}
		return 0;
	}
	public static void get_users()               
    {
		File inputFile = new File("/mnt/Titas/1_QA_MODEL/SemEval_Tasks/CQA/CQA_Updated/data/semeval2016_task3_tests/SemEval2016_task3_test/English/SemEval2016-Task3-CQA-QL-test-subtaskA.xml");
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
