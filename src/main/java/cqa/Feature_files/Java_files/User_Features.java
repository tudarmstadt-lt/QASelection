package cqa.Feature_files.Java_files;

import java.io.BufferedWriter;
import java.io.File;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.regex.Pattern;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;

public class User_Features                   //File for User related features
{
	public static String[] stopwords = {"a", "as", "able", "about", "above", "according", "accordingly", "across", "actually", "after", "afterwards", "again", "against", "aint", "all", "allow", "allows", "almost", "alone", "along", "already", "also", "although", "always", "am", "among", "amongst", "an", "and", "another", "any", "anybody", "anyhow", "anyone", "anything", "anyway", "anyways", "anywhere", "apart", "appear", "appreciate", "appropriate", "are", "arent", "around", "as", "aside", "ask", "asking", "associated", "at", "available", "away", "awfully", "be", "became", "because", "become", "becomes", "becoming", "been", "before", "beforehand", "behind", "being", "believe", "below", "beside", "besides", "best", "better", "between", "beyond", "both", "brief", "but", "by", "cmon", "cs", "came", "can", "cant", "cannot", "cant", "cause", "causes", "certain", "certainly", "changes", "clearly", "co", "com", "come", "comes", "concerning", "consequently", "consider", "considering", "contain", "containing", "contains", "corresponding", "could", "couldnt", "course", "currently", "definitely", "described", "despite", "did", "didnt", "different", "do", "does", "doesnt", "doing", "dont", "done", "down", "downwards", "during", "each", "edu", "eg", "eight", "either", "else", "elsewhere", "enough", "entirely", "especially", "et", "etc", "even", "ever", "every", "everybody", "everyone", "everything", "everywhere", "ex", "exactly", "example", "except", "far", "few", "ff", "fifth", "first", "five", "followed", "following", "follows", "for", "former", "formerly", "forth", "four", "from", "further", "furthermore", "get", "gets", "getting", "given", "gives", "go", "goes", "going", "gone", "got", "gotten", "greetings", "had", "hadnt", "happens", "hardly", "has", "hasnt", "have", "havent", "having", "he", "hes", "hello", "help", "hence", "her", "here", "heres", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "hi", "him", "himself", "his", "hither", "hopefully", "how", "howbeit", "however", "i", "id", "ill", "im", "ive", "ie", "if", "ignored", "immediate", "in", "inasmuch", "inc", "indeed", "indicate", "indicated", "indicates", "inner", "insofar", "instead", "into", "inward", "is", "isnt", "it", "itd", "itll", "its", "its", "itself", "just", "keep", "keeps", "kept", "know", "knows", "known", "last", "lately", "later", "latter", "latterly", "least", "less", "lest", "let", "lets", "like", "liked", "likely", "little", "look", "looking", "looks", "ltd", "mainly", "many", "may", "maybe", "me", "mean", "meanwhile", "merely", "might", "more", "moreover", "most", "mostly", "much", "must", "my", "myself", "name", "namely", "nd", "near", "nearly", "necessary", "need", "needs", "neither", "never", "nevertheless", "new", "next", "nine", "no", "nobody", "non", "none", "noone", "nor", "normally", "not", "nothing", "novel", "now", "nowhere", "obviously", "of", "off", "often", "oh", "ok", "okay", "old", "on", "once", "one", "ones", "only", "onto", "or", "other", "others", "otherwise", "ought", "our", "ours", "ourselves", "out", "outside", "over", "overall", "own", "particular", "particularly", "per", "perhaps", "placed", "please", "plus", "possible", "presumably", "probably", "provides", "que", "quite", "qv", "rather", "rd", "re", "really", "reasonably", "regarding", "regardless", "regards", "relatively", "respectively", "right", "said", "same", "saw", "say", "saying", "says", "second", "secondly", "see", "seeing", "seem", "seemed", "seeming", "seems", "seen", "self", "selves", "sensible", "sent", "serious", "seriously", "seven", "several", "shall", "she", "should", "shouldnt", "since", "six", "so", "some", "somebody", "somehow", "someone", "something", "sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry", "specified", "specify", "specifying", "still", "sub", "such", "sup", "sure", "ts", "take", "taken", "tell", "tends", "th", "than", "thank", "thanks", "thanx", "that", "thats", "thats", "the", "their", "theirs", "them", "themselves", "then", "thence", "there", "theres", "thereafter", "thereby", "therefore", "therein", "theres", "thereupon", "these", "they", "theyd", "theyll", "theyre", "theyve", "think", "third", "this", "thorough", "thoroughly", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "took", "toward", "towards", "tried", "tries", "truly", "try", "trying", "twice", "two", "un", "under", "unfortunately", "unless", "unlikely", "until", "unto", "up", "upon", "us", "use", "used", "useful", "uses", "using", "usually", "value", "various", "very", "via", "viz", "vs", "want", "wants", "was", "wasnt", "way", "we", "wed", "well", "were", "weve", "welcome", "well", "went", "were", "werent", "what", "whats", "whatever", "when", "whence", "whenever", "where", "wheres", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whos", "whoever", "whole", "whom", "whose", "why", "will", "willing", "wish", "with", "within", "without", "wont", "wonder", "would", "would", "wouldnt", "yes", "yet", "you", "youd", "youll", "youre", "youve", "your", "yours", "yourself", "yourselves", "zero"};
	public static void main(String args[])
    {
    	File inputFile = new File("/mnt/Titas/1_QA_MODEL/SemEval_Tasks/CQA/CQA_Updated/data/semeval2016_task3_tests/SemEval2016_task3_test/English/SemEval2016-Task3-CQA-QL-test-subtaskA.xml");
		SAXReader reader = new SAXReader();
		try {
			Document document = reader.read(inputFile);
			get_document_wc_ans();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }
    public static void get_wf_thread()
    {
    	File file = new File("/mnt/Titas/1_QA_MODEL/SemEval_Tasks/CQA/QASelection/src/main/java/cqa/Xml_reader/parsed_file.txt");
		BufferedReader reader = null;
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter("/mnt/Titas/1_QA_MODEL/SemEval_Tasks/CQA/QASelection/src/main/java/cqa/Feature_files/Data_format_files/User_features/train/With_stopwords/per_thread.txt", false)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			reader = new BufferedReader(new FileReader(file));
			try {				
				String q_id = reader.readLine();
				do
				{
					System.out.println(q_id);
					writer.println(q_id);
					HashMap<String, Integer> map = new HashMap<String, Integer>();
					String question = reader.readLine();
					String[] splited = question.toLowerCase().split("\\s+");
					//ArrayList<String> splited = removeStopWords(question.replaceAll("[^a-zA-Z0-9 ]", "").toLowerCase().split("\\s+"));
					enter_words_2(map, splited);
					for(int i=0; i<10; i++)
					{
						String str = reader.readLine();
						String[] spl = str.split("\\s+");
						String c_id = spl[0];
						String label = spl[1];
						String comment = reader.readLine();
						splited = comment.toLowerCase().split("\\s+");
						//splited = removeStopWords(comment.replaceAll("[^a-zA-Z0-9 ]", "").toLowerCase().split("\\s+"));
						enter_words_2(map, splited);
					}
					HashMap<String, Integer> sortedMap = sortByValues(map);
					System.out.println(sortedMap);
					for (HashMap.Entry<String, Integer> entry : sortedMap.entrySet()) {
					    writer.println(entry.getKey() + ": " + entry.getValue());
					}
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
    public static void get_document_wc_que()
    {
    	
    	File file = new File("/mnt/Titas/1_QA_MODEL/SemEval_Tasks/CQA/QASelection/src/main/java/cqa/Xml_reader/parsed_file.txt");
    	HashMap<String, Integer> map = new HashMap<String, Integer>();
    	BufferedReader reader = null;
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter("/mnt/Titas/1_QA_MODEL/SemEval_Tasks/CQA/QASelection/src/main/java/cqa/Feature_files/Data_format_files/User_features/train/With_stopwords/per_doc_que.txt", false)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			reader = new BufferedReader(new FileReader(file));
			try {				
				String q_id = reader.readLine();
				do
				{
					System.out.println(q_id);
					String question = reader.readLine();
					String[] splited = question.toLowerCase().split("\\s+");
					//ArrayList<String> splited = removeStopWords(question.replaceAll("[^a-zA-Z0-9 ]", "").toLowerCase().split("\\s+"));
					enter_words_2(map, splited);
					for(int i=0; i<10; i++)
					{
						String str = reader.readLine();
					}
				}
				while((q_id = reader.readLine())!=null);
				HashMap<String, Integer> sortedMap = sortByValues(map);
				System.out.println(sortedMap);
				for (HashMap.Entry<String, Integer> entry : sortedMap.entrySet()) {
				    writer.println(entry.getKey() + ": " + entry.getValue());
				}
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    }
    public static void get_document_wc_ans()
    {
    	
    	File file = new File("/mnt/Titas/1_QA_MODEL/SemEval_Tasks/CQA/QASelection/src/main/java/cqa/Xml_reader/parsed_file.txt");
    	HashMap<String, Integer> map = new HashMap<String, Integer>();
    	BufferedReader reader = null;
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter("/mnt/Titas/1_QA_MODEL/SemEval_Tasks/CQA/QASelection/src/main/java/cqa/Feature_files/Data_format_files/User_features/test/With_stopwords/per_doc_ans_bad.txt", false)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			reader = new BufferedReader(new FileReader(file));
			try {				
				String q_id = reader.readLine();
				do
				{
					System.out.println(q_id);
					String question = reader.readLine();
					for(int i=0; i<10; i++)
					{
						String str = reader.readLine();
						String[] spl = str.split("\\s+");
						String c_id = spl[0];
						String label = spl[1];
						String comment = reader.readLine();
						String[] splited = comment.toLowerCase().split("\\s+");
						//ArrayList<String> splited = removeStopWords(comment.replaceAll("[^a-zA-Z0-9 ]", "").toLowerCase().split("\\s+"));
						if(label.equals("Bad"))
							enter_words_2(map, splited);
					}
				}
				while((q_id = reader.readLine())!=null);
				HashMap<String, Integer> sortedMap = sortByValues(map);
				System.out.println(sortedMap);
				for (HashMap.Entry<String, Integer> entry : sortedMap.entrySet()) {
				    writer.println(entry.getKey() + ": " + entry.getValue());
				}
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    }
    public static void enter_words(Map map, ArrayList <String> words)
    {
    	for (String w : words) {
            Integer n = (Integer) map.get(w);
            n = (n == null) ? 1 : ++n;
            map.put(w, n);
        }
    }
    public static void enter_words_2(Map map, String[] words)
    {
    	for (String w : words) {
            Integer n = (Integer) map.get(w);
            n = (n == null) ? 1 : ++n;
            map.put(w, n);
        }
    }
    public static HashMap sortByValues(HashMap map) 
    { 
        List list = new LinkedList(map.entrySet());
        // Defined Custom Comparator here
        Collections.sort(list, new Comparator() {
             public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o1)).getValue())
                   .compareTo(((Map.Entry) (o2)).getValue());
             }
        });
        Collections.reverse(list);

        // Here I am copying the sorted list in HashMap
        // using LinkedHashMap to preserve the insertion order
        HashMap sortedHashMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
               Map.Entry entry = (Map.Entry) it.next();
               sortedHashMap.put(entry.getKey(), entry.getValue());
        } 
        return sortedHashMap;
   }
    public static ArrayList<String> removeStopWords(String[] words)
    {
        ArrayList<String> wordsList = new ArrayList<String>();
	    for (String word : words) {
	        wordsList.add(word);
	    	}
	    //System.out.println("After for loop:  " + wordsList);
	
	                //remove stop words here from the temp list
	    for (int i = 0; i < wordsList.size(); i++) {
	                    // get the item as string
	    	for (int j = 0; j < stopwords.length; j++) {
                if (wordsList.contains(stopwords[j])) {
                    wordsList.remove(stopwords[j]);//remove it
                }
            }
	    }
	    return wordsList;        
    }
 }

