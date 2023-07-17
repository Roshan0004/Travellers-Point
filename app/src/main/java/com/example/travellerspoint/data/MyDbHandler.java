package com.example.travellerspoint.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.travellerspoint.model.Comment;
import com.example.travellerspoint.model.Plan;
import com.example.travellerspoint.model.Post;
import com.example.travellerspoint.model.User;
import com.example.travellerspoint.params.Params;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyDbHandler extends SQLiteOpenHelper {
    public MyDbHandler(Context context) {
        super(context, Params.DB_NAME, null, Params.DB_VERSION);
    }

    private static final String CREATE_TABLE_USER = "CREATE TABLE " + Params.TABLE_NAME + "("
            + Params.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Params.KEY_USERNAME + " VARCHAR(25) UNIQUE NOT NULL, "
            + Params.KEY_PASS + " VARCHAR(30) NOT NULL, "
            + Params.KEY_FIRSTNAME + " VARCHAR(20), "
            + Params.KEY_LASTNAME + " VARCHAR(20), "
            + Params.KEY_MAIL + " VARCHAR(100) UNIQUE NOT NULL, "
            + Params.KEY_ADDRESS + " VARCHAR(250), "
            + Params.KEY_RATINGS + " VARCHAR(5), "
            + Params.KEY_INTERESTS + " VARCHAR(200), "
            + Params.KEY_EMERGENCY + " VARCHAR(12)" + ")";

    private static final String CREATE_TABLE_POST = "CREATE TABLE " + Params.TABLE_POST + "("
            + Params.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Params.KEY_USERNAME + " VARCHAR(25), "
            + Params.KEY_IMAGE + " BLOB, "
            + Params.KEY_LIKES + " INTEGER, "
            + Params.KEY_AUTHER + " VARCHAR(25), "
            + Params.KEY_DESCRIPTION + " VARCHAR(250), "
            + Params.KEY_COMMENT_COUNT + " INTEGER, "
            + Params.KEY_DATE + " TEXT NOT NULL, "
            + "FOREIGN KEY (" + Params.KEY_USERNAME + ") REFERENCES " + Params.TABLE_NAME + "(" + Params.KEY_USERNAME + ") ON DELETE CASCADE" + ")";

    private static final String CREATE_TABLE_COMMENT = "CREATE TABLE " + Params.TABLE_COMMENT + "("
            + Params.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Params.KEY_POST_ID + " INTEGER, "
            + Params.KEY_USERNAME + " VARCHAR(25), "
            + Params.KEY_COM_DESCRIPTION + " VARCHAR(200), "
            + Params.KEY_COM_DATE + " TEXT NOT NULL, "
            + Params.KEY_COM_LIKES + " INTEGER, "
            + "FOREIGN KEY (" + Params.KEY_POST_ID + ") REFERENCES " + Params.TABLE_POST + "(" + Params.KEY_ID + ") ON DELETE CASCADE" + ")";

    private static final String CREATE_TABLE_PLAN = "CREATE TABLE " + Params.TABLE_PLAN + "("
            + Params.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Params.KEY_PLAN_NAME + " VARCHAR(25), "
            + Params.KEY_PLAN_SOURCE + " VARCHAR(25), "
            + Params.KEY_PLAN_DESTINATION + " VARCHAR(25), "
            + Params.KEY_PLAN_DATE + " VARCHAR(25), "
            + Params.KEY_DEPARTURE + " VARCHAR(25), "
            + Params.KEY_EXPECTED_TIME + " VARCHAR(25)" + ")";

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Log.d("RoshanDB", "Creating Query String..");
            Log.d("RoshanDB", "Query being run is: " + CREATE_TABLE_USER);
            db.execSQL(CREATE_TABLE_USER);
        } catch (Exception ex){
            Log.e("RoshanDB", "Error: " + ex.toString(), null);
        }

        try {
            Log.d("RoshanDB", "Creating Query String..");
            Log.d("RoshanDB", "Query being run is: " + CREATE_TABLE_POST);
            db.execSQL(CREATE_TABLE_POST);
        } catch (Exception ex){
            Log.e("RoshanDB", "Error: " + ex.toString(), null);
        }

        try {
            Log.d("RoshanDB", "Creating Query String..");
            Log.d("RoshanDB", "Query being run is: " + CREATE_TABLE_COMMENT);
            db.execSQL(CREATE_TABLE_COMMENT);
        } catch (Exception ex){
            Log.e("RoshanDB", "Error: " + ex.toString(), null);
        }

        try {
            Log.d("RoshanDB", "Creating Query String..");
            Log.d("RoshanDB", "Query being run is: " + CREATE_TABLE_PLAN);
            db.execSQL(CREATE_TABLE_PLAN);
        } catch (Exception ex){
            Log.e("RoshanDB", "Error: " + ex.toString(), null);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS '" + Params.TABLE_NAME + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + Params.TABLE_POST + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + Params.TABLE_COMMENT + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + Params.TABLE_PLAN + "'");
        onCreate(db);
    }
