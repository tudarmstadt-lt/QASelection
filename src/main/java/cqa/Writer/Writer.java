package cqa.Writer;
import cqa.Feature_files.Java_files.Similarity_feature_generator;

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
import java.util.*;

public class Writer 
{
	public static void main(String args[])
	{
		PrintWriter writer = null;
		BufferedReader reader = null;
		BufferedReader reader_2 = null;
		BufferedReader reader_3 = null;
		File file = new File("/mnt/Titas/1_QA_MODEL/SemEval_Tasks/CQA/QASelection/src/main/java/cqa/Xml_reader/parsed_file.txt");
		File file_2 = new File("/mnt/Titas/1_QA_MODEL/SemEval_Tasks/CQA/QASelection/src/main/java/cqa/Feature_files/Data_format_files/RankLib/RankLib_test_file.score");
		File file_3 = new File("/mnt/Titas/1_QA_MODEL/Tools/libsvm-3.21/java/output.txt");
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter("/mnt/Titas/1_QA_MODEL/SemEval_Tasks/CQA/CQA_Updated/Results/semeval2016_task3_submissions_and_score/SemEval2016_task3_submissions_and_scores/_scorer/results.txt", false)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			reader = new BufferedReader(new FileReader(file));
			reader_2 = new BufferedReader(new FileReader(file_2));
			reader_3 = new BufferedReader(new FileReader(file_3));
			try {
				String q_id = reader.readLine();
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
						String score_line = reader_2.readLine();
						splited = score_line.split("\\s+");
						String score = splited[2];
						String bin_class = get_bool(reader_3.readLine());
						writer.println(q_id+" "+c_id+" 0 "+score+" "+bin_class);
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
	public static String get_bool(String s)
	{
		if(s.equals("0.0"))
			return "false";
		return "true";
	}
}
