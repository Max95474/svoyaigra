package model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Game {
    private String title;
    private String id;
    private String textId;
    private Date lastUpdated;
    private Date createdAt;
    private int questionsNum;

    public Game(String title, String id, String textId,
                String lastUpdated, String createdAt, int questionsNum) {
        this.title = title;
        this.id = id;
        this.textId = textId;
        //convert string to date
        try {
            this.lastUpdated = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(lastUpdated);
            this.createdAt = new SimpleDateFormat("yyyy-MM-dd").parse(createdAt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.questionsNum = questionsNum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTextId() {
        return textId;
    }

    public void setTextId(String textId) {
        this.textId = textId;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getQuestionsNum() {
        return questionsNum;
    }

    public void setQuestionsNum(int questionsNum) {
        this.questionsNum = questionsNum;
    }
}
