package cqa.writer;
import java.io.BufferedWriter;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
/**
 * This class writes the SVM classification scores and labels in the format of the scorer script for SemEval 2016
 * @author titas
 *
 */
public class Writer 
{
	static int oiso = 0;
	static int zisz = 0;
	static int oisz = 0;
	static int ziso = 0;
	static ArrayList<String>oisz_arr = new ArrayList<>();
	static ArrayList<String>ziso_arr = new ArrayList<>();
	
	public static void main(String args[])
	{
		PrintWriter writer = null;
		BufferedReader reader = null;
		BufferedReader reader_2 = null;
		PrintWriter writer2 = null; 
		File file = new File(args[0]);
		File file_2 = new File(args[1]);
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter(args[2], false)));
			writer2 = new PrintWriter(new BufferedWriter(new FileWriter(args[3], false)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			reader = new BufferedReader(new FileReader(file));
			reader_2 = new BufferedReader(new FileReader(file_2));
			try {
				String str = reader.readLine();
				do
				{
					String[] qs = str.split("\\s+");
					String q_id = qs[0];
					int num = Integer.parseInt(qs[1]);
					String question = reader.readLine();
					writer2.println(str);
					writer2.println(question);
					for(int i=0; i<num; i++)
					{
						str = reader.readLine();
						String[] splited = str.split("\\s+");
						String c_id = splited[0];
						String label = splited[1];
						String comment = reader.readLine();
						String score_line = reader_2.readLine();
						splited = score_line.split("\\s+");
						String score = splited[1];
						String l = splited[0];
						if(Double.parseDouble(l) != binary_class(label))                  //error analysis
						{
							writer2.println(str);
							writer2.println(comment);
						}
						String bin_class = get_class(l);
						comp_class(label, Double.parseDouble(l), c_id);
						writer.println(q_id+" "+c_id+" 0 "+score+" "+bin_class);        //scorer script format
					}
				}
				while((str = reader.readLine())!=null);
				writer.close();
				writer2.close();
				System.out.println("bad classified as bad: "+ zisz);                   //misclassified points
				System.out.println("bad classified as good: "+ ziso);
				System.out.println("good classified as bad: "+ oisz);
				System.out.println("good classified as good: "+ oiso);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 * This method calculates misclassifications by the classifier
	 * @param gold: the gold label
	 * @param predict: the predicted label
	 * @param c_id: comment id
	 */
	public static void comp_class(String gold, double predict, String c_id)      //find misclassified comments
	{
		if(predict == 0.0)
		{
			if(binary_class(gold) == 0.0)
				zisz++;
			else
			{
				oisz++;
				oisz_arr.add(c_id);
			}
				
		}
		if(predict == 1.0)
		{
			if(binary_class(gold) == 1.0)
				oiso++;
			else
			{
				ziso++;
				ziso_arr.add(c_id);
			}
		}
	}
	
	public static double binary_class(String s)
	{
		if(s.equals("Good"))
		{
			return 1.0;
		}
		else
		{
			return 0.0;
		}
	}
	/**
	 * This method returns "true" if classification label was 1.0 else "false"
	 * @param s: the input label
	 * @return a String "true" or "false"
	 */
	public static String get_class(String s)
	{
		if(Double.parseDouble(s) == 1.0)
			return "true";
		else
			return "false";
	}
}
