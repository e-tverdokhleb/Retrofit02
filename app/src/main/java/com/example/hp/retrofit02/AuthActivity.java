package com.example.hp.retrofit02;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

import retrofit2.Call;

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
                    Intent intent = new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(ServiceGenerator.API_BASE_URL + "activity_login" + "?client_id=" + clientId + "&redirect_uri=" + redirectUri));
                    startActivity(intent);
                }
            });

        }
    }

    @Override
    protected void onResume(){
        super.onResume();

        Uri uri = getIntent().getData();
        if (uri != null && uri.toString().startsWith(redirectUri)){
            String code = uri.getQueryParameter("code");
            if (code != null) {
                Log.d(TAG, code );
                LoginService loginService = ServiceGenerator.createService(LoginService.class, clientId,  clientSecret);
                Call<ServiceGenerator.AccessToken> call = loginService.getAccessToken(code, "autorization_code");
                try {
                    ServiceGenerator.AccessToken accessToken = call.execute().body();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if (uri.getQueryParameter("error") != null) {
                Log.e(TAG, "Query parameter - error");
            }
        }

    }


}
