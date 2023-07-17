package com.example.travellerspoint.params;

public class Params {
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "Travellers_db";
    public static final String TABLE_NAME = "users";

    //keys of our table "users" in db
    public static final String KEY_ID = "ID";
    public static final String KEY_USERNAME = "User_Name";
    public static final String KEY_PASS = "User_Pass";
    public static final String KEY_FIRSTNAME = "User_FName";
    public static final String KEY_LASTNAME = "User_LName";
    public static final String KEY_PROFILE = "User_Profile";
    public static final String KEY_MAIL = "User_Mail";
    public static final String KEY_ADDRESS = "User_Address";
    public static final String KEY_RATINGS = "User_Rating";
    public static final String KEY_INTERESTS = "User_Interest";
    public static final String KEY_EMERGENCY = "User_Emergency";

    //Table name for Post
    public static final String TABLE_POST = "posts";

    //keys of table "Post" in dd
    public static final String KEY_IMAGE = "image_url";
    public static final String KEY_DESCRIPTION = "Post_Description";
    public static final String KEY_LIKES = "Post_Likes";
    public static final String KEY_AUTHER = "Post_Auther";
    public static final String KEY_COMMENT_COUNT = "Post_ComCount";
    public static final String KEY_DATE = "Post_Date";

    //Table name for Comment
    public static final String TABLE_COMMENT = "comments";

    //keys of table "Comment"
    public static final String KEY_POST_ID = "Post_Id";
    public static final String KEY_COM_DESCRIPTION = "Com_Description";
    public static final String KEY_COM_DATE = "Com_Date";
    public static final String KEY_COM_LIKES = "Com_Likes";

    //Table name for Plans
    public static final String TABLE_PLAN = "plans";

    //keys of table Plan
    public static final String KEY_PLAN_NAME = "plan_title";
    public static final String KEY_PLAN_SOURCE = "source";
    public static final String KEY_PLAN_DESTINATION = "destination";
    public static final String KEY_PLAN_DATE = "date";
    public static final String KEY_DEPARTURE = "departure";
    public static final String KEY_EXPECTED_TIME = "expected_time";
}
