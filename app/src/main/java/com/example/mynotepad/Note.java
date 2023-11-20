package com.example.mynotepad;

public class Note {
    private long id;
    private String content;
    private String timestamp;
    private String password;
    private boolean locked;
    private int imgResourceId;



    public Note(long id, String content, String timestamp, String password, boolean locked, int img) {
        this.id = id;
        this.content = content;
        this.timestamp = timestamp;
        this.password = password;
        this.locked = locked;
        this.imgResourceId = imgResourceId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getImgResourceId() {
        return imgResourceId;
    }

    public void setImgResourceId(int imgResourceId) {
        this.imgResourceId = imgResourceId;
    }
}