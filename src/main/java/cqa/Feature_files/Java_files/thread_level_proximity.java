package cqa.Feature_files.Java_files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import cqa.Feature_files.Java_files.Thread_level_users;
public class thread_level_proximity 
{
	static Thread_level_users thread = new Thread_level_users();
	static double f_1, f_2, f_3, f_4;
	public static void main(String args[])
	{
		File file = new File("/mnt/Titas/1_QA_MODEL/SemEval_Tasks/CQA/QASelection/src/main/java/cqa/Feature_files/Data_format_files/Thread_level/users_each_thread.txt");
		BufferedReader reader = null;
		PrintWriter writer = null;
		
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter("/mnt/Titas/1_QA_MODEL/SemEval_Tasks/CQA/QASelection/src/main/java/cqa/Feature_files/Data_format_files/SVM/Multiclass/SVM_thread_level_proximity.txt", false)));
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
					String[] id_list = new String[10];
					String[] comment_list = new String[10];
					String[] label_list = new String[10];
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
						id_list[i] = commenter_id;
						comment_list[i] = comment;
						label_list[i] = label;
					}
					vicinity( writer, id_list, comment_list, label_list, asker_id, q_id_rank);
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
	public static void vicinity(PrintWriter writer, String[] arr1, String[] arr2, String[] arr3, String asker_id, int q_id_rank)
	{
		for(int i=0; i<10; i++)
		{
			f_1 = 0.0;
			f_2 = 0.0;
			f_3 = 0.0;
			f_4 = 0.0;
			boolean d_1=true, d_2=true, d_3=true, d_4=true;
			if(i<9)
			{
				for(int j=i+1; j<10; j++)
				{
					if(thread.get_same(asker_id, arr1[j]) == 1 && d_1)
					{
						double l = vicinity_ack(asker_id, arr1[j], arr2[j], j-i);
						if(l!=0.0)
						{
							//System.out.println(arr1[i]+" "+arr1[j]+" "+arr2[j]+" "+l+"#");
							f_1 = l;
							d_1 = false;
						}						
					}
					if(thread.get_same(asker_id, arr1[j]) == 1 && d_2)
					{
						double l = vicinity_noack(asker_id, arr1[j], arr2[j], j-i);
						if(l!=0.0)
						{
							//System.out.println(arr1[i]+" "+arr1[j]+" "+arr2[j]+" "+l+"@");
							f_2 = l;
							d_2 = false;
						}
					}
					if(thread.get_same(asker_id, arr1[j]) == 1 && d_3)
					{
						double l = vicinity_que(asker_id, arr1[j], arr2[j], j-i);
						if(l!=0.0)
						{
							//System.out.println(arr1[i]+" "+arr1[j]+" "+arr2[j]+" "+l+"%");
							f_3 = l;
							d_3 = false;
						}
					}
					if(thread.get_same(asker_id, arr1[j]) == 1 && d_4)
					{
						double l = vicinity_noque(asker_id, arr1[j], arr2[j], j-i);
						if(l!=0.0)
						{
							//System.out.println(arr1[i]+" "+arr1[j]+" "+arr2[j]+" "+l+"*");
							f_4 = l;
							d_4 = false;
						}
					}
				}
			}
			//writer.println(thread.get_Label_value(arr3[i])+" "+"qid:"+q_id_rank+" 1:"+f_1+" 2:"+f_2+" 3:"+f_3+" 4:"+f_4+" # "+arr1[i]);
			writer.println(thread.get_Label_value(arr3[i])+" 1:"+f_1+" 2:"+f_2+" 3:"+f_3+" 4:"+f_4);
			
		}
	}
	public static double vicinity_ack( String asker, String commenter, String comment, int k)
	{
		if(thread.get_same_ack(asker, commenter, comment) == 1)
		{
			return compute(k);
		}
		return 0.0;
	}
	public static double vicinity_noack(String asker, String commenter, String comment, int k)
	{
		if(thread.get_same_ack(asker, commenter, comment) == 0)
		{
			return compute(k);
		}
		return 0.0;
	}
	public static double vicinity_que(String asker, String commenter, String comment, int k)
	{
		if(thread.get_same_que(asker, commenter, comment) == 1)
		{
			return compute(k);
		}
		return 0.0;
	}
	public static double vicinity_noque(String asker, String commenter, String comment, int k)
	{
		if(thread.get_same_que(asker, commenter, comment) == 0)
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
