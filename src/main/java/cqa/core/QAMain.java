package cqa.core;
import java.io.File;
import java.io.IOException;

import cqa.reader.MultiFileReader;
import cqa.reader.XmlReader;
import cqa.writer.EmbeddingWriter;
import cqa.writer.Writer;
/**
 * This is the main java file initiating all computations and the CQA system
 * @author titas
 *
 */
public class QAMain 
{
    public static void main(String args[]) //args[0] is xml_files directory
    {
    	String resource_path = new File(".").getAbsolutePath();
    	int last = resource_path.length()-1;
    	resource_path = resource_path.substring(0, last)+"/src/main/resources/scripts/";				    //path to resources directory
    	parsed_files(args[0], 0);                                                           				//parsed files generation
    	parsed_files(args[0], 1);																			//comment this to not generate unannotated parsed files
    	get_clean_files(get_parent(args[0])+"/parsed_files/", resource_path);								//get clean data
    	string_similarity(get_parent(args[0])+"/parsed_files/");											//string similarity features computation
    	dialogue_features(get_parent(args[0])+"/parsed_files/",get_parent(args[0])+"/svm_files/");			//dialogue features computation
    	meta_features(get_parent(args[0])+"/parsed_files/",get_parent(args[0])+"/svm_files/");				//metadata features computation
    	thread_level_features(get_parent(args[0])+"/parsed_files/",get_parent(args[0])+"/svm_files/");		//thread level features computation
    	word2vec_training(args[0]);																			//word2vec training
    	embedding_trainer(args[0]);																			//sentence vectors computation
    	embedding_writer(get_parent(args[0])+"/word2vec_files/", get_parent(args[0])+"/svm_files/");		//writing embeddings
    	get_clean_files_thread(get_parent(args[0])+"/parsed_files/", resource_path);						//get clean data
    	user_features(get_parent(args[0])+"/parsed_files/");												//user features computation
    	thread_testing(get_parent(args[0])+"/parsed_files/",get_parent(args[0])+"/svm_files/");				//thread features computation
    	multi_file_reader(get_parent(args[0])+"/svm_files/", get_parent(args[0])+"/parsed_files/");			//combining several feature files
    	run_svm(get_parent(args[0])+"/svm_files/", get_parent(resource_path), 0);							//run svm 
    	compute_scorer(get_parent(args[0]), resource_path);													//compute scorer scripts for test data
    	writer(get_parent(args[0]));																		//run scorer scripts
    	get_scores(get_parent(args[0])+"/result_files/", resource_path);									//get final scores
		
    }
    /**
     * This method generates parsed files from XML files
     * @param inp: the directory of XML files
     * @param flag: 0 for train and test files, 1 for unannotated file
     */
    public static void parsed_files(String inp, int flag)
    {
    	System.out.println("Loading XML data......");
    	XmlReader xml = new XmlReader(inp, flag);
    	xml.initialize();	
    }
    /**
     * This method cleans parsed data using python scripts in resources directory
     * @param inp: The input directory
     * @param resource_path: The path to resources folder
     */
    public static void get_clean_files(String inp, String resource_path)
    {
    	System.out.println("Producing clean data......");
    	try {
    		Process p = (new ProcessBuilder("python",resource_path+"check.py",inp+"train.txt",inp+"train_clean.txt")).start();
    		p.waitFor();
    		Process p1 = (new ProcessBuilder("python",resource_path+"check.py",inp+"test.txt",inp+"test_clean.txt")).start();
    		p1.waitFor();
    		Process p2 = (new ProcessBuilder("python",resource_path+"check.py",inp+"utrain.txt",inp+"utrain_clean.txt")).start();
    		p2.waitFor();
    		Process p3 = (new ProcessBuilder("python",resource_path+"check.py",inp+"utest.txt",inp+"utest_clean.txt")).start();
    		p3.waitFor();
    		Process p4 = (new ProcessBuilder("python",resource_path+"check_unannotated.py",inp+"unannotated.txt",inp+"unannotated_clean.txt")).start();
    		p4.waitFor();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * This method runs LIBLinear 
     * @param inp: The input SVM files directory
     * @param resource_path: The path to resources folder
     */
    public static void run_svm(String inp, String resource_path, int param)
    {
    	System.out.println("SVM computation starts......");
    	System.out.println("SVM parameters: -s "+param+" (L2-regularized logistic regression)");
    	File inputFile = new File(inp);
		File parent = inputFile.getParentFile();
		String pathgp = parent.getAbsolutePath();
		File dir = new File(pathgp+"/result_files/");
		boolean success = dir.mkdirs();
		dir.setExecutable(true);
		dir.setReadable(true);
		dir.setWritable(true);
    	try {
    		ProcessBuilder builder = new ProcessBuilder("java","-cp",resource_path+"/lib/liblinear-java-1.95.jar","de.bwaldvogel.liblinear.Train","-s",param+"",inp+"/train/SVM_train.txt");
    		builder.directory(new File(inp+"train/"));
    		Process p = builder.start();
    		p.waitFor();
    		ProcessBuilder builder2 = new ProcessBuilder("java","-cp",resource_path+"/lib/liblinear-java-1.95.jar","de.bwaldvogel.liblinear.Predict","-b","1",inp+"/test/SVM_test.txt", inp+"/train/SVM_train.txt.model", pathgp+"/result_files/out.txt");
    		Process p2 = builder2.start();
    		p2.waitFor();
    	} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * This method computes scorer script for MAP computation
     * @param inp: The input directory
     * @param resource_path: The path to resources folder
     */
    public static void compute_scorer(String inp, String resource_path)
    {
    	System.out.println("Computing scorer scripts......");
		try {
			Process p = (new ProcessBuilder("python",resource_path+"scorer_format.py",inp+"/parsed_files/test_clean.txt",inp+"/result_files/scores_gold.txt")).start();
			p.waitFor();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * This method cleans parsed data using python scripts in resources directory
     * @param inp: The input directory
     * @param resource_path: The path to resources folder
     */
    public static void get_clean_files_thread(String inp, String resource_path)
    {
    	try {
    		Process p = (new ProcessBuilder("python",resource_path+"check_thread.py",inp+"train.txt",inp+"train_clean_thread.txt")).start();
    		p.waitFor();
    		Process p1 = (new ProcessBuilder("python",resource_path+"check_thread.py",inp+"test.txt",inp+"test_clean_thread.txt")).start();
    		p1.waitFor();
    		ProcessBuilder builder = new ProcessBuilder("cat",inp+"train_clean_thread.txt",inp+"test_clean_thread.txt");
            File combinedFile = new File(inp+"merge_clean_thread.txt");
            builder.redirectOutput(combinedFile);
    		Process p2 = builder.start();
    		p2.waitFor();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * This method computes final scores and writes it to the file results.txt
     * @param inp: The input directory
     * @param resource_path: The path to resources folder
     */
    public static void get_scores(String inp, String resource_path)
    {
    	System.out.println("Scores computation about to end......");
    	ProcessBuilder builder = new ProcessBuilder("python", resource_path+"ev.py", inp+"scores_gold.txt", inp+"results.txt");
    	File outputFile = new File(inp+"final_scores.txt");
    	builder.redirectOutput(outputFile);
 		Process p;
		try {
			p = builder.start();
			p.waitFor();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    /**
     * This method returns parent folder of given file or folder
     * @param inp: The input file or folder
     * @return the parent file or folder
     */
    public static String get_parent(String inp)
    {
    	File f = new File(inp);
    	String parent = f.getParent();
    	return parent;
    }
    /**
     * This method computes string similarity features
     * @param inp: The input directory
     */
    public static void string_similarity(String inp)
    {
    	SimilarityFeatureGenerator g = new SimilarityFeatureGenerator(inp);
    	g.initialize();
    }
    /**
     * This method computes dialogue features
     * @param inp: The input directory
     * @param out: The output directory
     */
    public static void dialogue_features(String inp, String out)
    {
    	DialogueFeatures f = new DialogueFeatures(inp, out);
    	f.initialize();
    }
    /**
     * This method computes metadata features
     * @param inp: The input directory
     * @param out: The output directory
     */
    public static void meta_features(String inp, String out)
    {
    	MetaFeatures m = new MetaFeatures(inp, out);
    	m.initialize();
    }
    /**
     * This method computes thread level features
     * @param inp: The input directory
     * @param out: The output directory
     */
    public static void thread_level_features(String inp, String out)
    {
    	ThreadLevelProximity tl = new ThreadLevelProximity(inp, out);
    	tl.initialize();
    }
    /**
     * This method does word2vec training 
     * @param inp: The input directory
     */
    public static void word2vec_training(String inp)
    {
    	word2vecTraining wv = new word2vecTraining(inp);
    	wv.initialize();
    }
    /**
     * This method computes sentence vectors
     * @param inp: The input directory
     */
    public static void embedding_trainer(String inp)
    {
    	EmbeddingTrainer e = new EmbeddingTrainer(inp);
    	e.initialize();
    }
    /**
     * This method writes embeddings to a file
     * @param inp: The input directory
     * @param out: The output directory
     */
    public static void embedding_writer(String inp, String out)
    {
    	EmbeddingWriter e = new EmbeddingWriter(inp, out);
    	e.initialize();
    }
    /**
     * This method computes user features
     * @param inp: The input directory
     */
    public static void user_features(String inp)
    {
    	UserFeatures u = new UserFeatures(inp);
    	u.initialize();
    }
    /**
     * This method computes thread features
     * @param inp: The input directory
     * @param out: The output directory
     */
    public static void thread_testing(String inp, String out)
    {
    	ThreadTesting tt = new ThreadTesting(inp, out);
    	tt.initialize();
    }
    /**
     * This method combines several SVM feature files
     * @param inp1: The input directory
     * @param inp2: Another input directory
     */
    public static void multi_file_reader(String inp, String inp2)
    {
    	MultiFileReader mfr = new MultiFileReader(inp, inp2);
    	mfr.initialize();
    }
    /**
     * This method computes final scores
     * @param inp: The input directory
     */
    public static void writer(String inp)
    {
    	Writer w = new Writer(inp);
    	w.initialize();
    }
}
