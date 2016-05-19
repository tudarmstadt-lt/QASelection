package example;



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

public class Similarity_feature_generator         //File generating various string features
{
	public static void main(String[] args)
	{
		File file = new File("/mnt/Titas/1_QA_MODEL/SemEval_Tasks/CQA/QASelection/src/main/java/info/debatty/java/stringsimilarity/examples/parsed_file.txt");
		BufferedReader reader = null;
		PrintWriter writer = null;
		int q_id_rank = 0;
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter("/mnt/Titas/1_QA_MODEL/SemEval_Tasks/CQA/QASelection/src/main/java/info/debatty/java/stringsimilarity/examples/RankLib_file.txt", false)));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			reader = new BufferedReader(new FileReader(file));
			NGram ngram = new NGram(3);
			Cosine cos = new Cosine(3);
			Jaccard j2 = new Jaccard(3);
			JaroWinkler jw = new JaroWinkler();
			Levenshtein levenshtein = new Levenshtein();
			Damerau damerau = new Damerau();
			NormalizedLevenshtein l = new NormalizedLevenshtein();
			LongestCommonSubsequence lcs = new LongestCommonSubsequence();
			SorensenDice sd = new SorensenDice(3);
			QGram dig = new QGram(3);
			try {
				String q_id = reader.readLine();
				do
				{
					String question = reader.readLine();
					q_id_rank++;
					for(int i=0; i<10; i++)
					{
						String str = reader.readLine();
						String[] splited = str.split("\\s+");
						String c_id = splited[0];
						String label = splited[1];
						String comment = reader.readLine();
						System.out.println(c_id);
						//System.out.println(question);
						//System.out.println(comment);
						System.out.println("n-gram score: "+ngram.distance(question, comment));
						System.out.println("cosine score: "+cos.similarity(question, comment));
						System.out.println("jaccard score: "+j2.similarity(question, comment));
						System.out.println("levenshtein score: "+levenshtein.distance(question, comment));
						System.out.println("jaro-wrinkler score: "+jw.similarity(question, comment));
						System.out.println("Damerau score: "+damerau.distance(question, comment));
						System.out.println("LCS score: "+lcs.distance(question, comment));
						System.out.println("Normalized Levenshtein score: "+l.distance(question, comment));
						System.out.println("q-gram score: "+dig.distance(question, comment));
						System.out.println("Sorensen score: "+sd.similarity(question, comment));
						double f_1 = ngram(question, comment, 3);
						double f_2 = cosine(question, comment, 3);
						double f_3 = Jaccard(question, comment, 3);
						double f_4 = QGram(question, comment, 3);
						double f_5 = Sorensen(question, comment, 3);
						double f_6 = JaroWinkler(question, comment);
						double f_7 = Damerau(question, comment);
						double f_8 = Levenshtein(question, comment);
						double f_9 = NormalizedLevenshtein(question, comment);
						double f_10 = LCS(question, comment);
						writer.println(get_Label_value(label)+" "+"qid:"+q_id_rank+" 1:"+f_1+" 2:"+f_2+" 3:"+f_3+" 4:"+f_4+" 5:"+f_5+" 6:"+f_6+" 7:"+f_7+" 8:"+f_8+" 9:"+f_9+" 10:"+f_10+" # "+c_id);
						
					}
					System.out.println("----------------------------------------");
				}
				while((q_id = reader.readLine())!=null);
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
			System.out.println("Nan");
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
}
