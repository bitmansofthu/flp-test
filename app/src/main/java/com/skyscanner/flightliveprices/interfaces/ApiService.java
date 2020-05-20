package com.skyscanner.flightliveprices.interfaces;

import com.skyscanner.flightliveprices.model.ApiResponse;

import java.util.Map;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiService {
    @FormUrlEncoded
    @POST("pricing/v1.0/")
    Single<Response<Object>> getSessionKey(@FieldMap Map<String, String> fields);

    @GET
    @Headers("Accept: application/json")
    Call<ResponseBody> pollResults(@Url String url, @Query("apiKey") String apikey, @Query("pageIndex") int pageIndex, @Query("pageSize") int pageSize);

}
