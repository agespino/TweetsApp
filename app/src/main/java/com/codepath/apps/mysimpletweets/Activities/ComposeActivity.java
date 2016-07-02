package com.codepath.apps.mysimpletweets.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {
    EditText etCompose;
    Button btnSubmit;
    TwitterClient client;
    ImageView ivProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        etCompose = (EditText) findViewById(R.id.etCompose);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        client = TwitterApplication.getRestClient();
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final String tweetText = etCompose.getText().toString();
                client.postTweet(tweetText, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                        super.onSuccess(statusCode, headers, json);
                        Log.d("DEBUG", json.toString());
                        User user = User.fromJsonObject(json);
                        Tweet tweet = Tweet.fromJson(json);
                        tweet.setBody(tweetText);
                        tweet.setUser(user);
                        //Picasso.with(v.getContext()).load(user.getProfileImageUrl()).into(ivProfileImage);

                        Intent data = new Intent();
                        // Pass relevant data back as a result
                        data.putExtra("tweet", tweet);
                        // Activity finished ok, return the data
                        setResult(RESULT_OK, data); // set result code and bundle data for response
                        finish();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Log.d("DEBUG", errorResponse.toString());

                    }

                });
            }
        });
    }

   /* public void OnSubmit(final View view) {
        final String tweetText = etCompose.getText().toString();
        client.postTweet(tweetText, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject json) {
                super.onSuccess(statusCode, headers, json);
                User user = User.fromJsonObject(json);
                Tweet tweet= Tweet.fromJson(json);
                tweet.setBody(tweetText);
                tweet.setUser(user);
                Picasso.with(view.getContext()).load(user.getProfileImageUrl()).into(ivProfileImage);
                Log.d("DEBUG", tweet.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });

    }*/

}
