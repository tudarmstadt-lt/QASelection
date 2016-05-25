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
	static int f_1, f_2, f_3, f_4, f_5, f_6, f_7, f_8, f_9, f_10, f_11;
	public static void main(String args[])
	{
		File file = new File("/mnt/Titas/1_QA_MODEL/SemEval_Tasks/CQA/QASelection/src/main/java/cqa/Xml_reader/parsed_file.txt");
		BufferedReader reader = null;
		PrintWriter writer = null;
		//Similarity_feature_generator gen = new Similarity_feature_generator();
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter("/mnt/Titas/1_QA_MODEL/SemEval_Tasks/CQA/QASelection/src/main/java/cqa/Feature_files/Data_format_files/RankLib/RankLib_thread_level.txt", false)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		int q_id_rank = 0;
		try {
			reader = new BufferedReader(new FileReader(file));
			try {
				String q_id = reader.readLine();
				
				do
				{
					String question = reader.readLine();
					q_id_rank++;
					for(int i=0; i<10; i++)
					{
						String str = reader.readLine();
						String[] splited = str.split("\\s+");
						String c_id = splited[0];
						String label = splited[1];
						String comment = reader.readLine();
						f_1 = URL_matcher(comment, c_id);
						f_2 = email_matcher(comment, c_id);
						f_3 = special_word_matcher("yes", comment);
						f_4 = special_word_matcher("sure", comment);
						f_5 = special_word_matcher("no", comment);
						f_6 = special_word_matcher("neither", comment);
						f_7 = special_word_matcher("ok", comment);
						f_8 = special_character_matcher("@", comment);
						f_9 = special_character_matcher("?", comment);
						f_10 = begin_matcher("yes", comment);
						f_11 = length_matcher(comment);
						RankLib_writer(writer, label, q_id_rank, c_id);
						//SVM_writer(writer, label, 1);
					}					
				}
				while((q_id = reader.readLine())!=null);
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public static void RankLib_writer(PrintWriter writer, String label, int q_id_rank, String c_id)
	{
		writer.println(get_Label_value(label)+" "+"qid:"+q_id_rank+" 1:"+f_1+" 2:"+f_2+" 3:"+f_3+" 4:"+f_4+" 5:"+f_5+" 6:"+f_6+" 7:"+f_7+" 8:"+f_8+" 9:"+f_9+" 10:"+f_10+" 11:"+f_11+" # "+c_id);
	}
	public static void SVM_writer(PrintWriter writer, String label, int flag)
	{
		if(flag == 0)
			writer.println(get_Label_value(label)+" 1:"+f_1+" 2:"+f_2+" 3:"+f_3+" 4:"+f_4+" 5:"+f_5+" 6:"+f_6+" 7:"+f_7+" 8:"+f_8+" 9:"+f_9+" 10:"+f_10);
		else
			writer.println(binary_class(label)+" 1:"+f_1+" 2:"+f_2+" 3:"+f_3+" 4:"+f_4+" 5:"+f_5+" 6:"+f_6+" 7:"+f_7+" 8:"+f_8+" 9:"+f_9+" 10:"+f_10+" 11:"+f_11);
	}
	public static int get_Label_value(String s)
	{
		if(s.equals("Good"))
		{
			return 1;
		}
		else if(s.equals("PotentiallyUseful"))
		{
			return 2;
		}
		return 3;
	}
	public static int binary_class(String s)
	{
		if(s.equals("Good"))
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}
	public static int URL_matcher(String comment, String c_id)
	{
		Pattern p = Pattern.compile("\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]",Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(comment);
        boolean result = m.find();
        if(result)
        {
        	//System.out.println(c_id);
        	//System.out.println(m.group());
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
        	//System.out.println(c_id);
        	//System.out.println(m.group());
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
				//System.out.println(comment);
				return 1;
			}
		}
		return 0;
	}
	public static int special_character_matcher(String to_match, String comment)
	{
		if(comment.contains(to_match))
		{
			//System.out.println(comment);
			return 1;
		}
			
		return 0;
	}
	public static int begin_matcher(String to_match, String comment)
	{
		String[] str = comment.replaceAll("[^a-zA-Z0-9 ]", "").toLowerCase().split("\\s+");
		if(str.length > 0 && str[0].equals(to_match))
		{
			//System.out.println(comment);
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
				//System.out.println(words);
				return 1;
			}
		}
		return 0;
	}
	
}
