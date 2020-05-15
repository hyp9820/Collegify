package com.example.collegify;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*
 * Created by Hitanshu on 01-03-2019.
 */

public class ApiClient {

        //public  static  final String BASE_URL = "http://192.168.1.13/Collegify/";
        public  static  final String BASE_URL = "http://192.168.43.175/Collegify/";
        //public  static  final String BASE_URL = "http://10.0.2.2/Collegify/";
        public  static Retrofit retrofit = null;

        public static Retrofit getApiClient()
        {

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build();

            if (retrofit == null)
            {
                retrofit = new Retrofit.Builder()
                        .client(okHttpClient)
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create(gson)).build();
            }
            return retrofit;

        }
}
