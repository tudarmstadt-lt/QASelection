package cqa.Writer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class embedding_writer 
{
	static double[] f = new double[3];
	public static void main(String args[])
	{
		File file = new File(args[0]);
		BufferedReader reader = null;
		PrintWriter writer = null;
		int q_id_rank = 0;
		double cos = 0.0;
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
					String splited[] = line.split("\\s+", 3);
					int num = Integer.parseInt(splited[1]);
					vector que_vec = new vector(splited[2]);
					q_id_rank++;
					for(int i=0; i<num; i++)
					{
						line = reader.readLine();
						splited = line.split("\\s+", 3);
						String c_id = splited[0];
						String label = splited[1];
						vector ans_vec = new vector(splited[2]);
						f[0] = que_vec.vector_cos(que_vec, ans_vec);
						f[1] = que_vec.vec_manhattan(que_vec, ans_vec);
						f[2] = que_vec.Euclidean(que_vec, ans_vec);
						double[] sub = que_vec.vector_sub(que_vec, ans_vec);
						//RankLib_writer(writer, label, q_id_rank, c_id, sub);
						SVM_writer(writer, label, sub, f);
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
	public static void RankLib_writer(PrintWriter writer, String label, int q_id_rank, String c_id, double[] vec, double[] f)
	{
		int i;
		writer.print(get_Label_value(label)+" qid:"+q_id_rank+" ");
		for(i=0; i<vec.length; i++)
		{
			writer.print(i+1+":"+vec[i]+" ");
		}
		for(i=0; i<f.length; i++)
		{
			writer.print(i+1+":"+f[i]+" ");
		}
		writer.println(" # "+c_id);
	}
	public static void SVM_writer(PrintWriter writer, String label, double[] vec, double[] f)
	{
		int i;
		writer.print(binary_class(label)+" ");
		for(i=0; i<vec.length; i++)
		{
			writer.print(i+1+":"+vec[i]+" ");
		}
		for(i=0; i<f.length; i++)
		{
			writer.print(i+1+":"+f[i]+" ");
		}
		writer.println();
	}
	public static int get_Label_value(String s)
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
}

class vector                                //find scoring vectors and cosine of question and comment vectors
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
	public double[] vector_sub(vector v1, vector v2)
	{
		double[] sub = new double[v1.vec.length];
		for(int i=0; i<v1.vec.length; i++)
		{
			sub[i] = v1.vec[i] - v2.vec[i];
		}
		return sub;
	}
	public double[] vector_mul(vector v1, vector v2)
	{
		double[] mul = new double[v1.vec.length];
		for(int i=0; i<v1.vec.length; i++)
		{
			mul[i] = v1.vec[i] * v2.vec[i];
		}
		return mul;
	}
	public double vector_cos(vector v1, vector v2)
	{
		double cos = vector_dot(v1, v2);
		if(cos != 0.0)
			cos = (vector_dot(v1, v2))/(Math.sqrt(vector_dot(v1, v1) * vector_dot(v2, v2)));
		return cos;
	}
	public double vec_manhattan(vector v1, vector v2)
	{
		double sum = 0.0;
		for(int i=0; i<v1.vec.length; i++)
		{
			sum+= Math.abs(v1.vec[i] - v2.vec[i]);
		}
		return sum;
	}
	public double Euclidean(vector v1, vector v2)
	{
		double result = 0.0;
		for(int i=0; i<v1.vec.length; i++)
		{
			result+= (v1.vec[i] - v2.vec[i])*(v1.vec[i] - v2.vec[i]);
		}
		return Math.sqrt(result);
	}
	public double vector_dot(vector v1, vector v2)
	{
		double sum = 0.0;
		for(int i=0; i<v1.vec.length; i++)
		{
			sum += v1.vec[i] * v2.vec[i];
		}
		return sum;
	}
}