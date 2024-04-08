package com.example.assgment_ph43396_rest_api_and;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIServer {
    String DOMAIN = "http://192.168.1.11:3000";
    @GET("/api/list")
    Call<List<SanphamModel>> getSanphams();
    @Multipart
    @POST("/api/register")
    Call<UserModel> register(
            @Part("username") RequestBody username,
            @Part("password") RequestBody password,
            @Part("email") RequestBody email,
            @Part("name") RequestBody name,
            @Part MultipartBody.Part avatar
    );

    @POST("/api/login")
    Call<UserModel> login (@Body UserModel userModel);

    @Multipart
    @POST("/api/add")
    Call<SanphamModel> addSanpham(@PartMap Map<String, RequestBody> requestBodyMap,
                     @Part MultipartBody.Part imageSanpham);

    @DELETE("/api/delete/{id}")
    Call<Void> deleteSanpham(@Path("id") String id);

    @Multipart
    @PUT("/api/update/{id}")
    Call<SanphamModel> updateSanpham(@PartMap Map<String, RequestBody> requestBodyMap,
                        @Path("id") String id,
                        @Part MultipartBody.Part imageSanPham
    );

    @Multipart
    @PUT("/api/update-no-image/{id}")
    Call<SanphamModel> updateNoImage(@PartMap Map<String, RequestBody> requestBodyMap,
                            @Path("id") String id
    );

    @GET("/api/search")
    Call<List<SanphamModel>> searchSanPham(@Query("key") String query);
    @GET("/api/giam-dan")
    Call<List<SanphamModel>> getGiam();

    @GET("/api/tang-dan")
    Call<List<SanphamModel>> getTang();

}
