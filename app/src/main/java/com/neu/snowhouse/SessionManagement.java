package com.neu.snowhouse;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManagement {
    public static final String MyPREFERENCES = "MyPrefs";

    private static SharedPreferences init(Context context) {
        return context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }

    public static void addUserName(Context context, String userName) {
        SharedPreferences.Editor editor = init(context).edit();
        editor.putString("userName", userName);
        editor.apply();
    }

    public static String getUserName(Context context) {
        return init(context).getString("userName", "");
    }

    public static void removeUserName(Context context) {
        SharedPreferences.Editor editor = init(context).edit();
        editor.remove("userName");
        editor.apply();
    }
}
