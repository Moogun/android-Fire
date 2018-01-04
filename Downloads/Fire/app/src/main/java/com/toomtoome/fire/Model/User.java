package com.toomtoome.fire.Model;

/**
 * Created by moogunjung on 11/24/17.
 */

public class User {

    private String email;
    private String profileImageUrl;
    private String username;

    public User(String email, String profileImageUrl, String username) {
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.username = username;
    }

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    @Override
    public String toString() {
        return "User{" +
                ", email='" + email + '\'' +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                ", username='" + username + '\'' +
                '}';
    }
}
