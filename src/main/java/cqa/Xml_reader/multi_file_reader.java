package cqa.Xml_reader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class multi_file_reader 
{
	public static void main(String args[])
	{
		String SVM_dir = args[0];
		String[] SVM_files_train = {SVM_dir+"SVM_file_binary.txt", SVM_dir+"SVM_thread_level.txt", SVM_dir+"SVM_embedding.txt"};
		String[] SVM_files_test = {SVM_dir+"SVM_test_file_binary.txt", SVM_dir+"SVM_thread_level_test.txt", SVM_dir+"SVM_embedding_test.txt"};
		String RankLib_dir = args[1];
		String[] RankLib_files_train = {RankLib_dir+"RankLib_file.txt", RankLib_dir+"RankLib_thread_level.txt", RankLib_dir+"RankLib_embedding.txt"};
		String[] RankLib_files_test = {RankLib_dir+"RankLib_test_file.txt", RankLib_dir+"RankLib_thread_level_test.txt", RankLib_dir+"RankLib_embedding_test.txt"};
		multireader(SVM_files_train, SVM_dir+"SVM_total.txt",1, 121);
		multireader(RankLib_files_train, RankLib_dir+"RankLib_total.txt", 0, 121);
		multireader(RankLib_files_test, RankLib_dir+"RankLib_total_test.txt", 0, 121);
		multireader(SVM_files_test, SVM_dir+"SVM_total_test.txt",1, 121);
	}
	public static void multireader(String[] input_dir, String output_file, int flag, int n)          //combine all data files and normalize
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
		int caller = 0;
		double[][] arr = new double[n][10]; 
		int[] labels = new int[10];
		String line;
		int breaker = 0;
		int q_id_rank = 0;
		try {
			while(true)
			{
				caller++;
				int count = 0;
				int start;
				for(int i=0; i<3; i++)
				{
					line = reader[i].readLine();
					if(line == null)
					{
						breaker = 1;
						break;
					}
					String[] splited = line.split("\\s+");
					if(flag == 0)
					{
						start = 2;
						for(int j=start; j<splited.length-2; j++)
						{
								//System.out.println(splited[j]);
							if(j<11)
							{
								arr[count][caller-1] = Double.parseDouble(splited[j].substring(2));
							}
							else if(j < 101)
							{
								arr[count][caller-1] = Double.parseDouble(splited[j].substring(3));
							}
							else
							{
								arr[count][caller-1] = Double.parseDouble(splited[j].substring(4));
							}
							count++;
						}
					}
					else
					{
						start = 1;
						for(int j=start; j<splited.length; j++)
						{
							//System.out.println(splited[j]);
							if(j<10)
							{
								arr[count][caller-1] = Double.parseDouble(splited[j].substring(2));
							}
							else if(j < 100)
							{
								arr[count][caller-1] = Double.parseDouble(splited[j].substring(3));
							}
							else
							{
								arr[count][caller-1] = Double.parseDouble(splited[j].substring(4));
							}
							count++;
						}
					}	
					if(i==0)
					{
						labels[caller-1] = Integer.parseInt(splited[0]);
					}
					
				}
				if(caller % 10 == 0)
				{
					q_id_rank++;
					normalize(arr, n);
					for(int i=0; i<10; i++)
					{
						if(flag == 0)
							writer.print(labels[i]+" qid:"+q_id_rank+" ");
						else
							writer.print(labels[i]+" ");
						for(int j=0; j<n; j++)
						{
							System.out.print((j+1)+":"+arr[j][i]+" ");
							writer.print((j+1)+":"+arr[j][i]+" ");
						}
						writer.println();
					}
					System.out.println();
					arr = new double[n][10];
					labels = new int[10];
					caller = 0;
				}
				if(breaker == 1)
				{
					break;
				}
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void normalize(double[][] arr, int n)                   //normalization code
	{
		for(int i=0; i<n; i++)
		{
			double[] data = new double[10];
			for(int j=0; j<10; j++)
			{
				data[j] = arr[i][j];
			}
			Statistics a = new Statistics(data);
			if(a.getStdDev() != 0.0)
			{
				for(int j=0; j<10; j++)
				{
					
						arr[i][j] = (arr[i][j] - a.getMean())/a.getStdDev();
				}
			}
			
//			double max = Collections.max(d);
//			double min = Collections.min(d);
//			for(int j=0; j<10; j++)
//			{
//				if((max - min)!=0)
//					arr[i][j] = (arr[i][j] - min)/ (max - min);
//			}
		}
	}
}

class Statistics 
{
    double[] data;
    int size;   

    public Statistics(double[] data) 
    {
        this.data = data;
        size = data.length;
    }   

    double getMean()
    {
        double sum = 0.0;
        for(double a : data)
            sum += a;
        return sum/size;
    }

    double getVariance()
    {
        double mean = getMean();
        double temp = 0;
        for(double a :data)
            temp += (mean-a)*(mean-a);
        return temp/size;
    }

    double getStdDev()
    {
        return Math.sqrt(getVariance());
    }
}
