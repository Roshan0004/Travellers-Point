package com.example.travellerspoint.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SharedPreference {
    public static void setDefaults(String key, String value, Context context) {
        SharedPreferences sp = context.getSharedPreferences("USER", 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void setDefaults(String key, Boolean value, Context context) {
        SharedPreferences sp = context.getSharedPreferences("LoginPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static String getDefaults(String key, Context context){
        SharedPreferences sp = context.getSharedPreferences("USER", 0);
        return sp.getString(key, null);
    }
}
