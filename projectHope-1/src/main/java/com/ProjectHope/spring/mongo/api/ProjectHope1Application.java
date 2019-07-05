package com.ProjectHope.spring.mongo.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.mongodb.MongoClient;

@SpringBootApplication
public class ProjectHope1Application {
	

	public static void main(String[] args) {
		MongoRSSFeed mrf = new MongoRSSFeed();
		mrf.updateDB();
		SpringApplication.run(ProjectHope1Application.class, args);
	}
	

}
