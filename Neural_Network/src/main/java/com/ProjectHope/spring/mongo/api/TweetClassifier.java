package com.ProjectHope.spring.mongo.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.io.IOUtils;
import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.datavec.api.util.ClassPathResource;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.preprocessor.DataNormalization;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerStandardize;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Sgd;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVReader;

public class TweetClassifier {

	private static Logger log = LoggerFactory.getLogger(TweetClassifier.class);

	private static Map<Integer, String> classifiers;

	public TweetClassifier() {
		classifiers = new HashMap<>();
		classifiers.put(0, "negative");
		classifiers.put(1, "positive");

	}

	public void classify(String twitterDataTrainFile, String twitterDataTestFile)
			throws FileNotFoundException, IOException, InterruptedException {

		// Second: the RecordReaderDataSetIterator handles conversion to DataSet
		// objects, ready for use in neural network
		int labelIndex = 100; // 5 values in each row of the animals.csv CSV: 4 input features followed by an
								// integer label (class) index. Labels are the 5th value (index 4) in each row
		int numClasses = 2; // 2 classes (types of tweet) in the results.csv data set. Classes have integer
							// values 0 or 1

		int batchSizeTraining = 1000; // Tweets training data set: 30 examples total. We are loading all of them into
										// one DataSet (not recommended for large data sets)
		DataSet trainingData = readCSVDataset(twitterDataTrainFile, batchSizeTraining, labelIndex, numClasses);

		// this is the data we want to classify
		int batchSizeTest = 23;
		DataSet testData = readCSVDataset(twitterDataTestFile, batchSizeTest, labelIndex, numClasses);

		// make the data model for records prior to normalization, because it
		// changes the data.
		Map<Integer, Tweet> tweets = objectify(testData);

		// We need to normalize our data. We'll use NormalizeStandardize (which gives us
		// mean 0, unit variance):
		DataNormalization normalizer = new NormalizerStandardize();
		normalizer.fit(trainingData); // Collect the statistics (mean/stdev) from the training data. This does not
										// modify the input data
		normalizer.transform(trainingData); // Apply normalization to the training data
		normalizer.transform(testData); // Apply normalization to the test data. This is using statistics calculated
										// from the *training* set

		// Configure neural network
		final int numInputs = 100;
		int outputNum = 2;
		int epochs = 1000;
		long seed = 6;

        log.info("Build model....");
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(seed)
                .activation(Activation.TANH)
                .weightInit(WeightInit.XAVIER)
                .updater(new Sgd(0.1))
                .l2(1e-4)
                .list()
                .layer(new DenseLayer.Builder().nIn(numInputs).nOut(3).build())
                .layer(new DenseLayer.Builder().nIn(3).nOut(3).build())
                .layer(new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .activation(Activation.SOFTMAX).nIn(3).nOut(outputNum).build())
                .build();

		// run the model
		MultiLayerNetwork model = new MultiLayerNetwork(conf);
		model.init();
		model.setListeners(new ScoreIterationListener(100));

		for (int i = 0; i < epochs; i++) {
			model.fit(trainingData);
		}

		// evaluate the model on the test set
		Evaluation eval = new Evaluation(2);
		INDArray output = model.output(testData.getFeatures());

		eval.eval(testData.getLabels(), output);
		log.info(eval.stats());

		classify(output, tweets);
		logTweets(tweets);

	}

	public static void logTweets(Map<Integer, Tweet> tweets) {
		for (int key : tweets.keySet()) {
			Tweet t = tweets.get(key);
			System.out.println("PREDICTED: " + t.getTweetClass() + " | ACTUAL: " + t.getSentiment());
		}
	}

	private static DataSet readCSVDataset(
            String csvFileClasspath, int batchSize, int labelIndex, int numClasses)
            throws IOException, InterruptedException{

        RecordReader rr = new CSVRecordReader();
        rr.initialize(new FileSplit(new ClassPathResource(csvFileClasspath).getFile()));
        DataSetIterator iterator = new RecordReaderDataSetIterator(rr,batchSize,labelIndex,numClasses);
        return iterator.next();
    }

	public Map<Integer, Tweet> objectify(DataSet testData) {
		Map<Integer, Tweet> iTweets = new HashMap<>();
		INDArray tweets = testData.getFeatures();
		INDArray sentiments = testData.getLabels();
		for (int i = 0; i < tweets.rows(); i++) {
			INDArray tweetSlice = tweets.slice(i);
			INDArray sentimentSlice = sentiments.slice(i);
			float[] tweetArray = getFloatArrayFromSlice(tweetSlice);
			int sentiment = sentimentSlice.getInt(0);
			Tweet t = new Tweet(sentiment, tweetArray);
			iTweets.put(i, t);
		}
		return iTweets;
	}

	private void classify(INDArray output, Map<Integer, Tweet> tweets) {
		for (int i = 0; i < output.rows(); i++) {
			Tweet irs = tweets.get(i);
			// set the classification from the fitted results
			irs.setTweetClass(classifiers.get(maxIndex(getFloatArrayFromSlice(output.slice(i)))));
		}
	}

	private static float[] getFloatArrayFromSlice(INDArray rowSlice) {
		float[] result = new float[rowSlice.columns()];
		for (int i = 0; i < rowSlice.columns(); i++) {
			result[i] = rowSlice.getFloat(i);
		}
		return result;
	}

	private static int maxIndex(float[] vals) {
		int maxIndex = 0;
		for (int i = 1; i < vals.length; i++) {
			float newnumber = vals[i];
			if ((newnumber > vals[maxIndex])) {
				maxIndex = i;
			}
		}
		return maxIndex;
	}

	public void printArray(float[] arr) {
		for (float f : arr) {
			System.out.print(f + " ");
		}
		System.out.println("\n");
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
	
	

}