package cqa.Writer;
import java.io.BufferedWriter;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

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
		BufferedReader reader_3 = null;
		File file = new File(args[0]);
		File file_2 = new File(args[1]);
		File file_3 = new File(args[2]);
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter(args[3], false)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			reader = new BufferedReader(new FileReader(file));
			reader_2 = new BufferedReader(new FileReader(file_2));
			reader_3 = new BufferedReader(new FileReader(file_3));
			try {
				String q_id = reader.readLine();
				do
				{
					String question = reader.readLine();
					for(int i=0; i<10; i++)
					{
						String str = reader.readLine();
						String[] splited = str.split("\\s+");
						String c_id = splited[0];
						String label = splited[1];
						String comment = reader.readLine();
						String score_line = reader_2.readLine();
						//System.out.println(score_line);
						splited = score_line.split("\\s+");
						String score = splited[2];
						String l = reader_3.readLine();
						String bin_class = get_class(l);
						comp_class(label, Double.parseDouble(l), c_id);
						writer.println(q_id+" "+c_id+" 0 "+score+" "+bin_class);
					}
				}
				while((q_id = reader.readLine())!=null);
				writer.close();
				System.out.println("bad classified as bad: "+ zisz);
				System.out.println("bad classified as good: "+ ziso);
//				for(int i=0; i<ziso_arr.size(); i++)
//				{
//					System.out.println(ziso_arr.get(i));
//				}
				System.out.println("good classified as bad: "+ oisz);
//				for(int i=0; i<oisz_arr.size(); i++)
//				{
//					System.out.println(oisz_arr.get(i));
//				}
				System.out.println("good classified as good: "+ oiso);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
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
	public static String get_bool(String s)
	{
		if(Double.parseDouble(s) == 0.0)
			return "false";
		return "true";
	}
	public static String get_class(String s)
	{
		if(Double.parseDouble(s) == 1.0)
			return "true";
		else
			return "false";
	}
}
