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
				HashMap<String, vector> map = new HashMap<>();    //HashMap to store vectors for each word
				while((l = reader.readLine())!= null)
				{
					String splited[] = l.split("\\s+", 2);
					String word = splited[0];
					vector v = new vector(splited[1]);
					map.put(word, v);
				}
				sentence_vector(map, args[1], args[2]);          // generate sentence vectors from word vectors
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
					String[] str = q_id.split("\\s+");
					int num = Integer.parseInt(str[1]);
					String question = reader.readLine();
					str = question.split("\\s+");
					init(writer, q_id, "");
					calculate_avg(writer, str, map);        //use average to calculate sentence vectors
					for(int i=0; i<num; i++)
					{
						String st = reader.readLine();
						String[] splited = st.split("\\s+");
						String c_id = splited[0];
						String label = splited[1];
						String comment = reader.readLine();
						str = comment.split("\\s+");
						init(writer, c_id, label);
						trans(writer, map, question, comment);
						calculate_avg(writer, str, map);
					}
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
	public static void init(PrintWriter writer, String id, String label)
	{
		if(!label.isEmpty())
			writer.print(id+" "+label+" ");
		else
			writer.print(id+" ");
	}
	public static void trans(PrintWriter writer, HashMap<String, vector> map, String ques, String ans)
	{
		String[] que_list = ques.split("\\s+");
		String[] ans_list = ans.split("\\s+");
		ArrayList<Double> d = new ArrayList<>();
		for(String qword: que_list)
		{
			vector qv = map.get(qword);
			if(qv != null)
			{
				double max_cos = 0.0;
				for(String aword: ans_list)
				{
					vector qa = map.get(aword);
					if(qa != null)
					{
						double pre_cos = vector_cos(qv, qa);
						if(pre_cos > max_cos)
						{
							max_cos = pre_cos;
						}
					}
				}
				d.add(max_cos);
			}	
		}
		double val=0.0;
		for(int i=0; i<d.size(); i++)
		{
			val+= d.get(i);
		}
		if(!d.isEmpty())
			writer.print((val/d.size())+" ");
		else
			writer.print("0.0 ");
	}
	public static double vector_cos(vector v1, vector v2)                          //cosine score of vectors
	{
		double cos = vector_dot(v1, v2);
		if(cos != 0.0)
			cos = (vector_dot(v1, v2))/(Math.sqrt(vector_dot(v1, v1) * vector_dot(v2, v2)));
		return cos;
	}
	public static double vector_dot(vector v1, vector v2)                           //vector dot product
	{
		double sum = 0.0;
		for(int i=0; i<v1.vec.length; i++)
		{
			sum += v1.vec[i] * v2.vec[i];
		}
		return sum;
	}
	public static void calculate_avg(PrintWriter writer, String[] str, HashMap<String, vector> map)         //calculate average of word vectors
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
				unnoticed++;
			}
		}
		
		for(int i=0; i<vec.length; i++)
		{
			if(count != 0)
				vec[i]/= count;
			writer.print(vec[i]+" ");
		}
		writer.println();
	}
}

class vector                                                                 //vector class
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