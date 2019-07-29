package projectHope.RSS_Reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.Document;
import org.jdom2.input.SAXBuilder;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

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

	private static MongoClient mongoClient;

	public static void main(String[] args) {
		MongoClientURI uriVal = new MongoClientURI(
				"mongodb+srv://gautam:projectHope@cluster0-biq2l.azure.mongodb.net/test?retryWrites=true&w=majority");
		mongoClient = new MongoClient(uriVal);
		updateDB();
		System.out.println(countArticles());

	}

	public static void printArray(List<String> arr) {
		for (int i = 0; i < arr.size(); i++) {
			System.out.println(arr.get(i));
		}
	}

	public static void updateDB() {
		try {
			MongoDatabase database = mongoClient.getDatabase("RSS_Reader");
			MongoCollection<Document> collection = database.getCollection("Article");
			File file = new File("rssFeeds.txt");
			Scanner sc = new Scanner(file);
			while (sc.hasNextLine()) {
				String url = sc.nextLine();
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				org.w3c.dom.Document doc = db.parse(new InputSource(new URL(url).openStream()));
				doc.getDocumentElement().normalize();

				NodeList itemList = doc.getElementsByTagName("item");
				for (int i = 0; i < itemList.getLength(); i++) {

					Node node = itemList.item(i);
					Element item = (Element) node;

					String title;
					NodeList titleList = item.getElementsByTagName("title");
					Element titleElement = (Element) titleList.item(0);
					titleList = titleElement.getChildNodes();
					title = titleList.item(0).getNodeValue();

					String description;
					NodeList descList = item.getElementsByTagName("description");
					Element descElement = (Element) descList.item(0);
					if (descElement == null) {
						description = "no description found";
					} else {
						descList = descElement.getChildNodes();
						description = descList.item(0).getNodeValue();
					}

					String link;
					NodeList linkList = item.getElementsByTagName("guid");
					Element linkElement = (Element) linkList.item(0);
					if (linkElement == null) {
						link = "no link found";
					} else {
						linkList = linkElement.getChildNodes();
						link = linkList.item(0).getNodeValue();
					}

					String pubDate;
					NodeList pubDateList = item.getElementsByTagName("pubDate");
					Element pubDateElement = (Element) pubDateList.item(0);
					if (pubDateElement == null) {
						pubDate = "no publication date found";
					} else {
						pubDateList = pubDateElement.getChildNodes();
						pubDate = pubDateList.item(0).getNodeValue();
					}

					String imgLink;
					NodeList mediaGroupList = item.getElementsByTagName("media:group");
					Element mediaGroupElement = (Element) mediaGroupList.item(0);
					if (mediaGroupElement == null) {
						imgLink = "no image found";

					} else {
						Element mediaContentElement = (Element) mediaGroupElement.getChildNodes().item(0);
						imgLink = mediaContentElement.getAttribute("url");
					}

					Document document = new Document();

					document.append("title", title);

					long count = collection.countDocuments(new BsonDocument("title", new BsonString(title)));
					if (count != 0) {
						continue;
					}

					document.append("link", link); // field used for identification
					document.append("url", link);
					document.append("description", description);
					document.append("pubDate", pubDate);
					document.append("image", imgLink);
					collection.insertOne(document);

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
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static int countArticles() {
		MongoDatabase database = mongoClient.getDatabase("RSS_Reader");
		MongoCollection<Document> collection = database.getCollection("Article");
		return (int) collection.countDocuments();
	}
}
