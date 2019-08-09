package projectHope.RSS_Reader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

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

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

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

public class Vectorize {

	private static Logger log = LoggerFactory.getLogger(Vectorize.class);

	public static final String DATA_PATH = FilenameUtils.concat(System.getProperty("java.io.tmpdir"),
			"TRAINED_DATA_PATH");

	public static void main(String args[]) {
		Vectorize v = new Vectorize();
		// clearFiles(); NEED TO WRITE
		v.prepareTrainData();
		v.prepareTestData();
	}

	public void prepareTrainData() {
		System.out.println("+==========PREPARING TRAIN DATA==========+");
		// csvReader("./datasets/train/data.csv");
		// wordToVec("./datasets/train/words.txt");
		// sentenceToVec("./datasets/train/word_vectors.txt");
		csvWriter("./datasets/train/results.csv");
		System.out.println("+==========DONE==========+");

	}

	public void prepareTestData() {
		System.out.println("+==========PREPARING TEST DATA==========+");
		// csvReader("./datasets/test/data.csv");
		// wordToVec("./datasets/test/words.txt");
		// sentenceToVec("./datasets/test/word_vectors.txt");
		csvWriter("./datasets/test/results.csv");
		System.out.println("+==========DONE==========+");
	}

	public void csvWriter(String csv_file_path) {
		String sentenceFileToRead = "";
		String csvFileToRead = "";
		if (csv_file_path.contains("train")) {
			sentenceFileToRead = "./datasets/train/sentence_vectors.txt";
			csvFileToRead = "./datasets/train/data.csv";
		} else {
			sentenceFileToRead = "./datasets/test/sentence_vectors.txt";
			csvFileToRead = "./datasets/test/data.csv";
		}
		try {
			CSVWriter writer = new CSVWriter(new FileWriter(csv_file_path));
			// Create record
			int sentimentIndex = 0;
			List<Integer> sentiments = getSentiments(csvFileToRead);
			File file = new File(sentenceFileToRead);
			Scanner sc = new Scanner(file);
			while (sc.hasNext()) {
				String s = sc.nextLine();
				if (s.contains("NO SENTENCE VECTOR")) {
					sentimentIndex++;
					continue;
				} else {
					s = s.replaceAll("\\[|\\]", "");
					String recordString = sentiments.get(sentimentIndex) + s;
					System.out.println(recordString);
					sentimentIndex++;
					String[] record = recordString.split(",");
					writer.writeNext(record);
				}
			}
			// close the writer
			writer.close();
			sc.close();
		} catch (IOException e) {
			e.printStackTrace();

		}
	}

