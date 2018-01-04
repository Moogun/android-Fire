package com.toomtoome.fire.Model;

/**
 * Created by moogunjung on 12/8/17.
 */

public class Announcement {

    String profileImageUrl;
    String username;
    String anImgUrl;
    String title;

    public Announcement(String profileImageUrl, String username, String anImgUrl, String title) {
        this.profileImageUrl = profileImageUrl;
        this.username = username;
        this.anImgUrl = anImgUrl;
        this.title = title;
    }

    public Announcement() {
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

    public String getAnImgUrl() {
        return anImgUrl;
    }

    public void setAnImgUrl(String anImgUrl) {
        this.anImgUrl = anImgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
