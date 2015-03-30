package model;

import java.util.ArrayList;

public class Theme {
    String themeTitle;
    ArrayList<Question> questions;

    public Theme(String themeTitle, ArrayList<Question> questions) {
        this.themeTitle = themeTitle;
        this.questions = questions;
    }

    public String getThemeTitle() {
        return themeTitle;
    }

    public void setThemeTitle(String themeTitle) {
        this.themeTitle = themeTitle;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    public int size() {
        return questions.size();
    }
}