//
//    public boolean addUser(User user){
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        Log.d("RoshanDB", "Putting Key Values...");
//        ContentValues values = new ContentValues();
//        values.put(Params.KEY_USERNAME, user.getUser_name());
//        values.put(Params.KEY_PASS, user.getPassword());
//        values.put(Params.KEY_MAIL, user.getMail_id());
//
//        long result = db.insert(Params.TABLE_NAME, null, values);
//
//        if (result == -1) {
//            db.close();
//            return false;
//        }
//        else {
//            Log.d("RoshanDB", "Successfully inserted!..");
//            db.close();
//            return true;
//        }
//    }
//
//    public boolean checkUser(User user){
//        SQLiteDatabase db = this.getReadableDatabase();
//        String check = "SELECT * FROM " + Params.TABLE_NAME + " WHERE " +
//                Params.KEY_USERNAME + " = ? OR " + Params.KEY_MAIL + " = ?";
//        Cursor cursor = db.rawQuery(check, new String[]{user.getUser_name(), user.getUser_name()});
//        Log.d("RoshanDB", "Data Inserted Successfully!");
//        if (cursor.getCount() > 0) {
//            Log.d("RoshanDB", "Exit Check User..true");
//            return true;
//        }
//        else {
//            Log.d("RoshanDB", "Exit Check User..false");
//            return false;
//        }
//    }

