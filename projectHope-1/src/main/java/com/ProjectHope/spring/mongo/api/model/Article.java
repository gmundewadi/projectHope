package com.ProjectHope.spring.mongo.api.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Article")
public class Article {

	@Id
	private String link;
	private String title;
	private String description;
	private String url;
	private String pubDate;
	private String image;

	public Article(String link, String title, String description, String url, String pubDate, String image) {
		super();
		this.link = link;
		this.title = title;
		this.description = description;
		this.url = url;
		this.pubDate = pubDate;
		this.image = image;
	}

	@Override
	public String toString() {
		return "Article [link=" + link + ", title=" + title + ", description=" + description + ", url=" + url
				+ ", pubDate=" + pubDate + ", image=" + image + "]";
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
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

	public String getUrl() {
		return url;
	}

	public void setUri(String url) {
		this.url = url;
	}

	public String getPubDate() {
		return pubDate;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

}
