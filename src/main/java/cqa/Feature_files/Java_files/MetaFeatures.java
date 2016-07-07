package cqa.Feature_files.Java_files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class MetaFeatures                                      //Meta Data Features
{
	static double[] f = new double[6];
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
				String line;
				String[] ack = {"thankyou","thanks","thank","thanku"};          //Acknowledgement words
				String[] ack2 = {"appreciate", "appreciated"};
				try {
					while((line = reader.readLine()) != null)
					{
						String splited[] = line.split("\\s+", 4);
						int num = Integer.parseInt(splited[1]);
						String quser = splited[2];
						String qusername = splited[3].toLowerCase();
						line = reader.readLine();
						for(int i=0; i<num; i++)
						{
							line = reader.readLine();
							splited = line.split("\\s+", 4);
							String cuser = splited[2];
							String label = splited[1];
							String comment = reader.readLine();
							String[] spl = comment.split("\\s+");
							f[0] = (num-i)*1.0/num;
							f[1] = check_commentor(quser, cuser);
							f[2] = special_word_matcher(ack, comment);
							f[3] = special_word_matcher(ack2, comment);
							f[4] = check_commentor(spl[0],"yes");
							f[5] = spl.length;
							SVM_writer(writer, label, 1);
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				writer.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	public static double check_commentor(String s1, String s2)
	{
		if(s1.equals(s2))
			return 1.0;
		else
			return 0.0;
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
}
