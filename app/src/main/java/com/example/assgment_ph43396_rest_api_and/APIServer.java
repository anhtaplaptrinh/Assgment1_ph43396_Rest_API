package com.example.assgment_ph43396_rest_api_and;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIServer {
    String DOMAIN = "http://192.168.1.10:3000";
    @GET("/api/list")
    Call<List<SanphamModel>> getSanphams();
    @POST("/api/add")
    Call<Void> addSanpham(@Body SanphamModel sanphamModel);

    @DELETE("/api/delete/{id}")
    Call<Void> deleteSanpham(@Path("id") String id);

    @PUT("/api/update/{id}")
    Call<Void> updateSanpham(
            @Path("id") String id,
            @Body SanphamModel sanphamModel
    );
    @GET("/api/search")
    Call<List<SanphamModel>> searchSanphamModel(@Query("q") String query);
}
