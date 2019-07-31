package projectHope.RSS_Reader;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.datavec.api.util.ClassPathResource;

import org.datavec.api.util.ClassPathResource;
import org.deeplearning4j.iterator.CnnSentenceDataSetIterator;
import org.deeplearning4j.iterator.LabeledSentenceProvider;
import org.deeplearning4j.iterator.provider.FileLabeledSentenceProvider;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.nn.api.Layer;
import org.deeplearning4j.nn.conf.ComputationGraphConfiguration;
import org.deeplearning4j.nn.conf.ConvolutionMode;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.graph.MergeVertex;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.ConvolutionLayer;
import org.deeplearning4j.nn.conf.layers.GlobalPoolingLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.conf.layers.PoolingType;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.api.InvocationType;
import org.deeplearning4j.optimize.listeners.EvaluativeListener;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.sentenceiterator.LineSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WordToVec {

	private static MongoClient mongoClient;
	private static Logger log = LoggerFactory.getLogger(WordToVec.class);
    public static final String DATA_PATH = FilenameUtils.concat(
    		System.getProperty("java.io.tmpdir"), "TRAINED_DATA_PATH");


	public static void main(String args[]) {
		MongoClientURI uriVal = new MongoClientURI(
				"mongodb+srv://gautam:projectHope@cluster0-biq2l.azure.mongodb.net/test?retryWrites=true&w=majority");
		mongoClient = new MongoClient(uriVal);
		WordToVec v = new WordToVec();
		v.updateWordFile();
		v.wordToVec();

	}

	public void updateWordFile() {
		MongoDatabase mongoDB = mongoClient.getDatabase("RSS_Reader");
		MongoCollection<Document> collection = mongoDB.getCollection("Article");
		FindIterable<Document> findIterable = collection.find(new Document());
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("./words.txt"));
			for (Document d : findIterable) {
				String title = d.getString("title");
				writer.write(title + "\n");

			}
			writer.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void wordToVec() {
		try {
			SentenceIterator iter = new LineSentenceIterator(new File("./words.txt"));

			log.info("Load & Vectorize Sentences....");
			// Strip white space before and after for each line &
			// Split on white spaces in the line to get words
			TokenizerFactory t = new DefaultTokenizerFactory();

			/*
			 * CommonPreprocessor will apply the following regex to each token:
			 * [\d\.:,"'\(\)\[\]|/?!;]+ So, effectively all numbers, punctuation symbols and
			 * some special symbols are stripped off. Additionally it forces lower case for
			 * all tokens.
			 */
			
			t.setTokenPreProcessor(new CommonPreprocessor());

			log.info("Building model....");
			Word2Vec vec = new Word2Vec.Builder()
					.minWordFrequency(2)
					.iterations(1)
					.layerSize(100)
					.seed(42)
					.windowSize(5)
					.iterate(iter)
					.tokenizerFactory(t)
					.build();

			log.info("Fitting Word2Vec model....");
			vec.fit();

			log.info("Writing word vectors to text file....");
	        WordVectorSerializer.writeWordVectors(vec, "./vectors.txt");
			
	        // Retrive Word2Vec
	        // Word2Vec word2Vec = WordVectorSerializer.readWord2VecModel("./vectors.txt");
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
