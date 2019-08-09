package com.ProjectHope.spring.mongo.api;

import java.util.Arrays;

import org.nd4j.linalg.api.ndarray.INDArray;

public class Tweet {

	private int sentiment; // 0 is negative. 4 is positive
	private String tweet; // sentence to vector representation of tweet

	private String tweetClass;

	
	public Tweet(int sentiment, String tweetArr) {
		super();
		this.sentiment = sentiment;
		this.tweet = tweetArr;
	}

	public int getSentiment() {
		return sentiment;
	}

	public void String(int sentiment) {
		this.sentiment = sentiment;
	}

	public String getTweet() {
		return tweet;
	}

	public void setTweet(String tweet) {
		this.tweet = tweet;
	}

	public String getTweetClass() {
		return tweetClass;
	}

	public void setTweetClass(String tweetClass) {
		this.tweetClass = tweetClass;
	}

	@Override
	public String toString() {
		return "Tweet [sentiment=" + sentiment + ", tweetArray=" + tweet.toString();
	}
}