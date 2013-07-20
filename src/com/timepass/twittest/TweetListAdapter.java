package com.timepass.twittest;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TweetListAdapter extends ArrayAdapter<Tweet> {

	private ArrayList<Tweet> tweets;
	private Context context;
	
	public TweetListAdapter(Context context, int textViewResourceId, ArrayList<Tweet> items) {
		super(context, textViewResourceId, items);
		this.tweets = items;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.activity_twitter, null);
		}
		Tweet t = tweets.get(position);
		TextView tt = (TextView) v.findViewById(R.id.toptext);
		TextView bt = (TextView) v.findViewById(R.id.bottomtext);
		tt.setText(t.getContent());
		bt.setText(t.getTimeStamp());
		return v;
	}
}
