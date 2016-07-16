package cqa.core;

import org.deeplearning4j.models.embeddings.WeightLookupTable;
import org.deeplearning4j.models.embeddings.inmemory.InMemoryLookupTable;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.VocabWord;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.models.word2vec.wordstore.inmemory.InMemoryLookupCache;
import org.deeplearning4j.text.sentenceiterator.LineSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

import java.io.File;

/**
 * This class trains a word2vec model on provided data and writes word vectors and model to a separate files
 * @author titas
 *
 */
public class word2vecTraining 
{
	static String input;
	public word2vecTraining(String inp)
	{
		input = inp;	
	}
	/**
	 * This method initializes computation
	 */
	public static void initialize()
	{
		File inputFile = new File(input);
		File parent = inputFile.getParentFile();
		String pathgp = parent.getAbsolutePath();
		File dir = new File(pathgp+"/word2vec_files/");
		boolean success = dir.mkdirs();
		dir.setExecutable(true);
		dir.setReadable(true);
		dir.setWritable(true);
		System.out.println("Word2Vec Training starts......");
		try {
			word2vecTrainingRun(pathgp+"/parsed_files/unannotated_clean.txt", pathgp+"/word2vec_files/word_vectors.txt", pathgp+"/word2vec_files/model.txt");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
    public static void word2vecTrainingRun(String input, String output, String output2) throws Exception {
        // Strip white space before and after for each line
        SentenceIterator iter = new LineSentenceIterator(new File(input));
        // Split on white spaces in the line to get words
        TokenizerFactory t = new DefaultTokenizerFactory();
        t.setTokenPreProcessor(new CommonPreprocessor());

        // manual creation of VocabCache and WeightLookupTable usually isn't necessary
        // but in this case we'll need them
        InMemoryLookupCache cache = new InMemoryLookupCache();
        WeightLookupTable<VocabWord> table = new InMemoryLookupTable.Builder<VocabWord>()
                .vectorLength(100)
                .useAdaGrad(false)
                .cache(cache)
                .lr(0.025f).build();

      //  log.info("Building model....");
        Word2Vec vec = new Word2Vec.Builder()
                .minWordFrequency(5)
                .iterations(1)
                .epochs(1)
                .layerSize(100)
                .seed(42)
                .windowSize(5)
                .iterate(iter)
                .tokenizerFactory(t)
                .lookupTable(table)
                .vocabCache(cache)
                .build();
        vec.fit();
        WordVectorSerializer.writeWordVectors(vec, output);         //write word vectors
        WordVectorSerializer.writeFullModel(vec, output2);         //write model
    }
}
