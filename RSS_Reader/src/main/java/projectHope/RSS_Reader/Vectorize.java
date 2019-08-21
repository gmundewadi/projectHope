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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

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
	private static String Neural_Net_File_Path = "../Neural_Network/src/main/resources/datasets";
	private static int negativeDataSize = 200;
	private static int positiveDataSize = 200;
	private static Set<String> stopwords;
	private static Set<String> positive;
	private static Set<String> negative;

	public static final String DATA_PATH = FilenameUtils.concat(System.getProperty("java.io.tmpdir"),
			"TRAINED_DATA_PATH");

	public static void main(String args[]) {
		Vectorize v = new Vectorize();
		loadStopWords();
		loadPositiveWords();
		loadNegativeWords();
//		clearFiles();
		v.prepareTestData();
		v.prepareTrainData();

	}

	public void prepareTrainData() {
		System.out.println("+==========PREPARING TRAIN DATA==========+");
//		csvReader(Neural_Net_File_Path + "/train/data.csv");
//		wordToVec(Neural_Net_File_Path + "/train/words.txt");
		sentenceToVec(Neural_Net_File_Path + "/train/word_vectors.txt");
		csvWriter(Neural_Net_File_Path + "/train/results.csv");
		System.out.println("+==========TRAIN/results.csv prepared==========+");

	}

	public void prepareTestData() {
		System.out.println("+==========PREPARING TEST DATA==========+");
//		csvReader(Neural_Net_File_Path + "/test/data.csv");
//		wordToVec(Neural_Net_File_Path + "/test/words.txt");
		sentenceToVec(Neural_Net_File_Path + "/test/word_vectors.txt");
		csvWriter(Neural_Net_File_Path + "/test/results.csv");
		System.out.println("+==========TEST/results.csv prepared==========+");
	}

	public static void loadStopWords() {
		try {
			String filePath = Neural_Net_File_Path + "/key_words/stopwords.txt";
			System.out.println("loading english stopwords from " + filePath + " into memory ...");
			File file = new File(filePath);
			Scanner sc = new Scanner(file);
			stopwords = new HashSet<String>();
			while (sc.hasNextLine()) {
				String word = sc.nextLine();
				stopwords.add(word);
			}
			sc.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void loadPositiveWords() {
		try {
			String filePath = Neural_Net_File_Path + "/key_words/positive.txt";
			System.out.println("loading positive english from " + filePath + " into memory ...");
			File file = new File(filePath);
			Scanner sc = new Scanner(file);
			positive = new HashSet<String>();
			while (sc.hasNextLine()) {
				String word = sc.nextLine();
				positive.add(word);
			}
			sc.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void loadNegativeWords() {
		try {
			String filePath = Neural_Net_File_Path + "/key_words/negative.txt";
			System.out.println("loading negative english from " + filePath + " into memory ...");
			File file = new File(filePath);
			Scanner sc = new Scanner(file);
			negative = new HashSet<String>();
			while (sc.hasNextLine()) {
				String word = sc.nextLine();
				negative.add(word);
			}
			sc.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void csvWriter(String csv_file_path) {
		System.out.println("Writing Neural Network friendly data to " + csv_file_path + " ... ");
		String sentenceFileToRead = "";
		String csvFileToRead = "";
		if (csv_file_path.contains("train")) {
			sentenceFileToRead = Neural_Net_File_Path + "/train/sentence_vectors.txt";
			csvFileToRead = Neural_Net_File_Path + "/train/data.csv";
		} else {
			sentenceFileToRead = Neural_Net_File_Path + "/test/sentence_vectors.txt";
			csvFileToRead = Neural_Net_File_Path + "/test/data.csv";
		}
		try {
			CSVWriter writer = new CSVWriter(new FileWriter(csv_file_path));
			// Create record
			int tweetIndex = 0;
			List<Integer> sentiments = getSentiments(csvFileToRead);
			List<String[]> results = new ArrayList<String[]>();
			File file = new File(sentenceFileToRead);
			Scanner sc = new Scanner(file);
			int positives = 0;
			int negatives = 0;
			while (sc.hasNext() && tweetIndex < sentiments.size()) {
				String s = sc.nextLine();
				if (s.contains("NO SENTENCE VECTOR") || sentiments.get(tweetIndex) == 2) {
					tweetIndex++;
					continue;
				} else {
					s = s.replaceAll("\\[|\\]", "");
					int sentiment = sentiments.get(tweetIndex);
					// this if statement ensures that the training data remains
					// evenly split between negative and positive news
					if (negatives >= negativeDataSize && sentiment == 0
							|| positives >= positiveDataSize && sentiment == 4) {
						tweetIndex++;
						continue;
					} else if (sentiment == 4) {
						positives++;
						sentiment = 1;
					} else if (sentiment == 0) {
						negatives++;
					}
					// add comma so that split occurs correctly
					String recordString = s + "," + sentiment;
					tweetIndex++;
					String[] record = recordString.split(",");
					results.add(record);
				}
			}
			Collections.shuffle(results);
			for (String[] r : results) {
				writer.writeNext(r);
			}
			// close the writer
			writer.close();
			sc.close();
		} catch (IOException e) {
			e.printStackTrace();

		}
	}

	public void csvReader(String csv_file_path) {
		try {
			System.out.println("Reading data.csv file from " + csv_file_path + " ... ");
			String fileToWrite = "";
			if (csv_file_path.contains("train")) {
				fileToWrite = Neural_Net_File_Path + "/train/words.txt";
			} else {
				fileToWrite = Neural_Net_File_Path + "/test/words.txt";
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
			System.out.println("Vectorizing sentences using" + word_vector_file_path + " word2vec model ... ");
			String fileToWrite = "";
			String fileToRead = "";
			if (word_vector_file_path.contains("train")) {
				fileToWrite = Neural_Net_File_Path + "/train/sentence_vectors.txt";
				fileToRead = Neural_Net_File_Path + "/train/words.txt";
			} else {
				fileToWrite = Neural_Net_File_Path + "/test/sentence_vectors.txt";
				fileToRead = Neural_Net_File_Path + "/test/words.txt";
			}
			Word2Vec word2Vec = WordVectorSerializer.readWord2VecModel(word_vector_file_path);
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileToWrite));
			File file = new File(fileToRead);
			Scanner sc = new Scanner(file);
			ArrayList<String> words = new ArrayList<>();
			while (sc.hasNextLine()) {
				double factor = 1.0;
				String tweet = sc.nextLine().replaceAll("[^a-zA-Z0-9\\s]", "").toLowerCase();
				for (String word : tweet.split(" ")) {
					// if word vector is not null then word frequency is greater than 5
					// OR if word is a stopword.
					if (word2Vec.getWordVector(word) == null || stopwords.contains(word)) {
						continue;
					}	
					words.add(word);
					// if word is positive increase its weight
					if (positive.contains(word)) {
						factor += .05;
					}
					// if word is negative decrease its weight
					if (negative.contains(word)) {
						factor -= .05;
					}
				}
				// if words size is 0, tweet is made up of words that have frequency < 5 each
				if (words.size() > 0) {
					INDArray wordVectors = word2Vec.getWordVectorsMean(words);
					words.clear();
					// factor will represent a bag of words prediction of sentiment
					String result = wordVectors.toString() + "," + factor;
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

	public void wordToVec(String word_file_path) {
		try {
			System.out.println("Building word to vector model to " + word_file_path + " ... ");
			String fileToWrite = "";
			if (word_file_path.contains("train")) {
				fileToWrite = Neural_Net_File_Path + "/train/word_vectors.txt";
			} else {
				fileToWrite = Neural_Net_File_Path + "/test/word_vectors.txt";
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
			Word2Vec vec = new Word2Vec.Builder().minWordFrequency(5).iterations(1).layerSize(100).seed(42)
					.windowSize(5).iterate(iter).tokenizerFactory(t).build();

			log.info("Fitting Word2Vec model....");
			vec.fit();

			log.info("Writing word vectors to text file....");
			WordVectorSerializer.writeWordVectors(vec, fileToWrite);

			// How to retrive Word2Vec
			// Word2Vec word2Vec =
			// WordVectorSerializer.readWord2VecModel("WORDVECS_FILE_PATH");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<Integer> getSentiments(String csv_file_path) {
		try {
			System.out.println("Getting sentiment labels from " + csv_file_path + " ...");
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

	public static void clearFiles() {
		try {
			System.out.println("Clearing /datasets/train words.txt, word_vectors.txt, and sentence_vectors.txt ... ");
			PrintWriter train_words = new PrintWriter(Neural_Net_File_Path + "/train/words.txt");
			PrintWriter train_word_vectors = new PrintWriter(Neural_Net_File_Path + "/train/word_vectors.txt");
			PrintWriter train_sentence_vectors = new PrintWriter(Neural_Net_File_Path + "/train/sentence_vectors.txt");
			train_words.close();
			train_word_vectors.close();
			train_sentence_vectors.close();

			System.out.println("Clearing /datasets/test words.txt, word_vectors.txt, and sentence_vectors.txt ... ");
			PrintWriter test_words = new PrintWriter(Neural_Net_File_Path + "/test/words.txt");
			PrintWriter test_word_vectors = new PrintWriter(Neural_Net_File_Path + "/test/word_vectors.txt");
			PrintWriter test_sentence_vectors = new PrintWriter(Neural_Net_File_Path + "/test/sentence_vectors.txt");
			test_words.close();
			test_word_vectors.close();
			test_sentence_vectors.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
