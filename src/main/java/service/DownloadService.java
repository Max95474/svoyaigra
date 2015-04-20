package service;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class DownloadService {
    public static void saveTours() {
        Document doc = null;
        try {
            File file = new File("/questions/SVOYAK/SVOYAK.xml");
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(file);
        } catch(ParserConfigurationException ex) {
            System.err.println("saveTours: ParserConfigurationException");
            return;
        } catch(SAXException ex) {
            System.err.println("saveTours: SAXException");
            return;
        } catch(IOException ex) {
            System.err.println("saveTours: IOException");
            ex.printStackTrace();
            return;
        }

        NodeList nodeList = doc.getElementsByTagName("tour");
        for(int i = 0; i < nodeList.getLength(); i++) {
            //get tour id
            Element element = (Element)nodeList.item(i);
            NodeList nodes = element.getElementsByTagName("TextId");
            String textId = nodes.item(0).getTextContent();
            //save file to directory
            saveFromUrl("http://db.chgk.info/tour/" + textId + "/xml",
                    "/questions/SVOYAK/packages/" + textId + ".xml");
            System.out.println("Saving from: " + "http://db.chgk.info/tour/" + textId + "/xml...");
        }
    }

    public static void saveFromUrl(String url, String location) {
        try {
            URL website = new URL(url);
            ReadableByteChannel rbc = Channels.newChannel(website.openStream());
            FileOutputStream fos = new FileOutputStream(location);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (MalformedURLException ex) {
            System.err.println("saveFromUrl: got MalformedURLException");
        } catch (FileNotFoundException ex) {
            System.err.println("saveFromUrl: got FileNotFoundException");
            System.err.println(ex.getMessage());
        } catch (IOException ex) {
            System.err.println("saveFromUrl: got IOException");
            System.err.println(ex.getMessage());
        }
    }

    public static void update() {
        //saveFromUrl(serverUrl + "/xml", "config/games.xml");
        //saveTours();
    }
}
