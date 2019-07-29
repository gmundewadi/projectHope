package projectHope.RSS_Reader;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class WordToVec {

	private static MongoClient mongoClient;

	public static void main(String args[]) {
		MongoClientURI uriVal = new MongoClientURI(
				"mongodb+srv://gautam:projectHope@cluster0-biq2l.azure.mongodb.net/test?retryWrites=true&w=majority");
		mongoClient = new MongoClient(uriVal);
		WordToVec v = new WordToVec();
		v.updateWordFile();
		v.wordToVec();
		
	}

	public void updateWordFile() {
		MongoDatabase mongoDB = mongoClient.getDatabase("RSS_Reader");
		MongoCollection<Document> collection = mongoDB.getCollection("Article");
		FindIterable<Document> findIterable = collection.find(new Document());
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("./words"));
			for(Document d: findIterable) {
				String title = d.getString("title");
				writer.write(title+"\n");

			}
			writer.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}		
	}
	
	public void wordToVec() {
		
	}
	
	
}

