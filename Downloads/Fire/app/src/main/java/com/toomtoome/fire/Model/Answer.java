package com.toomtoome.fire.Model;

/**
 * Created by moogunjung on 12/8/17.
 */

public class Answer {

    String uid;
    String profileImageUrl;
    String username;

    String text;
    String questionId;
    Double createdAt;

    //String createdAt;
    //Map<String, String> createdAt;

//    public Answer(String uid, String profileImageUrl, String username, String text) {
//        this.uid = uid;
//        this.profileImageUrl = profileImageUrl;
//        this.username = username;
//        this.text = text;
//    }


    public Answer(String uid, String profileImageUrl, String username, String text, String questionId, Double createdAt) {
        this.uid = uid;
        this.profileImageUrl = profileImageUrl;
        this.username = username;
        this.text = text;
        this.questionId = questionId;
        this.createdAt = createdAt;
    }

    public Answer() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String aText) {
        this.text = text;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public Double getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Double createdAt) {
        this.createdAt = createdAt;
    }
}
