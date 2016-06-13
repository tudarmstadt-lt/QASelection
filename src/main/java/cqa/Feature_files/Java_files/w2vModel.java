package cqa.Feature_files.Java_files;

import java.io.File;
import java.io.IOException;
import org.deeplearning4j.berkeley.Pair;
import org.deeplearning4j.models.embeddings.inmemory.InMemoryLookupTable;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.wordstore.VocabCache;
import org.deeplearning4j.plot.BarnesHutTsne;
import org.nd4j.linalg.api.buffer.DataBuffer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class w2vModel 
{
	public static Logger log = LoggerFactory.getLogger(w2vModel.class);
	public static void main(String args[])
	{
//		PrintWriter writer = null;
//		try {
//			writer = new PrintWriter(new BufferedWriter(new FileWriter(args[1], false)));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		File gModel = new File(args[0]);
//	    try {
//			WordVectors vec = WordVectorSerializer.loadGoogleModel(gModel, true);
//			Collection<String> similar = vec.wordsNearest("day", 10);
//		    writer.println(similar);
//		    double sim = vec.similarity("people", "money");
//		    writer.println(sim);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	   tsne(args[0], args[1]); 
	}
	public static void tsne(String input, String output)
	{
        Nd4j.dtype = DataBuffer.Type.DOUBLE;
        Nd4j.factory().setDType(DataBuffer.Type.DOUBLE);
        List<String> cacheList = new ArrayList<>();

        log.info("Load & Vectorize data....");
        File wordFile;
		try {
			wordFile = new File(input);
			Pair<InMemoryLookupTable,VocabCache> vectors = WordVectorSerializer.loadTxt(wordFile);
	        VocabCache cache = vectors.getSecond();
	        INDArray weights = vectors.getFirst().getSyn0();

	        for(int i = 0; i < cache.numWords(); i++)
	            cacheList.add(cache.wordAtIndex(i));

	        log.info("Build model....");
	        BarnesHutTsne tsne = new BarnesHutTsne.Builder()
			        .setMaxIter(100)
	                .stopLyingIteration(250)
	                .learningRate(500)
	                .useAdaGrad(false)
	                .theta(0.5)
	                .setMomentum(0.5)
	                .normalize(true)
	                .usePca(false)
	                .build();

	        log.info("Store TSNE Coordinates for Plotting....");
	        String outputFile = output;
	        //(new File(outputFile)).getParentFile().mkdirs();
	        tsne.plot(weights,2,cacheList,outputFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
