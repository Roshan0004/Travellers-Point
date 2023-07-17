package com.example.travellerspoint.model;

public class Comment {
    private int id;
    private int post_id;
    private String user_name;
    private String description;
    private String date;
    private int like_count;

    public Comment(){
    }

    public Comment(int id) {
        this.id = id;
    }

    public Comment(int post_id, String user_name, String description, String date) {
        this.post_id = post_id;
        this.user_name = user_name;
        this.description = description;
        this.date = date;
    }

    public Comment(String description) {
        this.description = description;
    }

    public Comment(int id, int post_id, String user_name, String description, String date, int like_count) {
        this.id = id;
        this.post_id = post_id;
        this.user_name = user_name;
        this.description = description;
        this.date = date;
        this.like_count = like_count;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }
}
