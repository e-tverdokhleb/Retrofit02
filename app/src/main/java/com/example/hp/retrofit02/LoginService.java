package com.example.hp.retrofit02;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginService {
    @FormUrlEncoded
    @POST("/token")
    Call<ServiceGenerator.AccessToken> getAccessToken(
        @Field("code") String code,
        @Field("grant_type") String grantType);

    @FormUrlEncoded
    @POST("token")
    Call<Object> getToken(@Field("grant_type") String grantType);
}
