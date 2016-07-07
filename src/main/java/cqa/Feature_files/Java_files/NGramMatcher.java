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
import java.util.List;

public class NGramMatcher 
{
	public static double f1 = 0.0, f2 = 0.0, f3 = 0.0;
	public static void main(String args[])
	{
		File file = new File(args[0]);
		File file2 = new File(args[1]);
		BufferedReader reader = null;
		BufferedReader reader2 = null;
		PrintWriter writer = null;
		PrintWriter writer2 = null;
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter(args[2], false)));
			writer2 = new PrintWriter(new BufferedWriter(new FileWriter(args[3], false)));
		} catch (IOException e) {
			e.printStackTrace();
		}
			try {
				reader = new BufferedReader(new FileReader(file));
				reader2 = new BufferedReader(new FileReader(file2));
				String line;
				try {
					while((line = reader.readLine()) != null)
					{
						String[] qs = line.split("\\s+");
						int num = Integer.parseInt(qs[1]);
						line = reader2.readLine();
						String question = reader.readLine();
						ArrayList<ArrayList<String>> lp = embedding_maker(reader2, question);      //create list of similar words
						for(int i=0; i<num; i++)
						{
							String str = reader.readLine();
							String[] splited = str.split("\\s+");
							String c_id = splited[0];
							String label = splited[1];
							str = reader2.readLine();
							String comment = reader.readLine();
							ArrayList<ArrayList<String>> lp2 = embedding_maker(reader2, comment);
							f1 = n_gram_generator(lp, lp2, 1);
							f2 = n_gram_generator(lp, lp2, 2);
							f3 = n_gram_generator(lp, lp2, 3);
							SVM_writer(writer2, label, 1);
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				writer.close();
				writer2.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
	}
	public static int matcher(List<String> que_list, List<String> com_list)   //n-gram count calculation
	{
		int n_gram_count = 0;
		for(int j=0; j<que_list.size(); j++)
		{
			if(com_list.contains(que_list.get(j)) && !que_list.get(j).isEmpty())
			{
				n_gram_count++;
			}
		}
		return n_gram_count;
	}
	public static void SVM_writer(PrintWriter writer, String label, int flag)
	{
		if(flag == 0)
			writer.println(get_Label_value(label)+" 1:"+f1+" 2:"+f2+" 3:"+f3);
		else
			writer.println(binary_class(label)+" 1:"+f1+" 2:"+f2+" 3:"+f3);
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
	public static int n_gram_generator(ArrayList<ArrayList<String>> s1, ArrayList<ArrayList<String>> s2, int n)
	{
		if(n==1 && s1.size()>0 && s2.size()>0)                           //generate common 1-gram lists
		{
			ArrayList<String> oneq = new ArrayList<>();
			for(int i=0; i<s1.size(); i++)
			{
				for(int j=0; j<s1.get(i).size(); j++)
				{
					oneq.add(s1.get(i).get(j));
				}
			}
			ArrayList<String> onea = new ArrayList<>();
			for(int i=0; i<s2.size(); i++)
			{
				for(int j=0; j<s2.get(i).size(); j++)
				{
					onea.add(s2.get(i).get(j));
				}
			}
			oneq.retainAll(onea);
			return oneq.size();
		}
		else if(n==2 && s1.size()>1 && s2.size()>1)                     //generate common bigram lists
		{
			ArrayList<String> oneq = new ArrayList<>();
			for(int i=0; i<s1.size()-1; i++)
			{
				for(int j=0; j<s1.get(i).size(); j++)
				{
					for(int k=0; k<s1.get(i+1).size(); k++)
					{
						oneq.add(s1.get(i).get(j)+" "+s1.get(i+1).get(k));
					}
				}
			}
			ArrayList<String> onea = new ArrayList<>();
			for(int i=0; i<s2.size()-1; i++)
			{
				for(int j=0; j<s2.get(i).size(); j++)
				{
					for(int k=0; k<s2.get(i+1).size(); k++)
					{
						onea.add(s2.get(i).get(j)+" "+s2.get(i+1).get(k));
					}
				}
			}
			oneq.retainAll(onea);
			return oneq.size();
		}
		else if(n==3 && s1.size()>2 && s2.size()>2)                       //generate common trigram lists
		{
			ArrayList<String> oneq = new ArrayList<>();
			for(int i=0; i<s1.size()-2; i++)
			{
				for(int j=0; j<s1.get(i).size(); j++)
				{
					for(int k=0; k<s1.get(i+1).size(); k++)
					{
						for(int l=0; l<s1.get(i+2).size(); l++)
						{
							oneq.add(s1.get(i).get(j)+" "+s1.get(i+1).get(k)+" "+s1.get(i+2).get(l));
						}
					}
				}
			}
			ArrayList<String> onea = new ArrayList<>();
			for(int i=0; i<s2.size()-2; i++)
			{
				for(int j=0; j<s2.get(i).size(); j++)
				{
					for(int k=0; k<s2.get(i+1).size(); k++)
					{
						for(int l=0; l<s2.get(i+2).size(); l++)
						{
							onea.add(s2.get(i).get(j)+" "+s2.get(i+1).get(k)+" "+s2.get(i+2).get(l));
						}
					}
				}
			}
			oneq.retainAll(onea);
			return oneq.size();
		}
		return 0;
	}
	public static ArrayList<ArrayList<String>> embedding_maker(BufferedReader reader2, String s)              //create dictionary of similar words
	{
		String line;
		int len = 0;
		String splited[] = s.split("\\s+");
		for(int i=0; i<splited.length; i++)
		{
			if(splited[i]!= null && splited[i].length() != 0)
			{
				len++;
			}
		}
		ArrayList<ArrayList<String>> dict = new ArrayList<>();
		for(int i=0; i<len; i++)
		{
			try {
					line = reader2.readLine();
					splited = line.split("\\s+");
					dict.add(new ArrayList<String>());
					for(int j=0; j<splited.length; j++)
					{
						if(splited[j].length() != 0)
						{
							if(j==0)
								dict.get(i).add(splited[j].substring(0, splited[j].length()-1));
							else
								dict.get(i).add(splited[j]);
						}
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
		}
		return dict;
	}

}
