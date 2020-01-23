package com.example.projet_android_lp.API;

import androidx.annotation.Nullable;

import com.example.projet_android_lp.JsonSchema.Track;

import java.lang.ref.WeakReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LampApiCalls {
    public interface Callbacks {
        void onResponse(@Nullable Track track);
        void onFailure();
    }

    public static void fetchWeather(Callbacks callbacks, String track, String key) {
        final WeakReference<Callbacks> callbacksWeakReference = new WeakReference<Callbacks>(callbacks);

        LampApiServices lampApiServices = LampApiServices.RETROFIT.create(LampApiServices.class);
        Call<Track> call = lampApiServices.getTrack(track, key);
        call.enqueue(new Callback<Track>() {
            @Override
            public void onResponse(Call<Track> call, Response<Track> response) {
                if (callbacksWeakReference.get() != null) {
                    callbacksWeakReference.get().onResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<Track> call, Throwable t) {

            }
        });
    }

}
