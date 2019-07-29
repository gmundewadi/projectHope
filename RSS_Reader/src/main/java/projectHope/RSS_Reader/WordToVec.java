package projectHope.RSS_Reader;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.datavec.api.util.ClassPathResource;

import org.datavec.api.util.ClassPathResource;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WordToVec {

	private static MongoClient mongoClient;
	private static Logger log = LoggerFactory.getLogger(WordToVec.class);

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
			BufferedWriter writer = new BufferedWriter(new FileWriter("./words.txt"));
			for (Document d : findIterable) {
				String title = d.getString("title");
				writer.write(title + "\n");

			}
			writer.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void wordToVec() {
		try {
			String filePath = new ClassPathResource("words.txt").getFile().getAbsolutePath();

			log.info("Load & Vectorize Sentences....");
			// Strip white space before and after for each line
			SentenceIterator iter = new BasicLineIterator(filePath);
			// Split on white spaces in the line to get words
			TokenizerFactory t = new DefaultTokenizerFactory();

			/*
			 * CommonPreprocessor will apply the following regex to each token:
			 * [\d\.:,"'\(\)\[\]|/?!;]+ So, effectively all numbers, punctuation symbols and
			 * some special symbols are stripped off. Additionally it forces lower case for
			 * all tokens.
			 */
			t.setTokenPreProcessor(new CommonPreprocessor());

			log.info("Building model....");
			Word2Vec vec = new Word2Vec.Builder().minWordFrequency(5).iterations(1).layerSize(100).seed(42)
					.windowSize(5).iterate(iter).tokenizerFactory(t).build();

			log.info("Fitting Word2Vec model....");
			vec.fit();

			log.info("Writing word vectors to text file....");

			// Prints out the closest 10 words to "day". An example on what to do with these
			// Word Vectors.
			log.info("Closest Words:");
			Collection<String> lst = vec.wordsNearestSum("day", 10);
			log.info("10 Words closest to 'day': {}", lst);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
