package com.ninjacart.Retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {

    public static String token = null;
    private static Retrofit retrofit = null;

    public static ApiInterface getClient() {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        if (retrofit == null) {

            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(1, TimeUnit.MINUTES)
                    .addInterceptor(logging)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl("http://graminvikreta.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    .build();

        }

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        return apiInterface;

    }
}
