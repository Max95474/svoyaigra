package service;

import model.*;
import model.Package;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URISyntaxException;;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuestionService {
    private static QuestionService questionService;
    private String serverUrl;

    private QuestionService() {
        //read question-server.xml
        Document questionServerDocument = getDocument("/question-server.xml");
        NodeList urlNode = questionServerDocument.getElementsByTagName("server-url");
        serverUrl = urlNode.item(0).getFirstChild().getTextContent();
        //don't forget to handle errors and exceptions
    }

    public static QuestionService getInstance() {
        if(questionService == null) {
            questionService = new QuestionService();
        }
        return questionService;
    }

    public ArrayList<Game> getGamesList() {
        ArrayList<Game> gamesList = new ArrayList<>();

        Document gamesDocument = getDocument("/questions/games.xml");
        NodeList gameNodes     = gamesDocument.getElementsByTagName("tour");
        for(int i = 0; i < gameNodes.getLength(); i++) {
            Element element           = (Element)gameNodes.item(i);

            NodeList titleNode        = element.getElementsByTagName("Title");
            NodeList idNode           = element.getElementsByTagName("Id");
            NodeList textIdNode       = element.getElementsByTagName("TextId");
            NodeList lastUpdatedNode  = element.getElementsByTagName("LastUpdated");
            NodeList createdAtNode    = element.getElementsByTagName("CreatedAt");
            NodeList questionsNumNode = element.getElementsByTagName("QuestionsNum");

            gamesList.add(new Game(
                    titleNode.item(0).getFirstChild().getTextContent(),
                    idNode.item(0).getFirstChild().getTextContent(),
                    textIdNode.item(0).getFirstChild().getTextContent(),
                    lastUpdatedNode.item(0).getFirstChild().getTextContent(),
                    createdAtNode.item(0).getFirstChild().getTextContent(),
                    Integer.parseInt(questionsNumNode.item(0).getFirstChild().getTextContent())
            ));
        }

        return gamesList;
    }

    public ArrayList<Tour> getToursList(Game game) {
        ArrayList<Tour> toursList = new ArrayList<>();

        String docLocation = "/questions/" + game.getTextId() + "/" + game.getTextId() + ".xml";
        Document toursDocument = getDocument(docLocation);
        NodeList tourNodes = toursDocument.getElementsByTagName("tour");

        for(int i = 0; i < tourNodes.getLength(); i++) {
            Element element = (Element)tourNodes.item(i);

            NodeList titleNode = element.getElementsByTagName("Title");
            NodeList idNode = element.getElementsByTagName("Id");
            NodeList textIdNode = element.getElementsByTagName("TextId");
            NodeList lastUpdatedNode = element.getElementsByTagName("LastUpdated");
            NodeList createdAtNode = element.getElementsByTagName("CreatedAt");
            NodeList questionsNumNode = element.getElementsByTagName("QuestionsNum");

            //===================Workaround============================
            String documentLocation = "/questions/" + game.getTextId() + "/packages/" + textIdNode.item(0)
                    .getFirstChild().getTextContent() + ".xml";
            Document document = getDocument(documentLocation);
            //=========================================================
            if(document.getElementsByTagName("question").getLength() != 0) {
                toursList.add(new Tour(
                        titleNode.item(0).getFirstChild().getTextContent(),
                        idNode.item(0).getFirstChild().getTextContent(),
                        textIdNode.item(0).getFirstChild().getTextContent(),
                        lastUpdatedNode.item(0).getFirstChild().getTextContent(),
                        createdAtNode.item(0).getFirstChild().getTextContent(),
                        Integer.parseInt(questionsNumNode.item(0).getFirstChild().getTextContent())
                ));
            }
        }

        return toursList;
    }

    public Package getPackage(Game game, Tour tour) {
        ArrayList<Theme> themeList = new ArrayList<>();
        ArrayList<Question> questionList = new ArrayList<>();
        ArrayList<String> textQuestions;
        ArrayList<String> textAnswers;

        String documentLocation = "/questions/" + game.getTextId() + "/packages/" + tour.getTextId() + ".xml";
        Document document = getDocument(documentLocation);
        NodeList nodeList = document.getElementsByTagName("question");
        for(int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element)nodeList.item(i);

            //Questions
            NodeList questionNode = element.getElementsByTagName("Question");
            String questionsText = questionNode.item(0).getFirstChild().getTextContent();
            questionsText = questionsText.replace("\n", " ");
            textQuestions = getQuestions(questionsText);

            //Answers
            NodeList answerNode = element.getElementsByTagName("Answer");
            String answersText = answerNode.item(0).getFirstChild().getTextContent();
            answersText = answersText.replace("\n", " ");
            answersText = answersText.replace("\"", "&");
            textAnswers = getQuestions(answersText);

            for(int j = 0; j < textQuestions.size(); j++) {
                //delete this later
                if((textAnswers.size() == 4 || textQuestions.size() == 4) && j == 4) {
                    break;
                }
                questionList.add(new Question(textQuestions.get(j), textAnswers.get(j), (j+1)*10));
            }
            themeList.add(new Theme(getTheme(questionsText), questionList));
            questionList = new ArrayList<>();
        }

        nodeList = document.getElementsByTagName("tournament");
        Element element = (Element)nodeList.item(0);
        NodeList theme = element.getElementsByTagName("Title");

        return new Package(theme.item(0).getFirstChild().getTextContent(), themeList);
    }

    private Document getDocument(String fileName) {
        Document dom = null;
        try {
            //InputStream inputStream = getClass().getResourceAsStream(fileName);
            File file = new File(getClass().getResource(fileName).toURI());
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            dom = db.parse(file);
        } catch(ParserConfigurationException ex) {
            System.err.println(ex.getMessage());
        } catch(SAXException ex) {
            System.err.println(ex.getMessage());
        } catch(IOException ex) {
            System.err.println(ex.getMessage());
        } catch(URISyntaxException ex) {
            System.err.println(ex.getMessage());
        }
        return dom;
    }

    private String getTheme(String text) {
        Pattern pattern = Pattern.compile(".+(?= 1\\.)");
        Matcher matcher = pattern.matcher(text);
        matcher.find();
        return matcher.group();
    }

    private ArrayList<String> getQuestions(String text) {
        ArrayList<String> questions = new ArrayList<>();

        Pattern pattern;
        Matcher matcher;
        String regexp;
        String s;
        for(int i = 1; i <= 5; i++) {
            if(i != 5) {
                regexp = "\\s" + i + ".+(?=" + (i+1) + "\\.)";
                pattern = Pattern.compile(regexp);
                matcher = pattern.matcher(text);
                if(!matcher.find()) {
                    continue;
                }
                s = matcher.group();
                questions.add(s.substring(4, s.length()));
            } else {
                regexp = "\\s" + i + ".+";
                pattern = Pattern.compile(regexp);
                matcher = pattern.matcher(text);
                if(!matcher.find()) {
                    continue;
                }
                s = matcher.group();
                questions.add(s.substring(4, s.length()));
            }
        }
        return questions;
    }

    /*private ArrayList<String> getAnswers(String text) {
        return getQuestions(text);
    }*/

    public String getServerUrl() {
        return serverUrl;
    }

