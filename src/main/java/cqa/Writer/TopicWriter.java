package cqa.Writer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

import cqa.Feature_files.Java_files.Similarity_feature_generator;

public class TopicWriter 
{
	static double[] f = new double[5]; 
	static double[] weights = new double[20];
	static String[] topic_words = new String[20];
	public static void main(String args[])
	{
		embedding_writer ew = new embedding_writer();
		File file = new File(args[0]);
		File file2 = new File(args[1]);
		File file3 = new File(args[2]);
		BufferedReader reader = null;
		BufferedReader reader2 = null;
		BufferedReader reader3 = null;
		String line;
		int k=0;
		try {
			reader3 = new BufferedReader(new FileReader(file3));
			try {
				while((line = reader3.readLine()) != null)
				{
					String[] spl = line.split("\\s+", 3);
					weights[k] = Double.parseDouble(spl[1]);
					topic_words[k] = spl[2];
					k++;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PrintWriter writer = null;
		int q_id_rank = 0;
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter(args[3], false)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			reader = new BufferedReader(new FileReader(file));
			reader2 = new BufferedReader(new FileReader(file2));
			
			try {
				while((line = reader.readLine()) != null)
				{
					String splited[] = line.split("\\s+");
					int num = Integer.parseInt(splited[1]);
					line = reader2.readLine();
					String[] spl = line.split("\\s+", 3);
					vector que_vec = new vector(spl[2]);
					double posq = findKthLargest(que_vec.vec, 10);
					String words = "";
					String non = "";
					for(int i=0; i<que_vec.vec.length; i++)
					{
						if(que_vec.vec[i] >= posq)
						{
							words += topic_words[i]+" ";
						}
						else
						{
							non += topic_words[i]+" ";
						}
					}
					String l = deDup(words);
					String l2 = deDup(non);
					System.out.println(l);
					line = reader.readLine();
					q_id_rank++;
					for(int i=0; i<num; i++)
					{
						line = reader.readLine();
						splited = line.split("\\s+");
						String c_id = splited[0];
						String label = splited[1];
						String comment = reader.readLine();
						line = reader2.readLine();
						spl = line.split("\\s+", 3);
						vector ans_vec = new vector(spl[2]);
						if(ans_vec.vec.length < 20)
						{
							spl = line.split("\\s+", 2);
							ans_vec = new vector(spl[1]);
						}
						System.out.println(spl[0]+" "+ans_vec.vec.length);
						f[0] = que_vec.vector_cos(que_vec, ans_vec);
						f[1] = que_vec.vec_manhattan(que_vec, ans_vec);
						f[2] = que_vec.Euclidean(que_vec, ans_vec);
						f[3] = word_matcher(l, comment);
						f[4] = word_matcher(l2, comment);
						double[] mul = que_vec.vector_sub(que_vec, ans_vec);
						//ew.RankLib_writer(writer, label, q_id_rank, c_id, mul, f);
						ew.SVM_writer(writer, label, mul, f);
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
	public static String deDup(String s) 
	{
	    return Arrays.stream(s.split("\\s+")).distinct().collect(Collectors.joining(" "));
	}
	public static int get_topmax(double[] vec)
	{
		double max = 0.0;
		int pos = 0;
		for(int i=0; i<vec.length; i++)
		{
			if(vec[i] > max)
			{
				max = vec[i];
				pos = i;
			}
		}
		return pos;
	}
	public static double word_matcher(String s1, String s2)
	{
		double val=0.0;
		String[] sp1 = s1.split("\\s+");
		String[] sp2 = s2.split("\\s+");
		for(String words: sp1)
		{
			for(String top: sp2)
			{
				if(words.equals(top))
				{
					val += 1.0;
				}
			}
		}
		return val;
	}
	public static double findKthLargest(double[] nums, int k) {
	    PriorityQueue<Double> q = new PriorityQueue<Double>(k);
	    for(double i: nums){
	        q.offer(i);
	 
	        if(q.size()>k){
	            q.poll();
	        }
	    }
	 
	    return q.peek();
	}
}
