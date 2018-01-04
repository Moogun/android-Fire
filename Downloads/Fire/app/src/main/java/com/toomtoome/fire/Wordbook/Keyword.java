package com.toomtoome.fire.Wordbook;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by moogunjung on 12/13/17.
 */

public class Keyword implements Parcelable {

    String id;
    String keyword;
    String pronunciation;
    String meaning;
    String eg_1;
    String eg_Pronun_1;
    String eg_meaning_1;
    String eg_2;
    String eg_Pronun_2;
    String eg_meaning_2;
    String chapterId;

    public Keyword(String id, String keyword, String pronunciation, String meaning, String eg_1, String eg_Pronun_1, String eg_meaning_1, String eg_2, String eg_Pronun_2, String eg_meaning_2, String chapterId) {
        this.id = id;
        this.keyword = keyword;
        this.pronunciation = pronunciation;
        this.meaning = meaning;
        this.eg_1 = eg_1;
        this.eg_Pronun_1 = eg_Pronun_1;
        this.eg_meaning_1 = eg_meaning_1;
        this.eg_2 = eg_2;
        this.eg_Pronun_2 = eg_Pronun_2;
        this.eg_meaning_2 = eg_meaning_2;
        this.chapterId = chapterId;
    }

    public Keyword() {
    }

    protected Keyword(Parcel in) {
        id = in.readString();
        keyword = in.readString();
        pronunciation = in.readString();
        meaning = in.readString();
        eg_1 = in.readString();
        eg_Pronun_1 = in.readString();
        eg_meaning_1 = in.readString();
        eg_2 = in.readString();
        eg_Pronun_2 = in.readString();
        eg_meaning_2 = in.readString();
        chapterId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(keyword);
        dest.writeString(pronunciation);
        dest.writeString(meaning);
        dest.writeString(eg_1);
        dest.writeString(eg_Pronun_1);
        dest.writeString(eg_meaning_1);
        dest.writeString(eg_2);
        dest.writeString(eg_Pronun_2);
        dest.writeString(eg_meaning_2);
        dest.writeString(chapterId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Keyword> CREATOR = new Creator<Keyword>() {
        @Override
        public Keyword createFromParcel(Parcel in) {
            return new Keyword(in);
        }

        @Override
        public Keyword[] newArray(int size) {
            return new Keyword[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getEg_1() {
        return eg_1;
    }

    public void setEg_1(String eg_1) {
        this.eg_1 = eg_1;
    }

    public String getEg_Pronun_1() {
        return eg_Pronun_1;
    }

    public void setEg_Pronun_1(String eg_Pronun_1) {
        this.eg_Pronun_1 = eg_Pronun_1;
    }

    public String getEg_meaning_1() {
        return eg_meaning_1;
    }

    public void setEg_meaning_1(String eg_meaning_1) {
        this.eg_meaning_1 = eg_meaning_1;
    }

    public String getEg_2() {
        return eg_2;
    }

    public void setEg_2(String eg_2) {
        this.eg_2 = eg_2;
    }

    public String getEg_Pronun_2() {
        return eg_Pronun_2;
    }

    public void setEg_Pronun_2(String eg_Pronun_2) {
        this.eg_Pronun_2 = eg_Pronun_2;
    }

    public String getEg_meaning_2() {
        return eg_meaning_2;
    }

    public void setEg_meaning_2(String eg_meaning_2) {
        this.eg_meaning_2 = eg_meaning_2;
    }

    public String getChapterId() {
        return chapterId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }
}