	public void prepareData(String file_path) {
		try {
			Reader reader = new BufferedReader(
					new InputStreamReader(new FileInputStream("./datasets" + file_path), "utf-8"));
			CSVReader csvReader = new CSVReader(reader);
			String[] nextRecord;
			while ((nextRecord = csvReader.readNext()) != null) {
				String tweet = nextRecord[1].replaceAll("[^a-zA-Z0-9\\s]", "");
				float[] tweetVector = sentenceToVector(tweet);
				System.out.println(tweetVector.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void csvReader(String csv_file_path) {
		try {
			String fileToWrite = "";
			if (csv_file_path.contains("train")) {
				fileToWrite = "./datasets/train/words.txt";
			} else {
				fileToWrite = "./datasets/test/words.txt";
			}
			Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(csv_file_path), "utf-8"));
			CSVReader csvReader = new CSVReader(reader);
			String[] nextRecord;
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileToWrite));
			while ((nextRecord = csvReader.readNext()) != null) {
				String tweet = nextRecord[1].replaceAll("[^a-zA-Z0-9\\s]", "");
				writer.write(tweet + "\n");
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sentenceToVec(String word_vector_file_path) {
		try {
			String fileToWrite = "";
			String fileToRead = "";
			if (word_vector_file_path.contains("train")) {
				fileToWrite = "./datasets/train/sentence_vectors.txt";
				fileToRead = "./datasets/train/words.txt";
			} else {
				fileToWrite = "./datasets/test/sentence_vectors.txt";
				fileToRead = "./datasets/test/words.txt";
			}
			Word2Vec word2Vec = WordVectorSerializer.readWord2VecModel(word_vector_file_path);
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileToWrite));
			File file = new File(fileToRead);
			Scanner sc = new Scanner(file);
			ArrayList<String> words = new ArrayList<>();
			while (sc.hasNextLine()) {
				String tweet = sc.nextLine().replaceAll("[^a-zA-Z0-9\\s]", "");
				for (String word : tweet.split(" ")) {
					// if length is not zero then word frequency is greater than 2
					if (word2Vec.getWordVector(word) != null) {
						words.add(word);
					}
				}
				// if words.size equals 0. Then the tweet is made up of words that are all
				// different
				if (words.size() > 0) {
					INDArray wordVectors = word2Vec.getWordVectorsMean(words);
					words.clear();
					String result = wordVectors.toString();
					writer.write(result + "\n");
				} else {
					writer.write("NO SENTENCE VECTOR" + "\n");
				}
			}
			sc.close();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private float[] stringToFloatArray(String value) {
		value = value.replaceAll("[-+.^:,]", "");
		String[] stringFloats = value.split(" ");
		float[] floatArray = new float[stringFloats.length];
		for (int i = 0; i < stringFloats.length; i++) {
			float f = Float.parseFloat(stringFloats[i]);
			floatArray[i] = f;
		}
		return floatArray;
	}

	public void wordToVec(String word_file_path) {
		try {
			String fileToWrite = "";
			if (word_file_path.contains("train")) {
				fileToWrite = "./datasets/train/word_vectors.txt";
			} else {
				fileToWrite = "./datasets/test/word_vectors.txt";
			}
			SentenceIterator iter = new LineSentenceIterator(new File(word_file_path));

			log.info("Load & Vectorize titles into words....");
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
			Word2Vec vec = new Word2Vec.Builder().minWordFrequency(2).iterations(1).layerSize(100).seed(42)
					.windowSize(5).iterate(iter).tokenizerFactory(t).build();

			log.info("Fitting Word2Vec model....");
			vec.fit();

			log.info("Writing word vectors to text file....");
			WordVectorSerializer.writeWordVectors(vec, fileToWrite);

			// How to retrive Word2Vec
			// Word2Vec word2Vec =
			// WordVectorSerializer.readWord2VecModel("WORDVECS_FILE_PATH");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void updateArticleTitles() {

		MongoClientURI uriVal = new MongoClientURI(
				"mongodb+srv://gautam:projectHope@cluster0-biq2l.azure.mongodb.net/test?retryWrites=true&w=majority");
		MongoClient mongoClient = new MongoClient(uriVal);
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

	public static void clearFiles() {
		try {
			System.out.println("----CLEARING FILES----");
			PrintWriter wordVecs = new PrintWriter("./wordVectors.txt");
			PrintWriter sentenceVecs = new PrintWriter("./sentenceVectors.txt");
			PrintWriter words = new PrintWriter("./words.txt");
			wordVecs.close();
			words.close();
			sentenceVecs.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public List<Integer> getSentiments(String csv_file_path) {
		try {
			Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(csv_file_path), "utf-8"));
			CSVReader csvReader = new CSVReader(reader);
			String[] nextRecord;
			List<Integer> sentiments = new ArrayList<>();
			while ((nextRecord = csvReader.readNext()) != null) {
				sentiments.add(Integer.parseInt(nextRecord[0]));
			}
			return sentiments;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null; // return statement for compilation only
	}

	public List<String> getTweets(String csv_file_path) {
		try {
			Reader reader = new BufferedReader(new InputStreamReader(new FileInputStream(csv_file_path), "utf-8"));
			CSVReader csvReader = new CSVReader(reader);
			String[] nextRecord;
			List<String> tweets = new ArrayList<>();
			while ((nextRecord = csvReader.readNext()) != null) {
				tweets.add(nextRecord[1]);
			}
			return tweets;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null; // return statement for compilation only
	}

	// DELETE THIS METHOD
	public float[] sentenceToVector(String sentence) {
		Word2Vec word2Vec = WordVectorSerializer.readWord2VecModel("./wordVectors.txt");
		ArrayList<String> words = new ArrayList<>();
		String tweet = sentence.replaceAll("[^a-zA-Z0-9\\s]", "");
		for (String word : tweet.split(" ")) {
			// if length is not zero then word frequency is greater than 2
			if (word2Vec.getWordVector(word) != null) {
				words.add(word);
			}
		}
		// if words.size equals 0. Then the tweet is made up of words that are all
		// different
		if (words.size() > 0) {
			INDArray wordVectors = word2Vec.getWordVectorsMean(words);
			words.clear();
			float[] result = stringToFloatArray(wordVectors.toString());
			return result;
		} else {
			return new float[] { 0 };
		}

	}

}
