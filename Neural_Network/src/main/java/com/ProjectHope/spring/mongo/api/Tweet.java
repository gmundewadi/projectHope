package com.ProjectHope.spring.mongo.api;

import java.util.Arrays;

import org.nd4j.linalg.api.ndarray.INDArray;

public class Tweet {

	private int sentiment; // 0 is negative. 4 is positive
	private float[] tweetVector; // sentence to vector representation of tweet
	private double factor; // multiplication factor determined by stanford NLP library

	private String tweetClass;

	public Tweet(int sentiment, float[] tweetVector, double factor) {
		super();
		this.sentiment = sentiment;
		this.tweetVector = tweetVector;
		this.factor = factor;
	}

	public double getFactor() {
		return factor;
	}

	public void setFactor(double factor) {
		this.factor = factor;
	}

	public int getSentiment() {
		return sentiment;
	}

	public void setSentiment(int sentiment) {
		this.sentiment = sentiment;
	}

	public float[] getTweetVector() {
		return tweetVector;
	}

	public void setTweetVector(float[] tweetVector) {
		this.tweetVector = tweetVector;
	}

	public String getTweetClass() {
		return tweetClass;
	}

	public void setTweetClass(String tweetClass) {
		this.tweetClass = tweetClass;
	}

	@Override
	public String toString() {
		return "Tweet [sentiment=" + sentiment + ", tweetVector=" + Arrays.toString(tweetVector) + ", factor=" + factor
				+ ", tweetClass=" + tweetClass + "]";
	}

}