//    public boolean checkUserPass(User user){
//        SQLiteDatabase db = getWritableDatabase();
//        String check = "SELECT * FROM " +
//                Params.TABLE_NAME + " WHERE " +
//                Params.KEY_USERNAME + " = ? OR " +
//                Params.KEY_MAIL + " = ? AND " + Params.KEY_PASS + " = ?";
//        Cursor cursor = db.rawQuery(check,new String[] {user.getUser_name(), user.getMail_id(), user.getPassword()});
//        if (cursor.getCount() > 0)
//            return true;
//        else
//            return false;
//    }
//
//    public String getUsername(String username){
//        String name = "";
//        SQLiteDatabase db = getReadableDatabase();
//        Log.d("roshanTest", "Getting Username..");
//        String select = "SELECT " + Params.KEY_USERNAME + " FROM " +
//                Params.TABLE_NAME + " WHERE " +
//                Params.KEY_USERNAME + " = " + '"' + username + '"' + " OR " +
//                Params.KEY_MAIL + " = " + '"' + username + '"' + ";";
//        Cursor cursor = db.rawQuery(select, null);
//        Log.d("roshanTest", select);
//        if (cursor.moveToFirst()) {
//            name = cursor.getString(0);
//        }
//        cursor.close();
//        Log.d("roshanTest", "Name = "+name);
//        return name;
//    }
//
//    public Map<String, String> getUserDetails(String username){
//        Map<String, String> userDetails = new HashMap<>();
//        SQLiteDatabase db = this.getReadableDatabase();
//        Log.d("roshanTest", "Getting Username & Mail..");
//        String select = "SELECT " + Params.KEY_USERNAME + "," + Params.KEY_MAIL + " FROM " +
//                Params.TABLE_NAME + " WHERE " +
//                Params.KEY_USERNAME + " = " + '"' + username + '"' + " OR " +
//                Params.KEY_MAIL + " = " + '"' + username + '"' + ";";
//        Cursor cursor = db.rawQuery(select, null);
//        Log.d("roshanTest", select);
//        if (cursor.moveToNext()){
//            userDetails.put("UserName", cursor.getString(0));
//            userDetails.put("Mail", cursor.getString(1));
//        }
//        Log.d("roshanTest", "Name = " + userDetails.get("UserName") +
//                "\nEmail = " + userDetails.get("Mail"));
//        cursor.close();
//        return userDetails;
//    }

    public boolean updatePass(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("RoshanDB", "Putting Key Values for updation...");

        ContentValues values = new ContentValues();
        values.put(Params.KEY_PASS, user.getPassword());
        long result = db.update(Params.TABLE_NAME, values,
                Params.KEY_USERNAME + " = ? OR " +
                        Params.KEY_MAIL + " = ?", new String[]{user.getUser_name(), user.getUser_name()});

        if (result == -1) {
            db.close();
            return false;
        }
        else {
            Log.d("RoshanDB", "Successfully updated!..");
            db.close();
            return true;
        }
    }


    //Post handler
    public boolean addPost(Post post){
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("RoshanDB", "Putting Key Values...");

        ContentValues postValues = new ContentValues();

        postValues.put(Params.KEY_USERNAME, post.getUser_name());
        postValues.put(Params.KEY_IMAGE, post.getImageUrl());
        postValues.put(Params.KEY_LIKES, post.getLikes());
        postValues.put(Params.KEY_AUTHER, post.getAuthor());
        postValues.put(Params.KEY_DESCRIPTION, post.getDescription());
        postValues.put(Params.KEY_COMMENT_COUNT, post.getCom_count());
        postValues.put(Params.KEY_DATE, post.getDate());

        long result = db.insert(Params.TABLE_POST, null, postValues);

        if (result == -1){
            db.close();
            return  false;
        } else {
            db.close();
            return true;
        }
    }

    public List<Post> getAllPosts() {
        List<Post> postList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        //generate query to red data from db
        String select = "SELECT * FROM " + Params.TABLE_POST;
        Cursor cursor = db.rawQuery(select, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    Post post = new Post();
                    post.setUser_name(cursor.getString(1));
                    post.setImageUrl(cursor.getBlob(2));
                    post.setLikes(cursor.getString(3));
                    post.setAuthor(cursor.getString(4));
                    post.setDescription(cursor.getString(5));
                    post.setCom_count(cursor.getString(6));
                    post.setDate(cursor.getString(7));
                    postList.add(0, post);
                } while (cursor.moveToNext());
            }
        } catch (Exception e){
            Log.e("roshanTest", "getAllPosts: " + e.getMessage());
        }
        return postList;
    }

    public int getNoOfLikes(int position){
        int noOfLikes = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String select = "SELECT " + Params.KEY_LIKES + " FROM " + Params.TABLE_POST
                + " WHERE " + Params.KEY_ID + " = ? ";

        try {
            Cursor cursor = db.rawQuery(select, new String[]{String.valueOf(position)});

            if (cursor.moveToFirst())
                noOfLikes = cursor.getInt(0);
            else
                noOfLikes = 0;
            cursor.close();
            db.close();
        } catch (Exception e){
            Log.e("roshanTest", "onClick: "+e.toString());
        }
        return noOfLikes;
    }

    public void updateLikes(int like_count, int position){
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            int noOfLikes = like_count;
            ContentValues values = new ContentValues();
            values.put(Params.KEY_LIKES, like_count);
            db.update(Params.TABLE_POST, values, Params.KEY_ID + " =? ", new String[]{String.valueOf(position)});
            Log.d("roshanTest", "updateLikes: Like updated successfully");
            values.clear();
            db.close();
        } catch (Exception e){
            Log.e("roshanTest", "onUpdateLikes: "+e.toString());
        }
    }

    public Boolean addCommment(Comment comment) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("RoshanDB", "Putting Key Values...");
        ContentValues comValue = new ContentValues();

        comValue.put(Params.KEY_POST_ID, comment.getPost_id());
        comValue.put(Params.KEY_USERNAME, comment.getUser_name());
        comValue.put(Params.KEY_COM_DESCRIPTION, comment.getDescription());
        comValue.put(Params.KEY_COM_DATE, comment.getDate());

        long result = db.insert(Params.TABLE_COMMENT, null, comValue);

        Log.d("roshanDB", "addCommment: Comment added successfully..");

        if (result == -1) {
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }
    }

    public List<Comment> getAllComments(int position){
        List<Comment> commentList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        //generate query to red data from db
        String select = "SELECT * FROM " + Params.TABLE_COMMENT + " WHERE " + Params.KEY_POST_ID + " = ? ";
        Cursor cursor = db.rawQuery(select, new String[]{String.valueOf(position)});

        try {
            if (cursor.moveToFirst()) {
                do {
                    Comment comment = new Comment();
                    comment.setPost_id(cursor.getInt(1));
                    comment.setUser_name(cursor.getString(2));
                    comment.setDescription(cursor.getString(3));
                    comment.setDate(cursor.getString(4));
                    comment.setLike_count(cursor.getInt(5));
                    commentList.add(0, comment);
                } while (cursor.moveToNext());
            }
        } catch (Exception e){
            Log.e("roshanDB", "getAllComments: " + e.getMessage());
        }
        return commentList;
    }

    public Boolean addPlan(Plan plan){
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("RoshanDB", "Putting Key Values...");
        ContentValues planValue = new ContentValues();

        planValue.put(Params.KEY_PLAN_NAME, plan.getPlan_name());
        planValue.put(Params.KEY_PLAN_SOURCE, plan.getSource_location());
        planValue.put(Params.KEY_PLAN_DESTINATION, plan.getDestination_location());
        planValue.put(Params.KEY_PLAN_DATE, plan.getPlan_date());
        planValue.put(Params.KEY_DEPARTURE, plan.getDeparture());
        planValue.put(Params.KEY_EXPECTED_TIME, plan.getExpected_time());

        long result = db.insert(Params.TABLE_PLAN, null, planValue);

        Log.d("roshanDB", "addPlan: Plan added successfully..");
        if (result == -1) {
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }
    }

    public List<Plan> getAllPlans() {
        List<Plan> planList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        //generate query to red data from db
        String select = "SELECT * FROM " + Params.TABLE_PLAN;
        Cursor cursor = db.rawQuery(select, null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    Plan plan = new Plan();
                    plan.setPlan_name(cursor.getString(1));
                    plan.setSource_location(cursor.getString(2));
                    plan.setDestination_location(cursor.getString(3));
                    plan.setPlan_date(cursor.getString(4));
                    plan.setDeparture(cursor.getString(5));
                    plan.setExpected_time(cursor.getString(6));
                    planList.add(0, plan);
                } while (cursor.moveToNext());
            }
        } catch (Exception e){
            Log.e("roshanTest", "getAllPlans: " + e.getMessage());
        }
        return planList;
    }
}
