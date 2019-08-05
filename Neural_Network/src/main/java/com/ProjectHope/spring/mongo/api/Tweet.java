package com.ProjectHope.spring.mongo.api;

import java.util.Arrays;

import org.nd4j.linalg.api.ndarray.INDArray;

public class Tweet {

	private int sentiment; // 0 is negative. 4 is positive
	private float[] tweetArray; // sentence to vector representation of tweet

	private String tweetClass;

	
	public Tweet(int sentiment, float[] tweetArr) {
		super();
		this.sentiment = sentiment;
		this.tweetArray = tweetArr;
	}

	public int getSentiment() {
		return sentiment;
	}

	public void setSentiment(int sentiment) {
		this.sentiment = sentiment;
	}

	public float[] getTweetArray() {
		return tweetArray;
	}

	public void setTweetArray(float[] tweetArray) {
		this.tweetArray = tweetArray;
	}

	public String getTweetClass() {
		return tweetClass;
	}

	public void setTweetClass(String tweetClass) {
		this.tweetClass = tweetClass;
	}

	@Override
	public String toString() {
		return "Tweet [sentiment=" + sentiment + ", tweetArray=" + tweetArray.toString();
	}
	

}