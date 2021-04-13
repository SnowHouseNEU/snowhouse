package com.neu.snowhouse.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.Executors;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    // local config
    private static final String BASE_URL = "http://10.0.2.2:8080/";
    // aws ec2
//    private static final String BASE_URL = "http://ec2-52-14-58-13.us-east-2.compute.amazonaws.com:8080/";
    private static RetrofitClient mInstance;
    private final Retrofit retrofit;
    Gson gson = new GsonBuilder().create();

    private RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static synchronized RetrofitClient getInstance() {
        if (mInstance == null) {
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    public API getAPI() {
        return retrofit.create(API.class);
    }
}
