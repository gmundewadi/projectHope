package projectHope.RSS_Reader;



public class Article {

	
	
	private String title;
	private String pubDate;
	private String link;
	private String guid;
	private String author;
	private String thumbnail;
	private String description;
	private String content;
	
	 
	public String toString() {
		return "article [title=" + title + ", description=" + description + ", link=" + link + "]";
	}


	public Article(String title, String description, String link) {
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
