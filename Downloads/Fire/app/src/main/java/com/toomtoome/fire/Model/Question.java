package com.toomtoome.fire.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

/**
 * Created by moogunjung on 12/8/17.
 */

public class Question implements Parcelable {

    String id;
    String uid;
    String profileImageUrl;
    String username;
    Double createdAt;

    String text;
    String qImageUrl;
    String chapterId;

    HashMap<String, Integer> answers;
    HashMap<String, Integer> likes;
    HashMap<String, Integer> followers;

    public Question() {
    }

    public Question(String id, String uid, String profileImageUrl, String username, Double createdAt, String text, String qImageUrl, String chapterId, HashMap<String, Integer> answers, HashMap<String, Integer> likes, HashMap<String, Integer> followers) {
        this.id = id;
        this.uid = uid;
        this.profileImageUrl = profileImageUrl;
        this.username = username;
        this.createdAt = createdAt;
        this.text = text;
        this.qImageUrl = qImageUrl;
        this.chapterId = chapterId;
        this.answers = answers;
        this.likes = likes;
        this.followers = followers;
    }

    protected Question(Parcel in) {
        id = in.readString();
        uid = in.readString();
        profileImageUrl = in.readString();
        username = in.readString();
        if (in.readByte() == 0) {
            createdAt = null;
        } else {
            createdAt = in.readDouble();
        }
        text = in.readString();
        qImageUrl = in.readString();
        chapterId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(uid);
        dest.writeString(profileImageUrl);
        dest.writeString(username);
        if (createdAt == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(createdAt);
        }
        dest.writeString(text);
        dest.writeString(qImageUrl);
        dest.writeString(chapterId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel in) {
            return new Question(in);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Double getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Double createdAt) {
        this.createdAt = createdAt;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getqImageUrl() {
        return qImageUrl;
    }

    public void setqImageUrl(String qImageUrl) {
        this.qImageUrl = qImageUrl;
    }

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }

    public HashMap<String, Integer> getAnswers() {
        return answers;
    }

    public void setAnswers(HashMap<String, Integer> answers) {
        this.answers = answers;
    }

    public HashMap<String, Integer> getLikes() {
        return likes;
    }

    public void setLikes(HashMap<String, Integer> likes) {
        this.likes = likes;
    }

    public HashMap<String, Integer> getFollowers() {
        return followers;
    }

    public void setFollowers(HashMap<String, Integer> followers) {
        this.followers = followers;
    }
}
