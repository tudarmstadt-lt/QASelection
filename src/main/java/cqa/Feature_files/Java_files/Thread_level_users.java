package cqa.Feature_files.Java_files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Thread_level_users 
{
	public static void main(String args[])
	{
		File file = new File("/mnt/Titas/1_QA_MODEL/SemEval_Tasks/CQA/QASelection/src/main/java/cqa/Feature_files/Data_format_files/Thread_level/users_each_thread.txt");
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			try {
				String str = reader.readLine();
				do
				{
					String[] splited = str.split("\\s+");
					String q_id = splited[0];
					String asker_id = splited[1];
					String asker_name = splited[2];
					String question = reader.readLine();
					String[] id_list = new String[10];
					String[] comment_list = new String[10];
					for(int i=0; i<10; i++)
					{
						str = reader.readLine();
						splited = str.split("\\s+");
						String c_id = splited[0];
						String commenter_id = splited[1];
						String commenter_name = splited[2];
						String label = splited[3];
						String comment = reader.readLine();
						id_list[i] = commenter_id;
						comment_list[i] = comment;
						//get_same(asker_id, commenter_id);
						//get_length(comment);
						//get_same_imp(asker_id, commenter_id, comment);
					}
					vicinity(id_list, comment_list, asker_id);
				}
				while((str = reader.readLine())!=null);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
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
			System.out.println(comment);
			return 1;
		}
		return 0;
	}
	public static void vicinity(String[] arr1, String[] arr2, String asker_id)
	{
		for(int i=0; i<9; i++)
		{
			boolean f_1=true, f_2=true, f_3=true, f_4=true;
			double k_1=0.0, k_2=0.0, k_3=0.0, k_4=0.0;
			for(int j=i+1; j<10; j++)
			{
				if(get_same(asker_id, arr1[j]) == 1 && f_1)
				{
					double l = vicinity_ack(asker_id, arr1[j], arr2[j], j-i);
					if(l!=0.0)
					{
						System.out.println(arr1[i]+" "+arr1[j]+" "+arr2[j]+" "+l+"#");
						k_1 = l;
						f_1 = false;
					}						
				}
				if(get_same(asker_id, arr1[j]) == 1 && f_2)
				{
					double l = vicinity_noack(asker_id, arr1[j], arr2[j], j-i);
					if(l!=0.0)
					{
						System.out.println(arr1[i]+" "+arr1[j]+" "+arr2[j]+" "+l+"@");
						k_2 = l;
						f_2 = false;
					}
				}
				if(get_same(asker_id, arr1[j]) == 1 && f_3)
				{
					double l = vicinity_que(asker_id, arr1[j], arr2[j], j-i);
					if(l!=0.0)
					{
						System.out.println(arr1[i]+" "+arr1[j]+" "+arr2[j]+" "+l+"%");
						k_3 = l;
						f_3 = false;
					}
				}
				if(get_same(asker_id, arr1[j]) == 1 && f_4)
				{
					double l = vicinity_noque(asker_id, arr1[j], arr2[j], j-i);
					if(l!=0.0)
					{
						System.out.println(arr1[i]+" "+arr1[j]+" "+arr2[j]+" "+l+"*");
						k_4 = l;
						f_4 = false;
					}
				}
			}
			
		}
	}
	public static double vicinity_ack(String asker, String commenter, String comment, int k)
	{
		if(get_same_ack(asker, commenter, comment) == 1)
		{
			return compute(k);
		}
		return 0.0;
	}
	public static double vicinity_noack(String asker, String commenter, String comment, int k)
	{
		if(get_same_ack(asker, commenter, comment) == 0)
		{
			return compute(k);
		}
		return 0.0;
	}
	public static double vicinity_que(String asker, String commenter, String comment, int k)
	{
		if(get_same_que(asker, commenter, comment) == 1)
		{
			return compute(k);
		}
		return 0.0;
	}
	public static double vicinity_noque(String asker, String commenter, String comment, int k)
	{
		if(get_same_que(asker, commenter, comment) == 0)
		{
			return compute(k);
		}
		return 0.0;
	}
	public static double compute(int k)
	{
		return Math.max(0, 1.1-(k*0.1));
	}
	
}
