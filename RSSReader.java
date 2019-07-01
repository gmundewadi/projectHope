import java.io.*;
import java.net.*;
import com.mongodb.client.MongoDatabase; 
import com.mongodb.MongoClient; 
import com.mongodb.MongoCredential; 

public class RSSReader{

    public static void main(String[]args){
        System.out.println("\n\nTEST FOR parsing RSS titles");
        System.out.println("\n-----------------------------\n");
        System.out.println(readRSS("http://rss.cnn.com/rss/cnn_topstories.rss") + "");
        System.out.println("\n-----------------------------\n");
    }


    public static String readRSS(String urlAddress){
        try{
            URL rssUrl = new URL(urlAddress);
            BufferedReader in = new BufferedReader(new InputStreamReader(rssUrl.openStream()));
            String sourceCode = "";
            String line;
            while((line = in.readLine()) != null){
                if(line.contains("<title><![CDATA[")){
                    int firstPos = line.indexOf("<title><![CDATA[");
                    String temp = line.substring(firstPos);
                    temp = temp.replace("<title><![CDATA[" , "");
                    int lastPos = temp.indexOf("</title>");
                    temp = temp.substring(0,lastPos);
                    lastPos = temp.indexOf("]]>");
                    temp = temp.substring(0,lastPos);
                    sourceCode +=temp+"\n";
                }
            }
            in.close();
            return sourceCode;
        } catch (MalformedURLException ue){
            System.out.println("URL provided is not found");
        } catch (IOException ioe){
            System.out.println("An error was encountered when reading rss contents");
        }
        return null;

    }
}