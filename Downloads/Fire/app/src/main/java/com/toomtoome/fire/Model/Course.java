package com.toomtoome.fire.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

/**
 * Created by moogunjung on 11/21/17.
 */

public class Course implements Parcelable {

    String id;
    String title;
    //temporary properties as data snapshot produces an error without instructor and id,
    // guess this has something to do with firebase node name instructor  Dec 22 2017
    String instructor;
    String instructorId;
    User user;

    String courseImageUrl;
    HashMap<String, Integer> chapters;

    public Course(String id, String title, String instructor, String instructorId, User user, String courseImageUrl, HashMap<String, Integer> chapters) {
        this.id = id;
        this.title = title;
        this.instructor = instructor;
        this.instructorId = instructorId;
        this.user = user;
        this.courseImageUrl = courseImageUrl;
        this.chapters = chapters;
    }

    public Course() {
    }

    protected Course(Parcel in) {
        id = in.readString();
        title = in.readString();
        instructor = in.readString();
        instructorId = in.readString();
        courseImageUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(instructor);
        dest.writeString(instructorId);
        dest.writeString(courseImageUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

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

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public String getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(String instructorId) {
        this.instructorId = instructorId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCourseImageUrl() {
        return courseImageUrl;
    }

    public void setCourseImageUrl(String courseImageUrl) {
        this.courseImageUrl = courseImageUrl;
    }

    public HashMap<String, Integer> getChapters() {
        return chapters;
    }

    public void setChapters(HashMap<String, Integer> chapters) {
        this.chapters = chapters;
    }
}
