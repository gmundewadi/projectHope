package com.ProjectHope.spring.mongo.api;

public class Tweet {

	private int sentiment;
	private String text;

	private String tweetClass;

	public Tweet(int sentiment, String text) {
		super();
		this.sentiment = sentiment;
		this.text = text;
	}

	@Override
	public String toString() {
		return "Tweet [sentiment=" + sentiment + ", text=" + text + ", tweetClass=" + tweetClass + "]";
	}

	public int getSentiment() {
		return sentiment;
	}

	public void setSentiment(int sentiment) {
		this.sentiment = sentiment;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getTweetClass() {
		return tweetClass;
	}

	public void setTweetClass(String tweetClass) {
		this.tweetClass = tweetClass;
	}

}