package com.example.projet_android_lp.API;

import com.example.projet_android_lp.JsonSchema.Track;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LampApiServices {
    public static final Retrofit RETROFIT = new Retrofit.Builder()
            .baseUrl("http://ws.audioscrobbler.com/2.0/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @GET("weather?units=metric")
    Call<Track> getTrack(@Query("track")String track, @Query("api_key")String key);
}
