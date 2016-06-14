package cqa.Xml_reader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class multiple_file_reader 
{
	public static void main(String args[])
	{
		String SVM_dir = args[0];
		String[] SVM_files_train = {SVM_dir+"SVM_file_binary.txt", SVM_dir+"SVM_thread_level.txt", SVM_dir+"SVM_temp.txt"};
		String[] SVM_files_test = {SVM_dir+"SVM_test_file_binary.txt", SVM_dir+"SVM_thread_level_test.txt", SVM_dir+"SVM_temp_test.txt"};
		String RankLib_dir = args[1];
		String[] RankLib_files_train = {RankLib_dir+"RankLib_file.txt", RankLib_dir+"RankLib_thread_level.txt", RankLib_dir+"RankLib_temp.txt"};
		String[] RankLib_files_test = {RankLib_dir+"RankLib_test_file.txt", RankLib_dir+"RankLib_thread_level_test.txt", RankLib_dir+"RankLib_temp_test.txt"};
		multireader(SVM_files_train, SVM_dir+"SVM_total.txt",1);
		multireader(RankLib_files_train, RankLib_dir+"RankLib_total.txt", 0);
		multireader(RankLib_files_test, RankLib_dir+"RankLib_total_test.txt", 0);
		multireader(SVM_files_test, SVM_dir+"SVM_total_test.txt",1);
	}
	public static void multireader(String[] input_dir, String output_file, int flag)              //combine files without normalization
	{
		BufferedReader[] reader = new BufferedReader[3];
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter(output_file, false)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(int i=0; i<3; i++)
		{
			File file = new File(input_dir[i]);
			try {
				reader[i] = new BufferedReader(new FileReader(file));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String line;
		try {
			while((line = reader[0].readLine())!=null)
			{
				int count = 16;
				String[] str = line.split("\\s+");
				if(flag == 0)
				{
					for(int i=0; i<str.length-2 ; i++)
					{
						writer.print(str[i]+" ");
					}
				}
				else
					writer.print(line+" ");
				for(int i=1; i<2; i++)
				{
					line = reader[i].readLine();
					String[] splited = line.split("\\s+");
					if(flag == 0)
					{
						for(int j=2; j<splited.length-2; j++)
						{
							//System.out.println(splited[j]);
							if(j<11)
							{
								writer.print(count+":"+splited[j].substring(2)+" ");
							}
							else if(j < 101)
							{
								writer.print(count+":"+splited[j].substring(3)+" ");
							}
							else
							{
								writer.print(count+":"+splited[j].substring(4)+" ");
							}
							count++;
						}
					}
					else
					{
						for(int j=1; j<splited.length; j++)
						{
							if(j<10)
							{
								writer.print(count+":"+splited[j].substring(2)+" ");
							}
							else if(j < 100)
							{
								writer.print(count+":"+splited[j].substring(3)+" ");
							}
							else
							{
								writer.print(count+":"+splited[j].substring(4)+" ");
							}
							count++;
						}
					}
				}
				writer.println();
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
