package org.md2k.mcerebrum.opioidlabstudy;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class MySharedPreference {
    private static final String MY_PREFS_NAME = "mydata";

    public static void set(Context context, String key, String value) {

        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void setMedications(Context context, ArrayList<String> value) {

        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putStringSet("MEDICATION", new HashSet<String>(value));
        editor.apply();
    }

    public static ArrayList<String> getMedications(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        HashSet<String> hashSet = new HashSet<>();
        Set<String> result = prefs.getStringSet("MEDICATION", hashSet);
        ArrayList<String> mainList = new ArrayList<String>();
        mainList.addAll(result);
        return mainList;
    }

    public static String get(Context context, String key) {
        SharedPreferences prefs = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        return prefs.getString(key, null);
    }

    public static void clear(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();

    }
}
