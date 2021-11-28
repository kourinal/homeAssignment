package com.example.homeassignment.data.api;

import com.example.homeassignment.data.vo.MovieDetails;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import io.reactivex.Single;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDbClient{

    final static String API_KEY = "2c46288716a18fb7aadcc2a801f3fc6b";
    final static String BASE_URL = "https://api.themoviedb.org/3/";

    final static String POSTER_BASE_URL = "https://image.tmdb.org/t/p/w342";

    public static String getApiKey() {
        return API_KEY;
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static String getPosterBaseUrl() {
        return POSTER_BASE_URL;
    }

    public static MovieDbInterface getClient(int id) {
        Interceptor requestInterceptor = new Interceptor(){
            @Override
            public Response intercept(Chain chain) throws IOException {
                HttpUrl url = chain.request().url().newBuilder().addQueryParameter("api_key", API_KEY).build();
                Request request = chain.request().newBuilder().url(url).build();
                return chain.proceed(request);
            }
        };
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(requestInterceptor).connectTimeout(60, TimeUnit.SECONDS).build();

        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(MovieDbInterface.class);
    }
}
