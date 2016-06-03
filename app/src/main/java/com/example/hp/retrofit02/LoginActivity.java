package com.example.hp.retrofit02;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;

public class LoginActivity extends AppCompatActivity {

    private static SharedPreferences mSharedPreferences;
    private static Twitter twitter;
    private static RequestToken requestToken;
    private AccessToken accessToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set full screen
        // set context
        setContentView(R.layout.activity_home);

        // define variable for the settings
        mSharedPreferences = getApplicationContext().getSharedPreferences(
                "twitter4j-sample", 0);

        Log.v("LoggedIn", "getUser().execute(<String>);");
        // process getting a user @<:handle>
        new getUser().execute("twitterdev");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Used to put dark icons on light action bar
        final Menu m = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        final MenuItem searchItem = menu.findItem(R.id.search_icon);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        //searchView.setSubmitButtonEnabled(true);
        assert searchView != null;

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO Auto-generated method stub
                Log.v("search", query);
                searchView.setIconified(true);
                searchView.clearFocus();
                searchView.setQuery("", false);
                new getUser().execute(query);


                // This method does not exist
                searchView.setIconified(true);
                return false;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                // TODO Auto-generated method stub

                return false;
            }

        });
        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.menu_logout) {
            mSharedPreferences = getApplicationContext().getSharedPreferences(
                    "twitter4j-sample", 0);
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.clear();
            editor.commit();
            Intent myIntent = new Intent(getBaseContext(), MainActivity1.class);
            startActivity(myIntent);
            return true;
        } else if (id == R.id.refresh) {
            Intent refresh = new Intent(getBaseContext(), LoginActivity.class);
            startActivity(refresh);
        }
        return super.onOptionsItemSelected(item);
    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/

    private class getUser extends AsyncTask<String, Void, String> {
        // set the last_tweet this is displayed on the device in a TextView
        private String last_tweet;

        @Override
        protected String doInBackground(String... params) {
            Log.v("LoggedIn", "getUser..doInBackground");
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(Constants.API_KEY);
            builder.setOAuthConsumerSecret(Constants.API_SECRET);

            // // Setup preferences
            mSharedPreferences = getApplicationContext().getSharedPreferences(
                    "twitter4j-sample", 0);

            // Access Token
            String access_token = mSharedPreferences.getString(Constants.PREF_KEY_OAUTH_TOKEN, "");
            // Access Token Secret
            String access_token_secret = mSharedPreferences.getString(Constants.PREF_KEY_OAUTH_SECRET, "");
            // Setup Access Token
            AccessToken accessToken = new AccessToken(access_token, access_token_secret);
            // Setup instance of twitter to perform requests e.g. twitter.showUser(<:handle>);
            Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);
            // Define the twitter_handle
            final String twitter_handle = params[0];
            // Try and make magic
            try {

                twitter4j.User user = twitter.showUser(twitter_handle);
                twitter4j.Status s = user.getStatus();
                final String last_tweet = s.getText();
                runOnUiThread(new Runnable() {

                    public void run() {
                        Log.v("LoggedIn", "running on UI thread update text view");
                        TextView lt = (TextView) findViewById(R.id.last_tweet);
                        lt.setText(last_tweet);
                        TextView h = (TextView) findViewById(R.id.handleId);
                        h.setText("@" + twitter_handle);

                    }
                });
                //Log.v("Last Tweet", s.getText());
            } catch (TwitterException e) {
                Toast.makeText(getApplicationContext(), "Oops something bad happened...", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            return null;
        }
    }
}
