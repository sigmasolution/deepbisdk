package com.pl.deepbisdk.network;

import com.google.gson.JsonObject;
import com.pl.deepbisdk.DeepBiManager;
import com.pl.deepbisdk.queuemanager.HitEvent;

import java.util.ArrayList;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UploadHitApi {
//    @FormUrlEncoded
    @POST("data/")
    Call<ResponseBody> uploadHit(
            @Query("getEventTime") String getEventTime,
            @Query("identifyBy") String identifyBy,
            @Body ArrayList<HitEvent> body
    );
}
