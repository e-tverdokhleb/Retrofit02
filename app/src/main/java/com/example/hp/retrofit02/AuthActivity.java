package com.example.hp.retrofit02;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthActivity extends AppCompatActivity {
/*
    public static String OAUTH_URL = "https://github.com/login/oauth/authorize";

    public static String OAUTH_ACCESS_TOKEN_URL = "https://github.com/login/oauth/access_token";
                https://api.twitter.com/oauth/access_token
    public static String CLIENT_ID = "D9PMov04JuXz7GyhPsoxmS0Bk";
    public static String CLIENT_SECRET = "EmUlx2NcXtYowaRmaqz2MreCLOTFa8dOu6sIompvN1e7lcGChz";

    public static String CALLBACK_URL = "http://localhost";

    OAUTH_URL + "?client_id=" + CLIENT_ID;
    https://api.twitter.com/oauth/authorize?client_id=D9PMov04JuXz7GyhPsoxmS0Bk

    String query = "client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET + "&code=" + accessCode;
 */

    private final String clientId = "D9PMov04JuXz7GyhPsoxmS0Bk";
    private final String clientSecret = "EmUlx2NcXtYowaRmaqz2MreCLOTFa8dOu6sIompvN1e7lcGChz";
    private final String redirectUri = "";

    private static final String TAG = "Class -AuthActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        if (btnLogin != null) {
            btnLogin.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                    OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("https://dev.twitter.com/pages/auth")
                            .client(client)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    Call<Object> call = retrofit.create(LoginService.class).getToken(clientId);
                    new NetworkCall().execute(call);
                }
            });

        }
    }



    private class NetworkCall extends AsyncTask<Call, Void, String> {

        @Override
        protected String doInBackground(Call... params) {
            Log.d(TAG, "onClick: doInBackground ");
            String response = "This is will be response";
            try {
                response = params[0].execute().body().toString();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "doInBackground: " + e.getMessage());
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d(TAG, "onPostExecute: result = " + result);
            TextView textView = (TextView) findViewById(R.id.textView1);
            textView.setText(result);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();

//        Uri uri = getIntent().getData();
//        if (uri != null && uri.toString().startsWith(redirectUri)){
//            String code = uri.getQueryParameter("code");
//            if (code != null) {
//                Log.d(TAG, code );
//                LoginService loginService = ServiceGenerator.createService(LoginService.class, clientId,  clientSecret);
//                Call<ServiceGenerator.AccessToken> call = loginService.getAccessToken(code, "authorization_code");
//                try {
//                    ServiceGenerator.AccessToken accessToken = call.execute().body();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }else if (uri.getQueryParameter("error") != null) {
//                Log.e(TAG, "Query parameter - error");
//            }
//        }

    }


}
