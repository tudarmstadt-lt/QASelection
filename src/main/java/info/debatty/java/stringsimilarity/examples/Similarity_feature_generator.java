package info.debatty.java.stringsimilarity.examples;

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
		
		try {
			reader = new BufferedReader(new FileReader(file));
			NGram ngram = new NGram(3);
			Cosine cos = new Cosine(3);
			Jaccard j2 = new Jaccard(2);
			try {
				String q_id = reader.readLine();
				do
				{
					String question = reader.readLine();
					for(int i=0; i<10; i++)
					{
						String c_id = reader.readLine();
						String comment = reader.readLine();
						System.out.println(c_id);
						//System.out.println(question);
						//System.out.println(comment);
						System.out.println("n-gram score: "+ngram.distance(question, comment));
						System.out.println("cosine score: "+cos.similarity(question, comment));
						System.out.println("jaccard score: "+j2.similarity(question, comment));
					}
					System.out.println("----------------------------------------");
				}
				while((q_id = reader.readLine())!=null);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
