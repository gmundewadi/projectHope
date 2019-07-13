package com.ProjectHope.spring.mongo.api.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Article")
public class Article {
	
	private String link;
	private String title;
	private String description;
		

	public Article(String title, String description, String link) {
		
		this.title = title;
		this.description = description;
		this.link = link;
	}

	public String toString() {
		return "Article [title = " + title + "description = "
				+ description + ", link=" + link + "]";
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}	
}
