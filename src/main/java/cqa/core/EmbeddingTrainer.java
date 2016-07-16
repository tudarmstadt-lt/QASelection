package cqa.core;

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
/**
 * This class creates sentence vectors from trained word embeddings by Word2Vec
 * @author titas
 *
 */
public class EmbeddingTrainer 
{
	static int size = 100;              //word vector dimension
	static String input;
	public EmbeddingTrainer(String inp)
	{
		input = inp;	
	}
	/**
	 * This method initializes computation
	 */
	public static void initialize()
	{
		System.out.println("Sentence Embedding Training starts......");
		File inputFile = new File(input);
		File parent = inputFile.getParentFile();                        //get parent directory
		String pathgp = parent.getAbsolutePath();
		EmbeddingTrainerRun(pathgp+"/word2vec_files/word_vectors.txt", pathgp+"/parsed_files/train_clean.txt", pathgp+"/word2vec_files/vectors_train.txt");
		EmbeddingTrainerRun(pathgp+"/word2vec_files/word_vectors.txt", pathgp+"/parsed_files/test_clean.txt", pathgp+"/word2vec_files/vectors_test.txt");
	}
	public static void EmbeddingTrainerRun(String input1, String input2, String output)
	{
		File file = new File(input1);
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
				sentence_vector(map, input2, output);          // generate sentence vectors from word vectors
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	/**
	 * This class creates sentence vectors from word vectors by averaging word vectors per sentence
	 * @param map: HashMap of word and its word vector
	 * @param fread: input file to read
	 * @param fwrite: file to write
	 */
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
						translation(writer, map, question, comment);
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
	/**
	 * This method calculates a translation score by aligning words with maximum cosine similarity 
	 * @param writer: Writer object
	 * @param map: Map of word embeddings
	 * @param ques: question string
	 * @param ans: comment string
	 */
	public static void translation(PrintWriter writer, HashMap<String, vector> map, String ques, String ans)
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
	/**
	 * This method finds the cosine of two given vectors
	 * @param v1: question vector
	 * @param v2: comment vector
	 * @return The cosine value
	 */
	public static double vector_cos(vector v1, vector v2)                          //cosine score of vectors
	{
		double cos = vector_dot(v1, v2);
		if(cos != 0.0)
			cos = (vector_dot(v1, v2))/(Math.sqrt(vector_dot(v1, v1) * vector_dot(v2, v2)));
		return cos;
	}
	/**
	 * This method calculates the dot product of two vectors
	 * @param v1: question vector
	 * @param v2: comment vector
	 * @return Dot product of vectors
	 */
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
/**
 * This class creates a vector object from word embedding values
 * @author titas
 *
 */
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