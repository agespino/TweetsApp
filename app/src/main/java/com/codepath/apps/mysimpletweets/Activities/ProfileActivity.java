package com.codepath.apps.mysimpletweets.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.TwitterClient;
import com.codepath.apps.mysimpletweets.fragments.UserTimelineFragment;
import com.codepath.apps.mysimpletweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ProfileActivity extends AppCompatActivity {
    TwitterClient client;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        String screenName;

        int fromActionBar = getIntent().getIntExtra("fromActionBar",0);
        if(fromActionBar == 0)
        {
            client = TwitterApplication.getRestClient();

            client.getUserInfo(new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    user = User.fromJsonObject(response);//current user account info
                    getSupportActionBar().setTitle("@"+user.getScreenName());
                    populateProfileHeader(user);
                }
            });
            screenName = getIntent().getStringExtra("screen_name");

        }
        else{
            user = (User)getIntent().getSerializableExtra("user");
            screenName = user.getScreenName();
            getSupportActionBar().setTitle("@"+user.getScreenName());
            populateProfileHeader(user);
        }

        UserTimelineFragment fragmentUserTimeline = UserTimelineFragment.newInstance(screenName);
        //Display user timeline fragment into container
        if(savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, fragmentUserTimeline);
            ft.commit();
        }
        //Display User fragment

    }

    private void populateProfileHeader(User user) {
        TextView tvUsername = (TextView) findViewById(R.id.tvUsername);
        TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
        ImageView ivProfileImage = (ImageView)findViewById(R.id.ivProfileImage);
        tvUsername.setText(user.getName());
        tvTagline.setText(user.getTagline());
        tvFollowers.setText(user.getFollowersCount() + " Followers");
        tvFollowing.setText(user.getFriendsCount() + " Following");

        Picasso.with(this).load(user.getProfileImageUrl()).into(ivProfileImage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }
}
