package com.abilix.robot.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.robot.et.common.lib.http.cookie.CookieManger;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class BaseActivity extends Activity {

    private Context context;
    private OkHttpClient okHttpClient;
    public Retrofit retrofit;

    private static final String BASE_URL_TURING = "http://www.tuling123.com/";
    private static final String BASE_URL_SERVER = "http://10.107.2.137:8080/ApolloWebService/";
//    private static final String BASE_URL_SERVER = "http://10.107.203.162:8080/ApolloWebService/";
    private static final String BASE_URL_TURING_COMPANY = "http://www.tuling123.com/";
    private static final List<String> BASE_URL_LIST = new ArrayList<String>(){{
        add(BASE_URL_TURING);
        add(BASE_URL_SERVER);
        add(BASE_URL_TURING_COMPANY);
    }};

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        okHttpClient = new OkHttpClient.Builder()
//                .addInterceptor(new MulityBaseUrlInterceptor(BASE_URL_LIST))
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .cookieJar(new CookieManger(context))
                .build();
        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL_SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
