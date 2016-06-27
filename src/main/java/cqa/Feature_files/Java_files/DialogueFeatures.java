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
import java.util.HashMap;
import java.util.Map;

public class DialogueFeatures 
{
	public static void main(String args[])
	{
		File file = new File(args[0]);
		BufferedReader reader = null;
		PrintWriter writer = null;
		int q_id_rank = 0;
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter(args[1], false)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			reader = new BufferedReader(new FileReader(file));
			String line;
			try {
				while((line = reader.readLine()) != null)
				{
					String splited[] = line.split("\\s+", 4);
					int num = Integer.parseInt(splited[1]);
					String quser = splited[2];
					String qusername = splited[3];
					ArrayList<String> users = new ArrayList<>();
					ArrayList<String> labels = new ArrayList<>();
					ArrayList<String> cid = new ArrayList<>();
					ArrayList<String> comments = new ArrayList<>();
					users.add(qusername);
					line = reader.readLine();
					q_id_rank++;
					for(int i=0; i<num; i++)
					{
						line = reader.readLine();
						splited = line.split("\\s+", 4);
						cid.add(splited[0]);
						labels.add(splited[1]);
						users.add(splited[3]);
						comments.add(reader.readLine());
					}
					ArrayList<Double> f1 = name_dialog(users, comments);
					ArrayList<Double> f2 = dialog(users, cid);
					//RankLib_writer(writer, labels, q_id_rank, cid, f1, f2);
					SVM_writer(writer, labels, f1, f2);
				}
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static ArrayList<Double> name_dialog(ArrayList<String> map, ArrayList<String> comments)  //find users in dialogue by name
	{
		ArrayList<Double> v1 = new ArrayList<Double>();
		for(int i=0; i<comments.size(); i++)
		{
			double val = 0.0;
			String[] spl = comments.get(i).split("\\s+");
			for(String value : map)
			{
				for(String words: spl)
				{
					if(value.equals(words))
					{
						val = 1.0;
						break;
					}
				}
				if(val == 1.0)
					break;
			}
			v1.add(val);
		}
		return v1;
	}
	public static ArrayList<Double> dialog(ArrayList <String> map, ArrayList<String> cid)               //find users in iterated dialogues
	{
		ArrayList<Double> v2 = new ArrayList<>();
		ArrayList<String> h = new ArrayList<>();
		h.add(map.get(0));
		for(int i=0; i<cid.size(); i++)
		{
			double val = 0.0;
			if(h.contains(map.get(i+1)))
			{
				val = 1.0;
			}
			else
			{
				h.add(map.get(i+1));
			}
			v2.add(val);
		}
		return v2;
	}
	public static void RankLib_writer(PrintWriter writer,ArrayList<String> label, int q_id_rank,ArrayList<String> c_id, ArrayList<Double> v1, ArrayList<Double> v2)  //RankLib File writer
	{
		for(int i=0; i<c_id.size(); i++)
		{
			writer.println(get_Label_value(label.get(i))+" "+"qid:"+q_id_rank+" 1:"+v1.get(i)+" 2:"+v2.get(i)+" # "+c_id.get(i));
		}
	}
	public static void SVM_writer(PrintWriter writer, ArrayList<String> label, ArrayList<Double> v1, ArrayList<Double> v2)       //SVM file writer
	{
		for(int i=0; i<label.size(); i++)
		{
			writer.println(binary_class(label.get(i))+" 1:"+v1.get(i)+" 2:"+v2.get(i));
		}
	}
	public static int get_Label_value(String s)                      //Generate multiclass labels
	{
		if(s.equals("Good"))
		{
			return 1;
		}
		else if(s.equals("PotentiallyUseful"))
		{
			return 3;
		}
		return 2;
	}
	public static int binary_class(String s)                     //Generate binary labels
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
}
