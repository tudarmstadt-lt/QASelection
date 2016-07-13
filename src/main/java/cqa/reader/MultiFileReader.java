package cqa.reader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
/**
 * This class combines all feature files into one file for SVM classification and z-score normalizes it
 * @author titas
 *
 */
public class MultiFileReader 
{
	static int num_features = 168;
	static ArrayList<Integer> length_auth = new ArrayList<>();
	static ArrayList<Integer> length_auth_test = new ArrayList<>();
	public static void main(String args[])
	{
		File SVM_dir_train = new File(args[0]);                 //path to train files' directory
		File SVM_dir_test = new File(args[1]);                  //path to test files' directory
		File[] files_train = SVM_dir_train.listFiles();
		File[] files_test = SVM_dir_test.listFiles();
		String[] SVM_files_train = new String[files_train.length];
		String[] SVM_files_test = new String[files_test.length];
		for(int i=0; i<files_train.length; i++)
		{
			SVM_files_train[i] = args[0]+"/"+files_train[i].getName();
		}
		for(int i=0; i<files_test.length; i++)
		{
			SVM_files_test[i] = args[1]+"/"+files_test[i].getName();
		}
		len_cal(args[2], args[3]);
		multireader(SVM_files_train, args[0]+"/SVM_train.txt", num_features, 0, files_train.length);
		multireader(SVM_files_test, args[1]+"/SVM_test.txt", num_features, 1, files_test.length);
	}
	/**
	 * This method reads multiple train or test SVM data files and combines them in one
	 * @param input_dir: The path to input directory
	 * @param output_file: The path to output file
	 * @param n: The number of features
	 * @param flag: Flag for train or test files
	 * @param num: Number of files in the directory
	 */
	public static void multireader(String[] input_dir, String output_file, int n, int flag, int num)          //combine all data files and normalize
	{
		ArrayList<Integer> l;
		
		if(flag == 0)
		{
			l = length_auth;
		}
		else
		{
			l = length_auth_test;
		}
		BufferedReader[] reader = new BufferedReader[num];
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter(output_file, false)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(int i=0; i<num; i++)
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
		double[][] arr = new double[n][l.get(0)]; 
		int[] labels = new int[l.get(0)];
		String line;
		int breaker = 0;
		int q_id_rank = 0;
		try {
			while(true)
			{
				caller++;
				int count = 0;
				int start;
				for(int i=0; i<num; i++)
				{
					line = reader[i].readLine();
					if(line == null)
					{
						breaker = 1;
						break;
					}
					String[] splited = line.split("\\s+");
					
					start = 1;
					for(int j=start; j<splited.length; j++)
					{
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
		
					if(i==0)
					{
						labels[caller-1] = Integer.parseInt(splited[0]);
					}
					
				}
				if(caller == l.get(q_id_rank))
				{
					normalize(arr, n, l.get(q_id_rank));         //normalize by z-score
					q_id_rank++;
					for(int i=0; i<l.get(q_id_rank-1); i++)
					{
						writer.print(labels[i]+" ");
						for(int j=0; j<n; j++)
						{
							writer.print((j+1)+":"+arr[j][i]+" ");
						}
						writer.println();
					}
					
					if(q_id_rank == l.size())
					{
						break;
					}
					arr = new double[n][l.get(q_id_rank)];
					labels = new int[l.get(q_id_rank)];
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
	/**
	 * This method adds important data from train and test files to final file
	 * @param input1: input train file
	 * @param input2: input test file
	 */
	public static void len_cal(String input1, String input2)
	{
		try {
			BufferedReader reader = new BufferedReader(new FileReader(input1));
			BufferedReader reader2 = new BufferedReader(new FileReader(input2));
			String line;
			try {
				while((line = reader.readLine()) != null)
				{
					String[] qs = line.split("\\s+");
					int num = Integer.parseInt(qs[1]);
					length_auth.add(num);
					reader.readLine();
					for(int i=0; i<num; i++)
					{
						reader.readLine();
						reader.readLine();
					}
				}
				while((line = reader2.readLine()) != null)
				{
					String[] qs = line.split("\\s+");
					int num = Integer.parseInt(qs[1]);
					length_auth_test.add(num);
					reader2.readLine();
					for(int i=0; i<num; i++)
					{
						reader2.readLine();
						reader2.readLine();
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(length_auth.size()+" "+length_auth_test.size());
	}
	/**
	 * This method z-score normalizes all data
	 * @param arr: array of values
	 * @param n: number of features
	 * @param len: number of threads
	 */
	public static void normalize(double[][] arr, int n, int len)                   //normalization code
	{
		for(int i=0; i<n; i++)
		{
			double[] data = new double[len];
			for(int j=0; j<len; j++)
			{
				data[j] = arr[i][j];
			}
			Statistics a = new Statistics(data);
			if(a.getStdDev() != 0.0)
			{
				for(int j=0; j<len; j++)
				{
					
						arr[i][j] = (arr[i][j] - a.getMean())/a.getStdDev();
				}
			}
		}
	}
}
/**
 * This class is used for calculating some statistical measures for z-score
 * @author titas
 *
 */
class Statistics                                                 //zscore normalization of data
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
