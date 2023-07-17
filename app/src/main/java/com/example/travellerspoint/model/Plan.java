package com.example.travellerspoint.model;

public class Plan {
    private int id;
    private String plan_name;
    private String source_location;
    private String destination_location;
    private String plan_date;
    private String departure;
    private String expected_time;
    
    public Plan(){}

    public Plan(int id) {
        this.id = id;
    }

    public Plan(String plan_name, String source_location, String destination_location, String plan_date) {
        this.plan_name = plan_name;
        this.source_location = source_location;
        this.destination_location = destination_location;
        this.plan_date = plan_date;
    }

    public Plan(String source_location, String destination_location, String plan_date, String departure, String expected_time) {
        this.source_location = source_location;
        this.destination_location = destination_location;
        this.plan_date = plan_date;
        this.departure = departure;
        this.expected_time = expected_time;
    }

    public Plan(String plan_name, String source_location, String destination_location, String plan_date, String departure, String expected_time) {
        this.plan_name = plan_name;
        this.source_location = source_location;
        this.destination_location = destination_location;
        this.plan_date = plan_date;
        this.departure = departure;
        this.expected_time = expected_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlan_name() {
        return plan_name;
    }

    public void setPlan_name(String plan_name) {
        this.plan_name = plan_name;
    }

    public String getSource_location() {
        return source_location;
    }

    public void setSource_location(String source_location) {
        this.source_location = source_location;
    }

    public String getDestination_location() {
        return destination_location;
    }

    public void setDestination_location(String destination_location) {
        this.destination_location = destination_location;
    }

    public String getPlan_date() {
        return plan_date;
    }

    public void setPlan_date(String plan_date) {
        this.plan_date = plan_date;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getExpected_time() {
        return expected_time;
    }

    public void setExpected_time(String expected_time) {
        this.expected_time = expected_time;
    }
}
