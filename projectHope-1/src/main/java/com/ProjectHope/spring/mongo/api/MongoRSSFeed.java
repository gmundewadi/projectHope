package com.ProjectHope.spring.mongo.api;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class MongoRSSFeed {
	
	private MongoClient mongoClient;
	
	public MongoRSSFeed() {
		mongoClient = new MongoClient("localhost",27017);
	}
	
	public void updateDB() {
		DB database = mongoClient.getDB("RSS_Reader");
		DBCollection collection = database.getCollection(("Article"));
		BasicDBObject document = new BasicDBObject();
		document.put("title", "CNN Test");
		document.put("description", "CNN Test desc");
		document.put("link", "CNN Test link");
		collection.insert(document);
	}
	
	
}
