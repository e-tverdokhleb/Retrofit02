package com.example.hp.retrofit02;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TwitterService {
    @GET("repos/e-tverdokhleb/Retrofit02/contributors")
    Call<List<Contributor>> repoContributors();
}
