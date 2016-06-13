package cqa.Feature_files.Java_files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class EmbeddingTrainer 
{
	static int size = 100;
	static int unnoticed = 0;
	public static void main(String args[])
	{
		File file = new File(args[0]);
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(new FileReader(file));
			String l;
			try {
				HashMap<String, vector> map = new HashMap<>();
				while((l = reader.readLine())!= null)
				{
					String splited[] = l.split("\\s+", 2);
					String word = splited[0];
					//System.out.println(size);
					vector v = new vector(splited[1]);
					map.put(word, v);
				}
				sentence_vector(map, args[1], args[2]);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	public static void sentence_vector(HashMap<String, vector> map, String fread, String fwrite)
	{
		File file = new File(fread);
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter(fwrite, false)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String q_id;
			try {
				while((q_id = reader.readLine())!=null)
				{
					String question = reader.readLine();
					String[] str = question.split("\\s+");
					calculate_avg(writer, str, map, q_id, "");
					for(int i=0; i<10; i++)
					{
						String st = reader.readLine();
						String[] splited = st.split("\\s+");
						String c_id = splited[0];
						String label = splited[1];
						String comment = reader.readLine();
						str = comment.split("\\s+");
						calculate_avg(writer, str, map, c_id, label);
					}
				}
				System.out.println(unnoticed);
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
	public static void calculate_avg(PrintWriter writer, String[] str, HashMap<String, vector> map, String id, String label)
	{
		int count = 0;
		double[] vec = new double[size];
		for(int i=0; i<vec.length; i++)
		{
			vec[i] = 0.0;
		}
		for(int i=0; i<str.length; i++)
		{
			vector v = map.get(str[i]);
			if( v!= null )
			{
				count++;
				
				for(int j=0; j<v.vec.length; j++)
				{
					vec[j]+=v.vec[j];
				}
			}
			else
			{
				System.out.println(str[i]);
				unnoticed++;
			}
		}
		//System.out.println(count);
		if(!label.isEmpty())
			writer.print(id+" "+label+" ");
		else
			writer.print(id+" ");
		for(int i=0; i<vec.length; i++)
		{
			if(count != 0)
				vec[i]/= count;
			writer.print(vec[i]+" ");
		}
		writer.println();
	}
}

class vector
{
	double[] vec;
	public vector(String s)
	{
		String[] str = s.split("\\s+");
		vec = new double[str.length];
		for(int i=0; i<str.length; i++)
		{
			if(str[i].length() != 0)
				vec[i] = Double.parseDouble(str[i]);
		}
	}
}