package projectHope.RSS_Reader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.rometools.fetcher.FeedFetcher;
import com.rometools.fetcher.FetcherException;
import com.rometools.fetcher.impl.HttpURLFeedFetcher;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;


@SuppressWarnings("deprecation")
public class RSSReader {
	
	private  MongoClient mongoClient;
	
	public RSSReader() {
		mongoClient = new MongoClient("localhost",27017);
	}
	
	public static void main(String[] args) {
		RSSReader r = new RSSReader();
		//r.updateDB();
		System.out.println(r.countArticles());
	}
	
	public void updateDB() {
        try { 
    		DB database = mongoClient.getDB("RSS_Reader");
    		DBCollection collection = database.getCollection(("Article"));
        	FeedFetcher fetcher = new HttpURLFeedFetcher();
			SyndFeed feed = fetcher.retrieveFeed(new URL("http://rss.cnn.com/rss/cnn_topstories.rss"));
			for(Object o: feed.getEntries()) {
				SyndEntry entry = (SyndEntry) o;
				String title = entry.getTitle();
				String link = entry.getLink();
				String description = entry.getDescription().getValue();
				
				
				BasicDBObject document = new BasicDBObject();
				document.append("_id", link);
				if(collection.findOne(document) != null)
					continue;
				document.append("title", title);
				document.append("link", link);
				document.append("description", description);
		        collection.insert((document));
			}
			
        } catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FeedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FetcherException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public int countArticles() {
		DB database = mongoClient.getDB("RSS_Reader");
		DBCollection collection = database.getCollection(("Article"));
		return (int) collection.getCount();
	}
}
