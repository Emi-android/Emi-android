package com.proj.emi.biz;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class HtPrefs {

//    private static final String KEY_SHORTCUT = "key_shortcut";
//    private static final String KEY_GUIDE = "key_guide";

    private final static String USER_PROVINCE = "cur_prvc";
    private final static String USER_CITY = "cur_city";

    private final static String USER_UUID = "user_id";
    private final static String ACCESS_TOKEN = "access_token";
    private final static String USER_PWD = "user_pwd";
    private final static String USER_PHONE = "user_phone";
    private final static String USER_WEIGHT = "user_weight";
    private final static String USER_UNREAD = "unread_";

    // default
    private static SharedPreferences getDefault(final Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

//    public static boolean isShortCutCreated(final Context ctx) {
//        return getDefault(ctx).getBoolean(KEY_SHORTCUT, false);
//    }
//
//    public static void setGuideDone(final Context ctx) {
//        getDefault(ctx).edit().putBoolean(KEY_GUIDE, false).commit();
//    }
//
//    public static boolean isShowGuide(final Context ctx) {
//        return getDefault(ctx).getBoolean(KEY_GUIDE, false);
//    }

//    public static void setShortCutCreated(final Context ctx) {
//        getDefault(ctx).edit().putBoolean(KEY_SHORTCUT, true).commit();
//    }

    public static boolean getBoolValue(final Context ctx, final String key) {
        return getDefault(ctx).getBoolean(key, false);
    }

    public static void setBoolValue(final Context ctx, final String key, final boolean b) {
        getDefault(ctx).edit().putBoolean(key, b).commit();
    }

    public static void setUid(final Context ctx, final String uid) {
        getDefault(ctx).edit().putString(USER_UUID, uid).commit();
    }

    public static String getUid(final Context ctx) {
        return getDefault(ctx).getString(USER_UUID, null);
    }

    public static void setToken(final Context ctx, final String token) {
        getDefault(ctx).edit().putString(ACCESS_TOKEN, token).commit();
    }

    public static String getToken(final Context ctx) {
        return getDefault(ctx).getString(ACCESS_TOKEN, null);
    }

    public static void setPassword(final Context ctx, final String password) {
        getDefault(ctx).edit().putString(USER_PWD, password).commit();
    }

    public static String getPassword(final Context ctx) {
        return getDefault(ctx).getString(USER_PWD, null);
    }

    public static void setProvince(final Context ctx, final String prvc) {
        getDefault(ctx).edit().putString(USER_PROVINCE, prvc).commit();
    }

    public static String getProvince(final Context ctx) {
        return getDefault(ctx).getString(USER_PROVINCE, null);
    }

    public static void setCity(final Context ctx, final String city) {
        getDefault(ctx).edit().putString(USER_CITY, city).commit();
    }

    public static String getCity(final Context ctx) {
        return getDefault(ctx).getString(USER_CITY, null);
    }

    public static void setUnread(final Context ctx, final String uid, final int c) {
        getDefault(ctx).edit().putInt(USER_UNREAD + uid, c).commit();
    }

    public static int getUnread(final Context ctx, final String uid) {
        return getDefault(ctx).getInt(USER_UNREAD + uid, 0);
    }

    public static void clearLoginInfo(final Context ctx) {
        getDefault(ctx).edit().remove(USER_UUID).remove(USER_PWD).commit();
    }

    public static String getPhone(Context ctx) {
        return getDefault(ctx).getString(USER_PHONE, null);
    }

    public static void setPhone(Context ctx, String phone) {
        getDefault(ctx).edit().putString(USER_PHONE, phone).commit();
    }

    public static float getWeight(Context ctx) {
        return getDefault(ctx).getFloat(USER_WEIGHT, 0);
    }

    public static void setWeight(Context ctx, float w) {
        getDefault(ctx).edit().putFloat(USER_WEIGHT, w).commit();
    }

}
