package cqa.Feature_files.Java_files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import cqa.Feature_files.Java_files.ThreadLevelUsers;
public class ThreadLevelProximity 
{
	static ThreadLevelUsers thread = new ThreadLevelUsers();
	static double f_1, f_2, f_3, f_4;
	public static void main(String args[])
	{
		File file = new File(args[0]);
		BufferedReader reader = null;
		PrintWriter writer = null;
		
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter(args[1], false)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			reader = new BufferedReader(new FileReader(file));
			try {
				String str = reader.readLine();
				do
				{
					String[] splited = str.split("\\s+", 4);
					String q_id = splited[0];
					int num = Integer.parseInt(splited[1]);
					String asker_id = splited[2];
					String asker_name = splited[3];
					String question = reader.readLine();
					String[] id_list = new String[num];
					String[] comment_list = new String[num];
					String[] label_list = new String[num];
					for(int i=0; i<num; i++)
					{
						str = reader.readLine();
						splited = str.split("\\s+", 4);
						String c_id = splited[0];
						String commenter_id = splited[2];
						String commenter_name = splited[3];
						String label = splited[1];
						String comment = reader.readLine();
						id_list[i] = commenter_id;
						comment_list[i] = comment;
						label_list[i] = label;
					}
					vicinity( writer, id_list, comment_list, label_list, asker_id);
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
	public static void vicinity(PrintWriter writer, String[] arr1, String[] arr2, String[] arr3, String asker_id)
	{
		int num = arr1.length;
		for(int i=0; i<num; i++)
		{
			f_1 = 0.0;
			f_2 = 0.0;
			f_3 = 0.0;
			f_4 = 0.0;
			boolean d_1=true, d_2=true, d_3=true, d_4=true;
			if(i<num-1)
			{
				for(int j=i+1; j<num; j++)
				{
					if(thread.get_same(asker_id, arr1[j]) == 1 && d_1)
					{
						double l = vicinity_ack(asker_id, arr1[j], arr2[j], j-i);
						if(l!=0.0)
						{
							f_1 = l;
							d_1 = false;
						}						
					}
					if(thread.get_same(asker_id, arr1[j]) == 1 && d_2)
					{
						double l = vicinity_noack(asker_id, arr1[j], arr2[j], j-i);
						if(l!=0.0)
						{
							f_2 = l;
							d_2 = false;
						}
					}
					if(thread.get_same(asker_id, arr1[j]) == 1 && d_3)
					{
						double l = vicinity_que(asker_id, arr1[j], arr2[j], j-i);
						if(l!=0.0)
						{
							f_3 = l;
							d_3 = false;
						}
					}
					if(thread.get_same(asker_id, arr1[j]) == 1 && d_4)
					{
						double l = vicinity_noque(asker_id, arr1[j], arr2[j], j-i);
						if(l!=0.0)
						{
							f_4 = l;
							d_4 = false;
						}
					}
				}
			}
			writer.println(thread.binary_class(arr3[i])+" 1:"+f_1+" 2:"+f_2+" 3:"+f_3+" 4:"+f_4);
		}
	}
	public static double vicinity_ack( String asker, String commenter, String comment, int k)     //among comments following c there is one by asker with an ack
	{
		if(thread.get_same_ack(asker, commenter, comment) == 1)
		{
			return compute(k);
		}
		return 0.0;
	}
	public static double vicinity_noack(String asker, String commenter, String comment, int k)  //among comments following c there is one by asker with no ack
	{
		if(thread.get_same_ack(asker, commenter, comment) == 0)
		{
			return compute(k);
		}
		return 0.0;
	}
	public static double vicinity_que(String asker, String commenter, String comment, int k)   //among comments following c there is one by asker with a question
	{
		if(thread.get_same_que(asker, commenter, comment) == 1)
		{
			return compute(k);
		}
		return 0.0;
	}
	public static double vicinity_noque(String asker, String commenter, String comment, int k)  //among comments following c there is one by asker with no question
	{
		if(thread.get_same_que(asker, commenter, comment) == 0)
		{
			return compute(k);
		}
		return 0.0;
	}
	public static double compute(int k)                //distance in terms of comments
	{
		return Math.max(0, 1.1-(k*0.1));
	}
}
