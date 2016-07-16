package cqa.reader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.nio.file.Files;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
/**
 * This file parses XML input file and converts it into a format easy to read from
 * @author titas
 *
 */
public class XmlReader                    // File for reading XML files 
{
	static String input;
	static int flag;
	public XmlReader(String inp, int flag)
	{
		input = inp;
		this.flag = flag;
	}
	/**
	 * This method initializes computation
	 */
	public static void initialize()
	{
		File inputFile = new File(input);
		File parent = inputFile.getParentFile();
		String pathgp = parent.getAbsolutePath();
		System.out.println("XML parsing starts......");
		if(flag == 0)
		{
			File dir = new File(pathgp+"/parsed_files/");
			boolean success = dir.mkdirs();
			dir.setExecutable(true);
			dir.setReadable(true);
			dir.setWritable(true);
			parse(input+"/train.xml", pathgp+"/parsed_files/train.txt");
			get_users(input+"/train.xml", pathgp+"/parsed_files/utrain.txt");
			parse(input+"/test.xml", pathgp+"/parsed_files/test.txt");
			get_users(input+"/test.xml", pathgp+"/parsed_files/utest.txt");
			
		}
		else
		{
			parse_unannotated(input+"/unannotated.xml", pathgp+"/parsed_files/unannotated.txt");
		}
	}
	/**
	 * This method parses the input file using dom4j XML reader
	 * @param input: input XML file
	 * @param output: output parsed file
	 */
	public static void parse(String input, String output)
	{ 
		File inputFile = new File(input);
		SAXReader reader = new SAXReader();
		try {
			Document document = reader.read(inputFile);
			List<Node> nodes = document.selectNodes("xml/Thread");         //read XML file
			PrintWriter writer = null;
			try {
				writer = new PrintWriter(new BufferedWriter(new FileWriter(output, false)));
			} catch (IOException e) {
				e.printStackTrace();
			}
			for(int i=0; i< nodes.size(); i++)
			{
				String p = nodes.get(i).selectSingleNode("RelQuestion/RelQSubject").getText().trim().replaceAll("\\s+", " ");
				String l = nodes.get(i).selectSingleNode("RelQuestion/RelQBody").getText().trim().replaceAll("\\s+", " ");           //Extract Question
				String q_id = nodes.get(i).selectSingleNode("RelQuestion").valueOf("@RELQ_ID");
			    List<Node> comment = nodes.get(i).selectNodes("RelComment");
			    if(comment.size() != 0)
			    {
				    writer.print(q_id+" ");
				    writer.print(comment.size());
				    writer.println();
				    if(l.length() != 0)
				    	writer.println(l);
				    else
				    	writer.println(p);
			    }
			    for(int j=0; j<comment.size(); j++)
			    {
			    	l = comment.get(j).selectSingleNode("RelCText").getText().trim().replaceAll("\\s+", " ");                        // Extract Comment
			    	String c_id = comment.get(j).valueOf("@RELC_ID");
			    	String label = comment.get(j).valueOf("@RELC_RELEVANCE2RELQ");
			    	writer.println(c_id+" "+label);
		    		writer.println(l);
			    }
			   
			}
			writer.close();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	public static void parse_unannotated(String input, String output)
	{ 
		File inputFile = new File(input);
		SAXReader reader = new SAXReader();
		try {
			Document document = reader.read(inputFile);
			List<Node> nodes = document.selectNodes("xml/Thread");         //read XML file
			PrintWriter writer = null;
			try {
				writer = new PrintWriter(new BufferedWriter(new FileWriter(output, false)));
			} catch (IOException e) {
				e.printStackTrace();
			}
			for(int i=0; i< nodes.size(); i++)
			{
				String p = nodes.get(i).selectSingleNode("RelQuestion/RelQSubject").getText().trim().replaceAll("\\s+", " ");
				String l = nodes.get(i).selectSingleNode("RelQuestion/RelQBody").getText().trim().replaceAll("\\s+", " ");           //Extract Question
			    List<Node> comment = nodes.get(i).selectNodes("RelComment");
			    if(comment.size() != 0)
			    {
				    if(l.length() != 0)
				    	writer.println(l);
				    else
				    	writer.println(p);
			    }
			    for(int j=0; j<comment.size(); j++)
			    {
			    	l = comment.get(j).selectSingleNode("RelCText").getText().trim().replaceAll("\\s+", " ");                        // Extract Comment
		    		writer.println(l);
			    }
			   
			}
			writer.close();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
	/**
	 * This method does additional parsing having user information
	 * @param input: input file
	 * @param output: output file
	 */
	public static void get_users(String input, String output)                     //get user information from XML file          
    {
		File inputFile = new File(input);
		SAXReader reader = new SAXReader();
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(new BufferedWriter(new FileWriter(output, false)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			Document document = reader.read(inputFile);
			List<Node> nodes = document.selectNodes("xml/Thread");          //Get users involved in Question-comment thread
	    	for(int i=0; i<nodes.size(); i++)
	    	{
	    		String q_id = nodes.get(i).selectSingleNode("RelQuestion").valueOf("@RELQ_ID");
	    		String p = nodes.get(i).selectSingleNode("RelQuestion/RelQSubject").getText().trim().replaceAll("\\s+", " ");
	    		String l = nodes.get(i).selectSingleNode("RelQuestion/RelQBody").getText().trim().replaceAll("\\s+", " ");
	    		String user_name = nodes.get(i).selectSingleNode("RelQuestion").valueOf("@RELQ_USERNAME");
	    		List<Node> comment = nodes.get(i).selectNodes("RelComment");
	    		if(comment.size() != 0)
	    		{
	    			writer.println(q_id+" "+comment.size()+" "+nodes.get(i).selectSingleNode("RelQuestion").valueOf("@RELQ_USERID")+" "+user_name);
	    			if(l.length() != 0)
				    	writer.println(l);
				    else
				    	writer.println(p);
	    		}
	    		
			    for(int j=0; j<comment.size(); j++)
			    {
			    	l = comment.get(j).selectSingleNode("RelCText").getText().trim().replaceAll("\\s+", " ");
			    	String c_id = comment.get(j).valueOf("@RELC_ID");
			    	String label = comment.get(j).valueOf("@RELC_RELEVANCE2RELQ");
			    	String commenter_id = comment.get(j).valueOf("@RELC_USERID");
			    	String commenter_name = comment.get(j).valueOf("@RELC_USERNAME");
			    	writer.println(c_id+" "+label+" "+commenter_id+" "+commenter_name);
			    	writer.println(l);
			    }
	    	}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writer.close();
    }
}
