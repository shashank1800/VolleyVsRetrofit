package com.shashankbhat.library;

import com.google.gson.JsonObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by SHASHANK BHAT on 12-Mar-20.
 * <p>
 * shashankbhat1800@gmail.com
 */
public interface API {

    @GET("weather")
    Call<ResponseBody> getStringResponse(@Query("q")String id, @Query("APPID") String APPID);

}
