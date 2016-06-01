package com.example.hp.retrofit02;

import android.util.Base64;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ServiceGenerator {
    public static final String API_BASE_URL = "https://api.twitter.com/1.1/";

    private static OkHttpClient.Builder httpClient = new OkHttpClient().newBuilder();

    private static Retrofit.Builder builder = new Retrofit.Builder()
                                                    .baseUrl(API_BASE_URL)
                                                    .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass){
        return createService(serviceClass, null);
    }


    public static <S> S createService(Class<S> serviceClass, String username, String password){
        if (username != null && password !=null){
            String credentials = username + ":" + password;
            final String basic =
                    "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();

                    Request.Builder requestBuilder = original.newBuilder()
                                                        .header("Authorization", basic)
                                                        .header("Accept", "application/json")
                                                        .method(original.method(), original.body());

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
        }

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
    }



    public static <S> S createService(Class<S> serviceClass, final AccessToken token){  //среда предложила сделать toke = final чем это обусловлено?
       if (token != null) {
           httpClient.addInterceptor(new Interceptor(){

               @Override
               public Response intercept(Chain chain) throws IOException {
                   Request original = chain.request();
                   Request.Builder requestBuilder = original.newBuilder()
                                                        .header("Accept", "application/json")
                                                        .header("Authorization", token.getTokenType() + " " + token.getAccessToken())
                                                        .method(original.method(), original.body());
                        Request request = requestBuilder.build();
                        return chain.proceed(request);
               }
           });
       }

        OkHttpClient client = httpClient.build();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serviceClass);
        }


    public class AccessToken{
        private String accessToken;
        private String tokenType;

        public String getAccessToken(){
            return accessToken;
        }

        public String getTokenType() {
            if (!Character.isUpperCase(tokenType.charAt(0))){
                tokenType = Character.toString(tokenType.charAt(0)).toUpperCase() + tokenType.substring(1);

            }
            return tokenType;
        }

    }
}



