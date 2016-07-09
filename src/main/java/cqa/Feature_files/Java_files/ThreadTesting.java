package cqa.Feature_files.Java_files;
import java.io.BufferedWriter;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ThreadTesting
{
	static double[] f = new double[5];
	public static void main(String args[])
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
				String q_id = reader.readLine();
				String[] good_words = {"get","good","qatar","go","one","visa","also","even","like","doha","need","would","know","try","best","think","work","take","find","people"};            //good class words on train set
				String[] bad_words = {"get","know","one","thanks","like","qatar","good","would","go","want","dont","think","need","also","much","find","time","people","please","work"};        //bad class words on train set
				String[] punc = {"!", "#", "$", "%", "^", "&", "*", "(", ")", "-", "_", "+", "=", ":", ";", ".", "/", "<", ">", "{", "}", "[", "]", "~", "\\"};									//punctuation list
				do
				{
					String[] qs = q_id.split("\\s+");
					int num = Integer.parseInt(qs[1]);
					String question = reader.readLine();
					for(int i=0; i<num; i++)
					{
						String str = reader.readLine();
						String[] splited = str.split("\\s+");
						String c_id = splited[0];
						String label = splited[1];
						String comment = reader.readLine();
						f[0] = URL_matcher(comment, c_id) + email_matcher(comment, c_id) + tag_matcher(comment, c_id);
						f[1] = special_word_matcher(good_words, comment);
						f[2] = special_word_matcher(bad_words, comment);
						f[3] = special_character_matcher("?", comment) + special_character_matcher("@", comment)+punc_matcher(punc,comment);
						f[4] = length_matcher(comment);
						SVM_writer(writer, label, 0);
					}
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
	public static double URL_matcher(String comment, String c_id)                     //match URLs
	{
		Pattern p = Pattern.compile("\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]",Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(comment);
        double val = 0.0;
        while(m.find())
        {
        	System.out.println("yes");
        	val+=0.1;
        }
        return val;
	}
	public static double email_matcher(String comment, String c_id)                  //match emails
	{
		Matcher m = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+").matcher(comment);
		double val = 0.0;
        while(m.find())
        {
        	val+=0.1;
        }
        return val;
	}
	public static double tag_matcher(String comment, String c_id)
	{
		
		Matcher m = Pattern.compile("<[^>]*>").matcher(comment);
		double val = 0.0;
        while(m.find())
        {
        	val+=0.1;
        }
        return val;
	}
	public static double special_word_matcher(String[] to_match, String comment)           //match special words
	{
		String[] str = comment.replaceAll("[^a-zA-Z0-9 ]", " ").toLowerCase().split("\\s+");
		double val = 0.0;
		for(int i=0; i<to_match.length; i++)
		{
			for(String comm : str)
			{
				if(to_match[i].equals(comm))
				{
					val+=0.1;
				}
			}
		}
		return val;
	}
	public static double special_character_matcher(String to_match, String comment)       //match special characters
	{
		int count = comment.split(Pattern.quote(to_match), -1).length - 1;
		return 0.1*count;
	}
	public static double punc_matcher(String[] to_match, String comment)                 //match punctuations
	{
		int count = 0;
		for(String punc: to_match)
		{
			count+= comment.split(Pattern.quote(punc), -1).length - 1;
		}
		return 0.1*count;
	}
	public static int begin_matcher(String to_match, String comment)                     //match beginning words
	{
		String[] str = comment.replaceAll("[^a-zA-Z0-9 ]", " ").toLowerCase().split("\\s+");
		if(str.length > 0 && str[0].equals(to_match))
		{
			//System.out.println(comment);
			return 1;
		}
		return 0;
	}
	public static int length_matcher(String comment)									//length of comment
	{
		return comment.length();
	}
}
