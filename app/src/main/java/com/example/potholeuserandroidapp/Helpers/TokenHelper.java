package com.example.potholeuserandroidapp.Helpers;

import android.content.Context;
import android.content.SharedPreferences;

public class TokenHelper {

    private static final String SHARED_PREFERENCE = "PREFERENCES";

    public static String getRefreshToken(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE,Context.MODE_PRIVATE);
        return sharedPreferences.getString("refreshToken",null);

    }

    public static void putRefreshToken(Context context , String refreshToken){

        SharedPreferences.Editor editor = context.getSharedPreferences(SHARED_PREFERENCE,Context.MODE_PRIVATE).edit();
        editor.putString("refershToken",refreshToken);
        editor.apply();

    }


}
