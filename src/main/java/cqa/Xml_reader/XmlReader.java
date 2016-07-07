package cqa.Xml_reader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
public class XmlReader                    // File for reading XML files 
{
	public static void main(String args[])    
	{
		File inputFile = new File(args[0]);
		SAXReader reader = new SAXReader();
		get_users(args[0], args[1]);
		try {
			Document document = reader.read(inputFile);
			List<Node> nodes = document.selectNodes("xml/Thread");         //read XML file
			PrintWriter writer = null;
			try {
				writer = new PrintWriter(new BufferedWriter(new FileWriter(args[1], false)));
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
