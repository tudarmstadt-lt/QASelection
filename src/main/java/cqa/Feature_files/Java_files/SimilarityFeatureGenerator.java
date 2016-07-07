package cqa.Feature_files.Java_files;



import java.io.BufferedWriter;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import info.debatty.java.stringsimilarity.Cosine;
import info.debatty.java.stringsimilarity.Damerau;
import info.debatty.java.stringsimilarity.Jaccard;
import info.debatty.java.stringsimilarity.JaroWinkler;
import info.debatty.java.stringsimilarity.Levenshtein;
import info.debatty.java.stringsimilarity.LongestCommonSubsequence;
import info.debatty.java.stringsimilarity.NGram;
import info.debatty.java.stringsimilarity.NormalizedLevenshtein;
import info.debatty.java.stringsimilarity.QGram;
import info.debatty.java.stringsimilarity.SorensenDice;

public class SimilarityFeatureGenerator         //File generating various string features
{
	static double[] f = new double[20];
	public static void main(String[] args)
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
					String[] qs = str.split("\\s+");
					String q_id = qs[0];
					int num = Integer.parseInt(qs[1]);
					String question = reader.readLine();
					for(int i=0; i<num; i++)
					{
						str = reader.readLine();
						String[] splited = str.split("\\s+");
						String c_id = splited[0];
						String label = splited[1];
						String comment = reader.readLine();
						 f[0] = ngram(question, comment, 1);
						 f[1] = ngram(question, comment, 2);
						 f[2] = ngram(question, comment, 3);
						 f[3] = cosine(question, comment,1);
						 f[4] = cosine(question, comment, 2);
						 f[5] = cosine(question, comment, 3);
						 f[6] = Jaccard(question, comment, 1);
						 f[7] = Jaccard(question, comment, 2);
						 f[8] = Jaccard(question, comment, 3);
						 f[9] = QGram(question, comment, 1);
						 f[10] = QGram(question, comment, 2);
						 f[11] = QGram(question, comment, 3);
						 f[12] = Sorensen(question, comment, 1);
						 f[13] = Sorensen(question, comment, 2);
						 f[14] = Sorensen(question, comment, 3);
						 f[15] = JaroWinkler(question, comment);
						 f[16] = Damerau(question, comment);
						 f[17] = Levenshtein(question, comment);
						 f[18] = NormalizedLevenshtein(question, comment);
						 f[19] = LCS(question, comment);
//						 if(question.length() != 0)
//							 f[20] = matcher(one_list, aone_list)/question.length();
//						 else
//							 f[20] = 0.0;
//						 if(comment.length() != 0)
//							 f[21] = matcher(one_list, aone_list)/comment.length();
//						 else
//							 f[21] = 0.0;
//						 f[16] = matcher(two_list, atwo_list);
//						 f[17] = matcher(three_list, athree_list);
						 SVM_writer(writer, label, 1);
					}					
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
	public static void SVM_writer(PrintWriter writer, String label, int flag)       //SVM file writer
	{
		if(flag == 0)
		{
			writer.print(get_Label_value(label)+" ");
			for(int i=0; i<f.length; i++)
			{
				writer.print((i+1)+":"+f[i]+" ");
			}
			writer.println();
		}
		else
		{
			writer.print(binary_class(label)+" ");
			for(int i=0; i<f.length; i++)
			{
				writer.print((i+1)+":"+f[i]+" ");
			}
			writer.println();
		}
	}
	public static double matcher(List<String> que_list, List<String> com_list)
	{
		int n_gram_count = 0;
		for(int j=0; j<que_list.size(); j++)
		{
			if(!que_list.get(j).isEmpty() && com_list.contains(que_list.get(j)))
			{
				n_gram_count++;
			}
		}
		return n_gram_count*1.0;
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
	public static double ngram(String s1, String s2, int n)        //ngram score
	{
		NGram ngram = new NGram(n);
		return ngram.distance(s1, s2);
	}
	public static double cosine(String s1, String s2, int n)       //cosine score
	{
		Cosine cos = new Cosine(n);
		if(Double.isNaN(cos.similarity(s1,s2)))
		{
			return 0.0;
		}
		return cos.similarity(s1, s2);
	}
	public static double Jaccard(String s1, String s2, int n)       //Jaccard score
	{
		Jaccard j2 = new Jaccard(n);
		if(Double.isNaN(j2.similarity(s1,s2)))
		{
			return 0.0;
		}
		return j2.similarity(s1, s2);
	}
	public static double QGram(String s1, String s2, int n)           //QGram score
	{
		QGram dig = new QGram(n);
		return dig.distance(s1, s2);
	}
	public static double Sorensen(String s1, String s2, int n)         //Sorensen score
	{
		SorensenDice sd = new SorensenDice(n);
		if(Double.isNaN(sd.similarity(s1,s2)))
		{
			return 0.0;
		}
		return sd.similarity(s1, s2);
	}
	public static double JaroWinkler(String s1, String s2)				//JaroWinkler score
	{
		JaroWinkler jw = new JaroWinkler();
		return jw.similarity(s1, s2);
	}
	public static double Damerau(String s1, String s2)					//Damerau score
	{
		Damerau damerau = new Damerau();
		return damerau.distance(s1, s2);
	}
	public static double Levenshtein(String s1, String s2)				//Levenshtein score
	{
		Levenshtein levenshtein = new Levenshtein();
		return levenshtein.distance(s1, s2);
	}
	public static double NormalizedLevenshtein(String s1, String s2)		//Normalized Levenshtein score
	{
		NormalizedLevenshtein l = new NormalizedLevenshtein();
		if(Double.isNaN(l.distance(s1,s2)))
		{
			return 0.0;
		}
		return l.distance(s1, s2);
	}
	public static double LCS(String s1, String s2)							// LCS score
	{
		LongestCommonSubsequence lcs = new LongestCommonSubsequence();
		if(Double.isNaN(lcs.distance(s1,s2)))
		{
			return 0.0;
		}
		return lcs.distance(s1, s2);
	}
}
