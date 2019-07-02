package projectHope.model;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "article")
public class article {

	@Id
	private String title;
	private String description;
	private String link;
	
	
	
	
	public String toString() {
		return "article [title=" + title + ", description=" + description + ", link=" + link + "]";
	}


	public article(String title, String description, String link) {
		super();
		this.title = title;
		this.description = description;
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
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	
	
}
