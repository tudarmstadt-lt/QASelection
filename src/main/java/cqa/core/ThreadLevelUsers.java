package cqa.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import cqa.writer.SVMWriter;
/**
 * This class finds out if a comment is by the asker of the question and is an ack or not, or a question or not
 * @author titas
 *
 */
public class ThreadLevelUsers 
{
	static double[] f = new double[4];
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
				String str = reader.readLine();
				do
				{
					String[] splited = str.split("\\s+",4);
					String q_id = splited[0];
					int num = Integer.parseInt(splited[1]);
					String asker_id = splited[2];
					String asker_name = splited[3];
					String question = reader.readLine();
					for(int i=0; i<num; i++)
					{
						str = reader.readLine();
						splited = str.split("\\s+", 4);
						String commenter_id = splited[2];
						String commenter_name = splited[3];
						String label = splited[1];
						String comment = reader.readLine();
						f[0] = get_same(asker_id, commenter_id);
						f[1] = get_same_ack(asker_id, commenter_id, comment);
						f[2] = get_same_que(asker_id, commenter_id, comment);
						f[3] = get_same_imp(asker_id, commenter_id, comment);
						SVMWriter w = new SVMWriter(writer, label, 1, f);
						w.write();
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

	public static double get_same(String asker, String commenter)              //check if comment is by asker
	{
		if(asker.equals(commenter))
		{
			return 1.0;
		}
		return 0.0;
	}
	public static int get_length(String comment)
	{
		return comment.length();
	}
	/**
	 * Checks if a comment by the asker is an acknowledgement
	 * @param asker: asker of the question
	 * @param commenter: user who commented
	 * @param comment: comment string
	 * @return binary value
	 */
	public static double get_same_ack(String asker, String commenter, String comment)             //check if a comment by the asker is a acknowledgement
	{
		if(get_same(asker, commenter) == 0)
		{
			return 0.0;
		}
		String[] ack_words = {"thanks", "thank", "thankyou", "appreciate", "appreciated"};
		String[] str = comment.replaceAll("[^a-zA-Z0-9 ]", " ").toLowerCase().split("\\s+");
		for(String words: str)
		{
			for(String ack: ack_words)
			{
				if(ack.equals(words))
				{
					return 1.0;
				}
			}
		}
		return 0.0;
	}
	/**
	 * Checks if a comment by the asker is a further question
	 * @param asker: asker of the question
	 * @param commenter: user who commented
	 * @param comment: comment string
	 * @return binary value
	 */
	public static double get_same_que(String asker, String commenter, String comment)       //check if a comment by the asker is a question
	{
		if(get_same(asker, commenter) == 0)
		{
			return 0.0;
		}
		if(comment.contains("?"))
		{
			return 1.0;
		}
		return 0.0;
	}
	/**
	 * Checks if a comment by the asker is neither acknowledgement nor question
	 * @param asker: asker of the question
	 * @param commenter: user who commented
	 * @param comment: comment string
	 * @return binary value
	 */
	public static double get_same_imp(String asker, String commenter, String comment)
	{
		if(get_same(asker, commenter) == 1 && get_same_ack(asker, commenter, comment) == 0 && get_same_que(asker, commenter, comment) == 0)
		{
			return 1.0;
		}
		return 0.0;
	}
}
