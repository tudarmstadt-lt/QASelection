package cqa.Feature_files.Java_files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Thread_level_users 
{
	static int f_1, f_2, f_3, f_4, f_5;
	public static void main(String args[])
	{
		File file = new File("/mnt/Titas/1_QA_MODEL/SemEval_Tasks/CQA/QASelection/src/main/java/cqa/Feature_files/Data_format_files/Thread_level/users_each_thread.txt");
		BufferedReader reader = null;
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter("/mnt/Titas/1_QA_MODEL/SemEval_Tasks/CQA/QASelection/src/main/java/cqa/Feature_files/Data_format_files/RankLib/RankLib_thread_level_users.txt", false)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		int q_id_rank = 0;
		try {
			reader = new BufferedReader(new FileReader(file));
			try {
				String str = reader.readLine();
				do
				{
					q_id_rank++;
					String[] splited = str.split("\\s+");
					String q_id = splited[0];
					String asker_id = splited[1];
					String asker_name = splited[2];
					String question = reader.readLine();
					for(int i=0; i<10; i++)
					{
						str = reader.readLine();
						splited = str.split("\\s+");
						String c_id="";
						String commenter_id="";
						String commenter_name="";
						String label="";
						if(splited.length == 4)
						{
							c_id = splited[0];
							commenter_id = splited[1];
							commenter_name = splited[2];
							label = splited[3];
						}
						else
						{
							c_id = splited[0];
							commenter_id = splited[1];
							commenter_name = " ";
							label = splited[2];
						}
						String comment = reader.readLine();
						f_1 = get_same(asker_id, commenter_id);
						f_2 = get_length(comment);
						f_3 = get_same_ack(asker_id, commenter_id, comment);
						f_4 = get_same_que(asker_id, commenter_id, comment);
						f_5 = get_same_imp(asker_id, commenter_id, comment);
						RankLib_writer(writer, label, q_id_rank, c_id);
						//SVM_writer(writer, label, 1);
					}
				}
				while((str = reader.readLine())!=null);
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
		writer.println(get_Label_value(label)+" "+"qid:"+q_id_rank+" 1:"+f_1+" 2:"+f_2+" 3:"+f_3+" 4:"+f_4+" 5:"+f_5+" # "+c_id);
	}
	public static void SVM_writer(PrintWriter writer, String label, int flag)
	{
		if(flag == 0)
			writer.println(get_Label_value(label)+" 1:"+f_1+" 2:"+f_2+" 3:"+f_3+" 4:"+f_4+" 5:"+f_5);
		else
			writer.println(binary_class(label)+" 1:"+f_1+" 2:"+f_2+" 3:"+f_3+" 4:"+f_4+" 5:"+f_5);
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
	public static int get_same(String asker, String commenter)
	{
		if(asker.equals(commenter))
		{
			//System.out.println(comment);
			return 1;
		}
		return 0;
	}
	public static int get_length(String comment)
	{
		return comment.length();
	}
	public static int get_same_ack(String asker, String commenter, String comment)
	{
		if(get_same(asker, commenter) == 0)
		{
			return 0;
		}
		String[] ack_words = {"thanks", "thank", "thankyou", "appreciate", "appreciated"};
		String[] str = comment.replaceAll("[^a-zA-Z0-9 ]", "").toLowerCase().split("\\s+");
		for(String words: str)
		{
			for(String ack: ack_words)
			{
				if(ack.equals(words))
				{
					//System.out.println(words);
					return 1;
				}
			}
		}
		return 0;
	}
	public static int get_same_que(String asker, String commenter, String comment)
	{
		if(get_same(asker, commenter) == 0)
		{
			return 0;
		}
		if(comment.contains("?"))
		{
			//System.out.println(comment);
			return 1;
		}
		return 0;
	}
	public static int get_same_imp(String asker, String commenter, String comment)
	{
		if(get_same(asker, commenter) == 1 && get_same_ack(asker, commenter, comment) == 0 && get_same_que(asker, commenter, comment) == 0)
		{
			//System.out.println(comment);
			return 1;
		}
		return 0;
	}
}
