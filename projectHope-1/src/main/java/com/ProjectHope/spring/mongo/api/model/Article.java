package com.ProjectHope.spring.mongo.api.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Article")
public class Article {

	// to work on here --> resolve issue with id not being saved correclty
	
	// next: load a bunch of RSS feeds from a file and save into local database
	
	// save all that information to an external database
	
	private String title;
	private String author;
	private String description;
	@Id
	private String link;
	

	public Article(String title, String author, String description, String link) {
		this.title = title;
		this.author = author;
		this.description = description;
		this.link = link;
	}

	public String toString() {
		return "Article [title = " + title + ", author = " + author + ", description = "
				+ description + ", link=" + link + "]";
	}
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
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
