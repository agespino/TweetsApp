package com.codepath.apps.mysimpletweets.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.fragments.HomeTimelineFragment;
import com.codepath.apps.mysimpletweets.fragments.MentionsTimelineFragment;
import com.codepath.apps.mysimpletweets.fragments.TweetsListFragment;
import com.codepath.apps.mysimpletweets.models.Tweet;

public class TimelineActivity extends AppCompatActivity {

    private TweetsListFragment fragmentTweetsList;
    TweetsPagerAdapter adapterViewPager;
    private final int REQUEST_CODE = 1122;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        //get viewpager
        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        //set viewpager adapter for pager
        //vpPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));
        //find sliding tabstrip
        adapterViewPager = new TweetsPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip)findViewById(R.id.tabs);
        //attach tabstrip
        tabStrip.setViewPager(vpPager);
     }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    public void onProfileView(MenuItem item) {
        //launch profile view
        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra("fromActionBar", 0);
        startActivity(i);
    }

    public void onCompose(MenuItem item) {
        Intent i = new Intent(this, ComposeActivity.class);
        startActivityForResult(i, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            Tweet tweet = (Tweet)data.getSerializableExtra("tweet");
            HomeTimelineFragment fragmentHomeTweets =
                    (HomeTimelineFragment) adapterViewPager.getRegisteredFragment(0);
            fragmentHomeTweets.appendTweet(tweet);
        }
    }


    public class TweetsPagerAdapter extends SmartFragmentStatePagerAdapter{
       final int PAGE_COUNT = 2;
       private String tabTitles[] = {"Home", "Mentions"};

       public TweetsPagerAdapter(FragmentManager fm){
           super(fm);
       }

       @Override
       public Fragment getItem(int position) {
           if(position == 0){
               return new HomeTimelineFragment();
           }
           else if(position == 1){
               return new MentionsTimelineFragment();
           }
           else {
               return null;
           }
       }

       @Override
       public CharSequence getPageTitle(int position) {
           return tabTitles[position];
       }

       @Override
       public int getCount() {
           return tabTitles.length;
       }
   }

}

