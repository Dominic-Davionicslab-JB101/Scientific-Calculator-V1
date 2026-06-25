package com.oyekaleblessing.scientificcalculator;

import android.content.Context;
import android.content.SharedPreferences;

public class themehelper {

    private static final String PREFS_NAME = "AppSettings";
    private static final String KEY_THEME = "dark_theme_enabled";

    public static boolean isDarkMode(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_THEME, true); // dark by default
    }

    public static void setDarkMode(Context context, boolean dark) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(KEY_THEME, dark).apply();
    }

    public static int getRootBackgroundColor(Context context) {
        return isDarkMode(context) ? 0xFF000000 : 0xFFF2F2F7;
    }

    public static int getPrimaryTextColor(Context context) {
        return isDarkMode(context) ? 0xFFFFFFFF : 0xFF000000;
    }

    public static int getSecondaryTextColor(Context context) {
        return isDarkMode(context) ? 0xFF888888 : 0xFF6E6E73;
    }
}
