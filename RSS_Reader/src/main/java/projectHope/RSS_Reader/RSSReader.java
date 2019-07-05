package projectHope.RSS_Reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;


public class RSSReader {
	
	private MongoClient mongoClient;
	private static ArrayList<Article> articles;
	
	public RSSReader() {
		mongoClient = new MongoClient("localhost",27017);
		articles = new ArrayList<Article>();
	}
	
	public static void main(String[] args) {
		RSSReader r = new RSSReader();
		r.updateDB();
		for(Article a: articles) {
			System.out.println(a.toString());
		}
	}
	
	public void updateDB() {
		DB database = mongoClient.getDB("RSS_Reader");
		DBCollection collection = database.getCollection(("Article"));
		BasicDBObject document = new BasicDBObject();
		readRSS("http://rss.cnn.com/rss/cnn_topstories.rss");
		collection.insert(document);
	}
	
		
	public String serializeArticle(Article article) {
		Gson gson = new Gson();
		String json = gson.toJson(article);
		return json;
	}
	
	public Article deserializeArticle(String json) {
		Gson gson = new Gson();
		Article result = gson.fromJson((json), Article.class);
		articles.add(result);
		return result;
	}
	
	
	 public void readRSS(String urlAddress){
	        try{
	            URL rssUrl = new URL(urlAddress);
	            BufferedReader in = new BufferedReader(new InputStreamReader(rssUrl.openStream()));
	            String line;
	            while((line = in.readLine()) != null)
	            	deserializeArticle(line);
	            in.close();
	        } catch (MalformedURLException ue){
	            System.out.println("URL provided is not found");
	        } catch (IOException ioe){
	            System.out.println("An error was encountered when reading rss contents");
	        }

	    }
	
	
}
