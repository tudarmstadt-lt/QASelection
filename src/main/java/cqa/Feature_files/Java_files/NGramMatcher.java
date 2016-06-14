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
	public static double f1 = 0.0, f2=0.0, f3=0.0;
	public static void main(String args[])
	{
		File file = new File(args[0]);
		File file2 = new File(args[1]);
		BufferedReader reader = null;
		BufferedReader reader2 = null;
		PrintWriter writer = null;
		PrintWriter writer2 = null;
		int q_id_rank = 0;
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
						line = reader2.readLine();
						q_id_rank++;
						String question = reader.readLine();
						ArrayList<String> lp = embedding_maker(reader2, question);      //create list of similar words
						ArrayList<List<String>> one_list = new ArrayList<>();           //unigram lists
						ArrayList<List<String>> two_list = new ArrayList<>();			//bigram lists
						ArrayList<List<String>> three_list = new ArrayList<>();			//trigram lists
						for(int i=0; i<lp.size(); i++)
						{
							one_list.add(maker(lp.get(i),1));
							two_list.add(maker(lp.get(i),2));
							three_list.add(maker(lp.get(i),3));
						}
//						List<String> one_list = maker(question, 1);
//						List<String> two_list = maker(question, 2);
//						List<String> three_list = maker(question, 3);
						for(int i=0; i<10; i++)
						{
							String str = reader.readLine();
							String[] splited = str.split("\\s+");
							String c_id = splited[0];
							String label = splited[1];
							str = reader2.readLine();
							String comment = reader.readLine();
							ArrayList<String> lp2 = embedding_maker(reader2, comment);
							ArrayList<List<String>> aone_list = new ArrayList<>();
							ArrayList<List<String>> atwo_list = new ArrayList<>();
							ArrayList<List<String>> athree_list = new ArrayList<>();
							for(int j=0; j<lp2.size(); j++)
							{
								aone_list.add(maker(lp2.get(j),1));
								atwo_list.add(maker(lp2.get(j),2));
								athree_list.add(maker(lp2.get(j),3));
							}

//							List<String> aone_list = maker(comment, 1);
//							List<String> atwo_list = maker(comment, 2);
//							List<String> athree_list = maker(comment, 3);
							long count = 0;
							for(int j=0; j<lp.size(); j++)
							{
								for(int k=0; k<lp2.size(); k++)
								{
									count++;
									f1 += (matcher(one_list.get(j), aone_list.get(k)) - f1)/count;
									f2 += (matcher(two_list.get(j), atwo_list.get(k)) - f2)/count;
									f3 += (matcher(three_list.get(j), athree_list.get(k)) - f3)/count;
								}
							}
							RankLib_writer(writer, label, q_id_rank, c_id);
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
	public static List<String> maker(String s, int n)       //lists for n-grams
	{
		Gram gram = new Gram(s, n);
		return gram.list();
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
	public static void RankLib_writer(PrintWriter writer, String label, int q_id_rank, String c_id)
	{
		writer.println(get_Label_value(label)+" "+"qid:"+q_id_rank+" 1:"+f1+" 2:"+f2+" 3:"+f3+" # "+c_id);
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
	public static ArrayList<String> embedding_maker(BufferedReader reader2, String s)
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
		String curStr="";
		ArrayList<String> list = new ArrayList<>();
		printUtil(dict, curStr, list, 0, dict.size());        //Recursively calculate all possible sentences
		return list;
	}
	static void printUtil(ArrayList<ArrayList<String>> strs, String curStr, ArrayList<String> list, int index, int n)
	{
		if (index == n)
		{
			list.add(curStr);
		}
		else
		{
			for (int i = 0; i < strs.get(index).size(); i++)
			{
			String tmp = curStr;
			curStr = curStr + " " + strs.get(index).get(i);
			printUtil(strs, curStr, list, index + 1, n);
			curStr = tmp;
			}
		}
	}

}