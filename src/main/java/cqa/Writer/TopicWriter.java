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

public class TopicWriter 
{
	static double[] f = new double[7]; 
	static double[] weights = new double[20];
	static String[] topic_words = new String[20];
	public static void main(String args[])
	{
		EmbeddingWriter ew = new EmbeddingWriter();
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
					int maxq = get_topmax(que_vec.vec);
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
					line = reader.readLine();
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
						int maxc = get_topmax(ans_vec.vec);
						System.out.println(spl[0]+" "+ans_vec.vec.length);
						f[0] = que_vec.vector_cos(que_vec, ans_vec);                         //cosine similarity of topic vectors                    
						f[1] = que_vec.vec_manhattan(que_vec, ans_vec);                      //manhattan distance of topic vectors
						f[2] = que_vec.Euclidean(que_vec, ans_vec);                          //euclidean distance of topic vectors
						f[3] = word_matcher(l, comment);
						f[4] = word_matcher(l2, comment);
						f[5] = weights[maxq] * weights[maxc];
						f[6] = word_matcher(topic_words[maxq], topic_words[maxc]);
						double[] sub = que_vec.vector_sub(que_vec, ans_vec);
						ew.SVM_writer(writer, label, sub, f);
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
