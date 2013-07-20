package com.timepass.twittest;

import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;

public class TwitterActivity extends ListActivity {

	private static final String APIKEY = "";
	private static final String APISECRET = "";
	private static final String URL = "https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=pardroid&include_rts=1&count=10";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppData.activity = this;
		String token = TweetGetter.getBearerToken(APIKEY, APISECRET);
		ArrayList<Tweet> items = new ArrayList<Tweet>();
		if(token != null)
			items = TweetGetter.getTweets(token, URL);
		TweetListAdapter adapter = new TweetListAdapter(this, R.menu.activity_twitter, items);
		setListAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_twitter, menu);
		return true;
	}
	
}
