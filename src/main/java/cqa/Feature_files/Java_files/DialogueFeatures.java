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

public class DialogueFeatures                                       // Capture if multiple users are in dialogue
{
	public static void main(String args[])
	{
		File file = new File(args[0]);               //input file
		BufferedReader reader = null;
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter(args[1], false)));      //output file
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
					String qusername = splited[3].toLowerCase();
					ArrayList<String> users = new ArrayList<>();
					ArrayList<String> labels = new ArrayList<>();
					ArrayList<String> cid = new ArrayList<>();
					ArrayList<String> comments = new ArrayList<>();
					users.add(qusername);
					line = reader.readLine();
					for(int i=0; i<num; i++)
					{
						line = reader.readLine();
						splited = line.split("\\s+", 4);
						cid.add(splited[0]);
						labels.add(splited[1]);
						users.add(splited[3].toLowerCase());
						comments.add(reader.readLine());
					}
					ArrayList<Double> f1 = name_dialog(users, comments);
					ArrayList<Double> f2 = dialog(users, cid);
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
	public static ArrayList<Double> dialog(ArrayList <String> map, ArrayList<String> cid)               //find users in dialogues by multiplicity of their comments
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
	public static void SVM_writer(PrintWriter writer, ArrayList<String> label, ArrayList<Double> v1, ArrayList<Double> v2)       //SVM file writer
	{
		for(int i=0; i<label.size(); i++)
		{
			writer.println(binary_class(label.get(i))+" 1:"+v1.get(i)+" 2:"+v2.get(i));
		}
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