//    public Package getTestPackage() {
//        ArrayList<Theme> themeList = new ArrayList<>();
//        ArrayList<Question> questionList = new ArrayList<>();
//        ArrayList<String> textQuestions;
//        ArrayList<String> textAnswers;
//
//        Document document = getDocument("res/packages/enot03sv.xml");
//
//        NodeList nodeList = document.getElementsByTagName("question");
//        for(int i = 0; i < nodeList.getLength(); i++) {
//            Element element = (Element)nodeList.item(i);
//
//            //Questions
//            NodeList questionNode = element.getElementsByTagName("Question");
//            String questionsText = questionNode.item(0).getFirstChild().getTextContent();
//            questionsText = questionsText.replace("\n", " ");
//            textQuestions = getQuestions(questionsText);
//
//            //Answers
//            NodeList answerNode = element.getElementsByTagName("Answer");
//            String answersText = answerNode.item(0).getFirstChild().getTextContent();
//            answersText = answersText.replace("\n", " ");
//            answersText = answersText.replace("\"", "&");
//            textAnswers = getQuestions(answersText);
//
//            for(int j = 0; j < textQuestions.size(); j++) {
//                questionList.add(new Question(textQuestions.get(j), textAnswers.get(j), (j+1)*10));
//            }
//            themeList.add(new Theme(getTheme(questionsText), questionList));
//            questionList = new ArrayList<>();
//        }
//
//        nodeList = document.getElementsByTagName("tournament");
//        Element element = (Element)nodeList.item(0);
//        NodeList theme = element.getElementsByTagName("Title");
//
//        return new Package(theme.item(0).getFirstChild().getTextContent(), themeList);
//    }

}