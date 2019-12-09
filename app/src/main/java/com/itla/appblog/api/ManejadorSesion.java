package com.itla.appblog.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class ManejadorSesion {
    private SharedPreferences prefs;
    private static int userID;
    private static String token;

    public static int getUserID() {
        return userID;
    }

    private static String token_normal;

    public static String getToken_normal() {
        return token_normal;
    }

    public static void setToken_normal(String token_normal) {
        ManejadorSesion.token_normal = token_normal;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public ManejadorSesion(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setSesion(String key, String value) {
        prefs.edit().putString(key, value).commit();
    }

    public String getSesion(String key) {
        String value = prefs.getString(key, "");
        return value;
    }

    public void removerSession(String key) {
        prefs.edit().remove(key).apply();
    }

    public static String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
