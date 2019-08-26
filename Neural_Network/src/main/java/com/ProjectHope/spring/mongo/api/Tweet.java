package com.ProjectHope.spring.mongo.api;

import java.util.Arrays;

import org.nd4j.linalg.api.ndarray.INDArray;

public class Tweet {

	private int sentiment; // 0 is negative. 4 is positive
	private float[] tweetVector; // sentence to vector representation of tweet
	private float nlpFactor; // multiplication factor determined by stanford NLP library
	private float keywordFactor; // multiplication factor determined by stanford NLP library

	private String tweetClass;

	public Tweet(int sentiment, float[] tweetVector, float nlpFactor, float keywordFactor) {
		super();
		this.sentiment = sentiment;
		this.tweetVector = tweetVector;
		this.nlpFactor = nlpFactor;
		this.keywordFactor = keywordFactor;
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

	public float getNlpFactor() {
		return nlpFactor;
	}

	public void setNlpFactor(float nlpFactor) {
		this.nlpFactor = nlpFactor;
	}

	public float getKeywordFactor() {
		return keywordFactor;
	}

	public void setKeywordFactor(float keywordFactor) {
		this.keywordFactor = keywordFactor;
	}

	public String getTweetClass() {
		return tweetClass;
	}

	public void setTweetClass(String tweetClass) {
		this.tweetClass = tweetClass;
	}

	@Override
	public String toString() {
		return "Tweet [sentiment=" + sentiment + ", tweetVector=" + Arrays.toString(tweetVector) + ", nlpFactor="
				+ nlpFactor + ", keywordFactor=" + keywordFactor + ", tweetClass=" + tweetClass + "]";
	}

}