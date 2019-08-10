package com.ProjectHope.spring.mongo.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
import org.nd4j.linalg.lossfunctions.LossFunctions;

public class TweetClassifier {

	private static Map<Integer, String> classifiers;

	public TweetClassifier() {
		classifiers = new HashMap<>();
		classifiers.put(0, "negative");
		classifiers.put(1, "positive");

	}

	public void classify(String twitterDataTrainFile, String twitterDataTestFile)
			throws FileNotFoundException, IOException, InterruptedException {

		int labelIndex = 0;
		int numClasses = 2;

		int batchSizeTraining = 100000;

		System.out.println("----Loading training data---");

		DataSet trainingData = readCSVDataset(twitterDataTrainFile, batchSizeTraining, labelIndex, numClasses);

		// shuffle our training data to avoid any impact of ordering
		trainingData.shuffle();

		int batchSizeTest = 5;
		System.out.println("----Loading test data---");
		DataSet testData = readCSVDataset(twitterDataTestFile, batchSizeTest, labelIndex, numClasses);

		Map<Integer, Tweet> tweets = objectify(trainingData);

		// Neural nets all about numbers. Lets normalize our data
		DataNormalization normalizer = new NormalizerStandardize();
		// Collect the statistics from the training data. This does
		// not modify the input data
		normalizer.fit(trainingData);

		// Apply normalization to the training data
		normalizer.transform(trainingData);

		// Apply normalization to the test data.
		normalizer.transform(testData);

		int numInputs = 100;
		int outputNum = 2;
		int iterations = 1000;
		long seed = 123;

		System.out.println("Building model....");
		MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder().seed(seed).activation(Activation.TANH)
				.optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT).weightInit(WeightInit.XAVIER)
				.l2Bias(1e-4).list().layer(0, new DenseLayer.Builder().nIn(numInputs).nOut(3).build())
				.layer(1, new DenseLayer.Builder().nIn(3).nOut(3).build())
				.layer(2, new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
						.activation(Activation.SOFTMAX).nIn(3).nOut(outputNum).build())
				.build();

		MultiLayerNetwork model = new MultiLayerNetwork(conf);
		model.init();
		model.setListeners(new ScoreIterationListener(100));

		for (int i = 0; i < iterations; i++) {
			model.fit(trainingData);
		}

		// evaluate the model on the test set
		Evaluation eval = new Evaluation(3);
		INDArray output = model.output(testData.getFeatures());

		eval.eval(testData.getLabels(), output);

		System.out.println(eval.stats());

		System.out.println(output);

		classify(output, tweets);

	}

	public DataSet readCSVDataset(String csvFileClasspath, int batchSize, int labelIndex, int numClasses)
			throws IOException, InterruptedException {

		RecordReader rr = new CSVRecordReader();
		rr.initialize(new FileSplit(new ClassPathResource(csvFileClasspath).getFile()));
		DataSetIterator iterator = new RecordReaderDataSetIterator(rr, batchSize, labelIndex, numClasses);
		return iterator.next();
	}

	public Map<Integer, Tweet> objectify(DataSet testData) {
		Map<Integer, Tweet> iTweets = new HashMap<>();
		INDArray features = testData.getFeatures();
		for (int i = 0; i < features.rows(); i++) {
			INDArray slice = features.slice(i);
			float[] tweetArray = getFloatArrayFromSlice(slice);
			Tweet t = new Tweet(slice.getInt(0), tweetArray);
			iTweets.put(i, t);
		}
		return iTweets;
	}

	private void classify(INDArray output, Map<Integer, Tweet> flowers) {
		for (int i = 0; i < output.rows(); i++) {
			Tweet irs = flowers.get(i);
			// set the classification from the fitted results
			irs.setTweetClass(classifiers.get(maxIndex(getFloatArrayFromSlice(output.slice(i)))));
		}
	}

	private float[] getFloatArrayFromSlice(INDArray rowSlice) {
		float[] result = new float[rowSlice.columns()];
		int index = 0;
		for (int i = 1; i < rowSlice.columns(); i++) {
			result[index] = rowSlice.getFloat(i);
			index++;
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

}