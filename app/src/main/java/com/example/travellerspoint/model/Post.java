package com.example.travellerspoint.model;

public class Post {
    private int id;
    private byte[] profileUrl;
    private String user_name;
    private byte[] imageUrl;
    private String likes;
    private String author;
    private String description;
    private String com_count;
    private String comments;
    private String date;

    public Post(){
    }

    public Post(byte[] profileUrl, String user_name, String likes){
        this.profileUrl = profileUrl;
        this.user_name = user_name;
        this.likes = likes;
    }

    public Post(byte[] profileUrl, String user_name, byte[] imageUrl, String likes, String author, String description, String com_count) {
        this.profileUrl = profileUrl;
        this.user_name = user_name;
        this.imageUrl = imageUrl;
        this.likes = likes;
        this.author = author;
        this.description = description;
        this.com_count = com_count;
    }

    public Post(int id, byte[] profileUrl, String user_name, byte[] imageUrl, String likes, String author, String description, String com_count, String comments, String date) {
        this.id = id;
        this.profileUrl = profileUrl;
        this.user_name = user_name;
        this.imageUrl = imageUrl;
        this.likes = likes;
        this.author = author;
        this.description = description;
        this.com_count = com_count;
        this.comments = comments;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(byte[] profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public byte[] getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(byte[] imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCom_count() {
        return com_count;
    }

    public void setCom_count(String com_count) {
        this.com_count = com_count;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
