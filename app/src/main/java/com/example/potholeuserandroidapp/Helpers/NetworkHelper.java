package com.example.potholeuserandroidapp.Helpers;

import android.content.Context;

import com.example.potholeuserandroidapp.R;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkHelper {



    public static Retrofit getRetrofitInstance(Context context){


        ClearableCookieJar clearableCookieJar = new PersistentCookieJar(new SetCookieCache(),new SharedPrefsCookiePersistor(context));

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                                            .cookieJar(clearableCookieJar)
                                            .build();


        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(context.getString(R.string.BASE_URL))
                                .client(okHttpClient)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();


        return retrofit;
    }



}
