package com.example.julionery.guia.Data;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionHandler {

    public static String PROPERTY_EMAIL = "EMAIL";
    public static String PROPERTY_TOKEN = "TOKEN";
    public static String GROUP_USER = "USER_APP";

    public static void saveEmail(String inputEmail, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(GROUP_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PROPERTY_EMAIL, inputEmail);
        editor.commit();
    }

    public static String getEmail(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(GROUP_USER, Context.MODE_PRIVATE);
        String email = sharedPreferences.getString(PROPERTY_EMAIL,"");
        return email;
    }
    public static void saveToken(String inputEmail, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(GROUP_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PROPERTY_TOKEN, inputEmail);
        editor.commit();
    }

    public static String getToken(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(GROUP_USER, Context.MODE_PRIVATE);
        String email = sharedPreferences.getString(PROPERTY_TOKEN,"");
        return email;
    }

}
