package com.clownqiang.mmlibrary.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.clownqiang.mmlibrary.MyApp;

/**
 * Created by clownqiang on 14/11/30.
 */
public class UserData {

    public static Context getAppContext() {
        return MyApp.getContext();
    }

    public static String getBookMessage() {
        SharedPreferences pref = getAppContext().getSharedPreferences(
                "user", 0);
        return pref.getString("books", "");
    }

    public static boolean saveBookMessage(String s) {
        SharedPreferences pref = getAppContext().getSharedPreferences(
                "user", 0);
        return pref.edit().putString("books", s).commit();
    }

    public static String getUserLoginName() {
        SharedPreferences pref = getAppContext().getSharedPreferences(
                "user", 0);
        return pref.getString("login", "");
    }

    public static boolean saveUserLoginName(String s) {
        SharedPreferences pref = getAppContext().getSharedPreferences(
                "user", 0);
        return pref.edit().putString("login", s).commit();
    }

    public static String getUserPassWord() {
        SharedPreferences pref = getAppContext().getSharedPreferences(
                "user", 0);
        return pref.getString("password", "");
    }

    public static boolean saveUserPassWord(String s) {
        SharedPreferences pref = getAppContext().getSharedPreferences(
                "user", 0);
        return pref.edit().putString("password", s).commit();
    }

    public static String getUserCookie() {
        SharedPreferences pref = getAppContext().getSharedPreferences(
                "user", 0);
        return pref.getString("cookie", "");
    }

    public static boolean saveUserCookie(String s) {
        SharedPreferences pref = getAppContext().getSharedPreferences(
                "user", 0);
        return pref.edit().putString("cookie", s).commit();
    }

    public static int getEarlyDay() {
        SharedPreferences pref = getAppContext().getSharedPreferences(
                "user", 0);
        return pref.getInt("day", 3);
    }

    public static boolean saveEarlyDay(int s) {
        SharedPreferences pref = getAppContext().getSharedPreferences(
                "user", 0);
        return pref.edit().putInt("day", s).commit();
    }
}
