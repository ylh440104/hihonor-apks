package cn.richinfo.dm.util;

import android.content.Context;
import android.content.SharedPreferences;

/* loaded from: classes.dex */
public class SharedPreferencesUtils {
    private SharedPreferencesUtils() {
    }

    public static void clearSharedPreferences(Context context, String str, int i) {
        getSharedPreferences(context, str, i).edit().clear().commit();
    }

    public static void clearSharedPreferences(Context context, String str, int i, String str2) {
        getSharedPreferences(context, str, i).edit().remove(str2).commit();
    }

    public static SharedPreferences getSharedPreferences(Context context, String str, int i) {
        return context.getSharedPreferences(str, i);
    }

    public static float getValueInPrivateMode(Context context, String str, String str2, float f) {
        return getSharedPreferences(context, str, 0).getFloat(str2, f);
    }

    public static int getValueInPrivateMode(Context context, String str, String str2, int i) {
        return getSharedPreferences(context, str, 0).getInt(str2, i);
    }

    public static long getValueInPrivateMode(Context context, String str, String str2, long j) {
        return getSharedPreferences(context, str, 0).getLong(str2, j);
    }

    public static String getValueInPrivateMode(Context context, String str, String str2, String str3) {
        return getSharedPreferences(context, str, 0).getString(str2, str3);
    }

    public static boolean getValueInPrivateMode(Context context, String str, String str2, boolean z) {
        return getSharedPreferences(context, str, 0).getBoolean(str2, z);
    }

    public static void removeValueInPrivateMode(Context context, String str, String str2) {
        getSharedPreferences(context, str, 0).edit().remove(str2);
    }

    public static void setValueInPrivateMode(Context context, String str, float f, String str2) {
        getSharedPreferences(context, str2, 0).edit().putFloat(str, f).commit();
    }

    public static void setValueInPrivateMode(Context context, String str, int i, String str2) {
        getSharedPreferences(context, str2, 0).edit().putInt(str, i).commit();
    }

    public static void setValueInPrivateMode(Context context, String str, long j, String str2) {
        getSharedPreferences(context, str2, 0).edit().putLong(str, j).commit();
    }

    public static void setValueInPrivateMode(Context context, String str, String str2, String str3) {
        getSharedPreferences(context, str3, 0).edit().putString(str, str2).commit();
    }

    public static void setValueInPrivateMode(Context context, String str, boolean z, String str2) {
        getSharedPreferences(context, str2, 0).edit().putBoolean(str, z).commit();
    }
}
