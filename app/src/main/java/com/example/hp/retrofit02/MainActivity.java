package com.example.hp.retrofit02;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import retrofit2.*;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwitterService twitterService =  TwitterService.retrofit.create(TwitterService.class);
                 Call<List<Contributor>> call = twitterService.repoContributors();
                new NetworkCall().execute(call);
            }
        });
    }



    private class NetworkCall extends AsyncTask<Call, Void, String> {

        @Override
        protected String doInBackground(Call... params) {
            try{
                params[0].execute().body().toString();
            }catch (IOException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result){
            TextView textView = (TextView) findViewById(R.id.textView1);
            textView.setText(result);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //  getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    int getData(){
        return 0;
    }
}
