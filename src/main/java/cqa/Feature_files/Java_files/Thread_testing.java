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

public class Thread_testing
{
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
		int q_id_rank = 0;
		try {
			reader = new BufferedReader(new FileReader(file));
			try {
				String q_id = reader.readLine();
				String[] good_words = {"good","qatar","u","visa","doha","time","2","people","school","company"};            //good class words
				String[] bad_words = {"u","qatar","good","visa","doha","time","people","lol","work","back"};                //bad class words
				String[] punc = {"!", "#", "$", "%", "^", "&", "*", "(", ")", "-", "_", "+", "=", ":", ";", ".", "/", "<", ">", "{", "}", "[", "]", "~", "\\"};
				do
				{
					double[][] arr = new double[5][10];
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
						//System.out.println(comment);
						arr[0][i] = URL_matcher(comment, c_id) + email_matcher(comment, c_id) + special_character_matcher("img", comment);
						arr[1][i] = special_word_matcher(good_words, comment);
						arr[2][i] = special_word_matcher(bad_words, comment);
						arr[3][i] = special_character_matcher("?", comment) + special_character_matcher("@", comment) + punc_matcher(punc, comment);
						arr[4][i] = length_matcher(comment);
						//arr[5][i] = punc_matcher(punc, comment);
					}

					RankLib_writer(writer, labels, q_id_rank, cid, arr);
					//SVM_writer(writer, labels, 1, arr);
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
			writer.println(get_Label_value(label[i])+" "+"qid:"+q_id_rank+" 1:"+arr[0][i]+" 2:"+arr[1][i]+" 3:"+arr[2][i]+" 4:"+arr[3][i]+" 5:"+arr[4][i]+" # "+c_id[i]);
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
				writer.println(binary_class(label[i])+" "+"1:"+arr[0][i]+" 2:"+arr[1][i]+" 3:"+arr[2][i]+" 4:"+arr[3][i]+" 5:"+arr[4][i]);
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
	public static double special_word_matcher(String[] to_match, String comment)           //match special words
	{
		String[] str = comment.replaceAll("[^a-zA-Z0-9 ]", "").toLowerCase().split("\\s+");
		double val = 0.0;
		for(String words : to_match)
		{
			for(String comm : str)
			{
				if(words.equals(comm))
				{
					//System.out.println(comment);
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
	public static double punc_matcher(String[] to_match, String comment)
	{
		int count = 0;
		for(String punc: to_match)
		{
			count+= comment.split(Pattern.quote(punc), -1).length - 1;
		}
		return 0.1*count;
	}
	public static int begin_matcher(String to_match, String comment)
	{
		String[] str = comment.replaceAll("[^a-zA-Z0-9 ]", "").toLowerCase().split("\\s+");
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
