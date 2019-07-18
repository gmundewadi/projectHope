package projectHope.RSS_Reader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;

import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.rometools.fetcher.FeedFetcher;
import com.rometools.fetcher.FetcherException;
import com.rometools.fetcher.impl.HttpURLFeedFetcher;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;

@SuppressWarnings("deprecation")
public class RSSReader {
	
	private static  MongoClient mongoClient;
	

	public static void main(String[] args) {
		MongoClientURI uriVal = new MongoClientURI("mongodb+srv://gautam:projectHope@cluster0-biq2l.azure.mongodb.net/test?retryWrites=true&w=majority");
    	mongoClient = new MongoClient(uriVal);
		updateDB();
		System.out.println(countArticles());
	}
	
	public static void updateDB() {
        try { 

        	
        	MongoDatabase database = mongoClient.getDatabase("RSS_Reader");
        	
    		MongoCollection<Document> collection = database.getCollection("Article");
        	FeedFetcher fetcher = new HttpURLFeedFetcher();
        	SyndFeed feed;
        	File file = new File("rssFeeds");
        	Scanner sc = new Scanner(file);
        	while(sc.hasNextLine()) {
        		String url = sc.nextLine();
    			feed = fetcher.retrieveFeed(new URL(url));
    			for(Object o: feed.getEntries()) {
    				SyndEntry entry = (SyndEntry) o;
    				String title = entry.getTitle();
    				String link = entry.getLink();
    				String description = entry.getDescription().getValue();
    				Date pubDate = entry.getPublishedDate();
    				String uri = entry.getUri();
    				Document document = new Document();
    				document.append("link", link);
    				long count = collection.countDocuments(new BsonDocument("link", new BsonString(link)));    					
    				if(count > 1) {
    					continue;
    				}
    				document.append("title", title);
    				document.append("description", description);
    			
    				document.append("pubDate", pubDate);
    				document.append("uri", uri);
    		        collection.insertOne((document));
    			}
        	}
        	sc.close();	
        } catch (IllegalArgumentException e) {
			System.out.println("Illegal Arguement Exception");
			e.printStackTrace();
		} catch (MalformedURLException e) {
			System.out.println("MalformedURL Exception");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IO Exception");
			e.printStackTrace();
		} catch (FeedException e) {
			System.out.println("Feed Exception");
			e.printStackTrace();
		} catch (FetcherException e) {
			System.out.println("Fetchter Exception");
			e.printStackTrace();
		}	
	}
	
	public static int countArticles() {
		MongoDatabase database = mongoClient.getDatabase("RSS_Reader");
		MongoCollection<Document> collection = database.getCollection("Article");
		return (int) collection.countDocuments();
	}
}
