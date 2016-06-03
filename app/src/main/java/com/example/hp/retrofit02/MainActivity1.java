package com.example.hp.retrofit02;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity1 extends AppCompatActivity {
    private static final String TAG = "MainActivity1";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_1);

        Button btnOAuth = (Button) findViewById(R.id.btnOAuth);

        if (btnOAuth != null) {
            btnOAuth.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), AuthActivity.class);
                    startActivity(intent);
                }
            });

        }

        Button button = (Button) findViewById(R.id.button);
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                    OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("https://api.github.com/")
                            .client(client)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    Call<List<Contributor>> call = retrofit.create(TwitterService.class).repoContributors();
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


}