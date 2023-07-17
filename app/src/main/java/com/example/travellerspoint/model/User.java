package com.example.travellerspoint.model;

public class User {
    private int id;
    private String user_name;
    private String password;
    private String first_name;
    private String last_name;
    private String mail_id;
    private String user_address;
    private String ratings;
    private String interests;
    private String emergency_number;

    public User( String user_name, String password, String first_name, String last_name, String mail_id, String user_address, String ratings, String interests, String emergency){
        this.user_name = user_name;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.mail_id = mail_id;
        this.user_address = user_address;
        this.ratings = ratings;
        this.interests = interests;
        this.emergency_number = emergency;
    }

    public User(int id, String first_name, String password, String last_name, String user_name, String mail_id, String user_address, String ratings, String interests, String emergency_number){
        this.id = id;
        this.user_name = user_name;
        this.password = password;
        this.first_name = first_name;
        this.last_name = last_name;
        this.mail_id = mail_id;
        this.user_address = user_address;
        this.ratings = ratings;
        this.interests = interests;
        this.emergency_number = emergency_number;
    }

    public User(String user_name, String password, String mail_id){
        this.user_name = user_name;
        this.password = password;
        this.mail_id = mail_id;
    }

    public User(String user_name, String password){
        this.user_name = user_name;
        this.password = password;
    }

    public User(){
    }


    //Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setMail_id(String mail_id) {
        this.mail_id = mail_id;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public void setRatings(String ratings) {
        this.ratings = ratings;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public void setEmergency_number(String emergency_number) {
        this.emergency_number = emergency_number;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    // Getters

    public int getId() {
        return id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getMail_id() {
        return mail_id;
    }

    public String getUser_address() {
        return user_address;
    }

    public String getRatings() {
        return ratings;
    }

    public String getInterests() {
        return interests;
    }

    public String getEmergency_number() {
        return emergency_number;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getPassword() {
        return password;
    }
}
