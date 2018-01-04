package com.toomtoome.fire.Model;

import java.util.HashMap;

/**
 * Created by moogunjung on 12/8/17.
 */

public class Chapter {

    String id;
    String title;
    String chapterDescription;
    HashMap<String, Integer> keywords;
    HashMap<String, Integer> questions;

    public Chapter(String id, String title, String chapterDescription, HashMap<String, Integer> keywords, HashMap<String, Integer> questions) {
        this.id = id;
        this.title = title;
        this.chapterDescription = chapterDescription;
        this.keywords = keywords;
        this.questions = questions;
    }

    public Chapter() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChapterDescription() {
        return chapterDescription;
    }

    public void setChapterDescription(String chapterDescription) {
        this.chapterDescription = chapterDescription;
    }

    public HashMap<String, Integer> getKeywords() {
        return keywords;
    }

    public void setKeywords(HashMap<String, Integer> keywords) {
        this.keywords = keywords;
    }

    public HashMap<String, Integer> getQuestions() {
        return questions;
    }

    public void setQuestions(HashMap<String, Integer> questions) {
        this.questions = questions;
    }
}
