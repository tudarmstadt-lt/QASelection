package cqa.Feature_files.Java_files;

import java.io.BufferedWriter;
import java.io.File;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;

import info.debatty.java.stringsimilarity.CharacterSubstitutionInterface;
import info.debatty.java.stringsimilarity.Cosine;
import info.debatty.java.stringsimilarity.Damerau;
import info.debatty.java.stringsimilarity.Jaccard;
import info.debatty.java.stringsimilarity.JaroWinkler;
import info.debatty.java.stringsimilarity.KShingling;
import info.debatty.java.stringsimilarity.Levenshtein;
import info.debatty.java.stringsimilarity.LongestCommonSubsequence;
import info.debatty.java.stringsimilarity.NGram;
import info.debatty.java.stringsimilarity.NormalizedLevenshtein;
import info.debatty.java.stringsimilarity.QGram;
import info.debatty.java.stringsimilarity.SorensenDice;
import info.debatty.java.stringsimilarity.WeightedLevenshtein;

public class Similarity_normalizer         //File generating various string features
{
	public static void main(String[] args)
	{
		File file = new File("/mnt/Titas/1_QA_MODEL/SemEval_Tasks/CQA/QASelection/src/main/java/cqa/Xml_reader/parsed_file.txt");
		BufferedReader reader = null;
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter("/mnt/Titas/1_QA_MODEL/SemEval_Tasks/CQA/QASelection/src/main/java/cqa/Feature_files/Data_format_files/SVM/Binary/SVM_file_binary.txt", false)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		int q_id_rank = 0;
		try {
			reader = new BufferedReader(new FileReader(file));
			try {
				String q_id = reader.readLine();
				do
				{
					double[][] arr = new double[15][10];
					String[] labels = new String[10];
					String[] cid = new String[10];
					String question = reader.readLine();
					q_id_rank++;
					for(int i=0; i<10; i++)
					{
						String str = reader.readLine();
						String[] splited = str.split("\\s+");
						String c_id = splited[0];
						cid[i] = c_id;
						String label = splited[1];
						labels[i] = label;
						String comment = reader.readLine();
						 arr[0][i] = ngram(question, comment, 2);
						 arr[1][i] = ngram(question, comment, 3);
						 arr[2][i] = cosine(question, comment, 2);
						 arr[3][i] = cosine(question, comment, 3);
						 arr[4][i] = Jaccard(question, comment, 2);
						 arr[5][i] = Jaccard(question, comment, 3);
						 arr[6][i] = QGram(question, comment, 2);
						 arr[7][i] = QGram(question, comment, 3);
						 arr[8][i] = Sorensen(question, comment, 2);
						 arr[9][i] = Sorensen(question, comment, 3);
						 arr[10][i] = JaroWinkler(question, comment);
						 arr[11][i] = Damerau(question, comment);
						 arr[12][i] = Levenshtein(question, comment);
						 arr[13][i] = NormalizedLevenshtein(question, comment);
						 arr[14][i] = LCS(question, comment);
						 
					}
					normalize(arr,15);
					//RankLib_writer(writer, labels, q_id_rank, cid, arr);
					SVM_writer(writer, labels, 1, arr);
				}
				while((q_id = reader.readLine())!=null);
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	public static void RankLib_writer(PrintWriter writer, String[] label, int q_id_rank, String[] c_id, double[][] arr)
	{
		for(int i=0; i<10; i++)
		{
			writer.println(get_Label_value(label[i])+" "+"qid:"+q_id_rank+" 1:"+arr[0][i]+" 2:"+arr[1][i]+" 3:"+arr[2][i]+" 4:"+arr[3][i]+" 5:"+arr[4][i]+" 6:"+arr[5][i]+" 7:"+arr[6][i]+" 8:"+arr[7][i]+" 9:"+arr[8][i]+" 10:"+arr[9][i]+" 11:"+arr[10][i]+" 12:"+arr[11][i]+" 13:"+arr[12][i]+" 14:"+arr[13][i]+" 15:"+arr[14][i]+" # "+c_id[i]);
		}
	}
	public static void SVM_writer(PrintWriter writer, String[] label, int flag, double[][] arr)
	{
		if(flag == 0)
		{
			for(int i=0; i<10; i++)
			{
				writer.println(get_Label_value(label[i])+" "+"1:"+arr[0][i]+" 2:"+arr[1][i]+" 3:"+arr[2][i]+" 4:"+arr[3][i]+" 5:"+arr[4][i]);
			}
		}
		else
		{
			for(int i=0; i<10; i++)
			{
				writer.println(binary_class(label[i])+" "+"1:"+arr[0][i]+" 2:"+arr[1][i]+" 3:"+arr[2][i]+" 4:"+arr[3][i]+" 5:"+arr[4][i]+" 6:"+arr[5][i]+" 7:"+arr[6][i]+" 8:"+arr[7][i]+" 9:"+arr[8][i]+" 10:"+arr[9][i]+" 11:"+arr[10][i]+" 12:"+arr[11][i]+" 13:"+arr[12][i]+" 14:"+arr[13][i]+" 15:"+arr[14][i]);
			}
		}
	}
	public static int get_Label_value(String s)
	{
		if(s.equals("Good"))
		{
			return 1;
		}
		else if(s.equals("PotentiallyUseful"))
		{
			return 2;
		}
		return 3;
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
	public static double ngram(String s1, String s2, int n)
	{
		NGram ngram = new NGram(n);
		return ngram.distance(s1, s2);
	}
	public static double cosine(String s1, String s2, int n)
	{
		Cosine cos = new Cosine(n);
		if(Double.isNaN(cos.similarity(s1,s2)))
		{
			return 0.0;
		}
		return cos.similarity(s1, s2);
	}
	public static double Jaccard(String s1, String s2, int n)
	{
		Jaccard j2 = new Jaccard(n);
		return j2.similarity(s1, s2);
	}
	public static double QGram(String s1, String s2, int n)
	{
		QGram dig = new QGram(n);
		return dig.distance(s1, s2);
	}
	public static double Sorensen(String s1, String s2, int n)
	{
		SorensenDice sd = new SorensenDice(n);
		return sd.similarity(s1, s2);
	}
	public static double JaroWinkler(String s1, String s2)
	{
		JaroWinkler jw = new JaroWinkler();
		return jw.similarity(s1, s2);
	}
	public static double Damerau(String s1, String s2)
	{
		Damerau damerau = new Damerau();
		return damerau.distance(s1, s2);
	}
	public static double Levenshtein(String s1, String s2)
	{
		Levenshtein levenshtein = new Levenshtein();
		return levenshtein.distance(s1, s2);
	}
	public static double NormalizedLevenshtein(String s1, String s2)
	{
		NormalizedLevenshtein l = new NormalizedLevenshtein();
		return l.distance(s1, s2);
	}
	public static double LCS(String s1, String s2)
	{
		LongestCommonSubsequence lcs = new LongestCommonSubsequence();
		return lcs.distance(s1, s2);
	}
	public static void normalize(double[][] arr, int n)
	{
		for(int i=0; i<n; i++)
		{
			ArrayList<Double> d = new ArrayList<>();
			for(int j=0; j<10; j++)
			{
				d.add(arr[i][j]);
			}
			double max = Collections.max(d);
			double min = Collections.min(d);
			for(int j=0; j<10; j++)
			{
				if((max - min)!=0)
					arr[i][j] = (arr[i][j] - min)/ (max - min);
			}
		}
	}
}

