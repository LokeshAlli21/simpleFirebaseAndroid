package com.example.goodlifegym;

public class UserDataModel {
    String name,work,age,docId,currentUserID,photoUrl,currentDateAndTimeInStr,date,time;

    public UserDataModel(String name, String work, String age, String docId, String currentUserID, String photoUrl, String currentDateAndTimeInStr, String date, String time) {
        this.name = name;
        this.work = work;
        this.age = age;
        this.docId = docId;
        this.currentUserID = currentUserID;
        this.photoUrl = photoUrl;
        this.currentDateAndTimeInStr = currentDateAndTimeInStr;
        this.date = date;
        this.time = time;
    }

    public UserDataModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getCurrentUserID() {
        return currentUserID;
    }

    public void setCurrentUserID(String currentUserID) {
        this.currentUserID = currentUserID;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getCurrentDateAndTimeInStr() {
        return currentDateAndTimeInStr;
    }

    public void setCurrentDateAndTimeInStr(String currentDateAndTimeInStr) {
        this.currentDateAndTimeInStr = currentDateAndTimeInStr;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
