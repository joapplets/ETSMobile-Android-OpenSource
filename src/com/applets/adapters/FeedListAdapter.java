package com.applets.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.applets.R;
import com.applets.adapters.wrappers.FeedWrapper;
import com.applets.models.Feed;

public class FeedListAdapter extends ArrayAdapter<Feed> {

    Activity context;
    ArrayList<Feed> list;

    public FeedListAdapter(Activity context, ArrayList<Feed> feedList) {
	super(context, R.layout.feed_list_row, feedList);
	this.context = context;
	this.list = feedList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	View row = convertView;
	FeedWrapper wrapper;

	if (row == null) {
	    LayoutInflater inflater = context.getLayoutInflater();
	    row = inflater.inflate(R.layout.feed_list_row, null);
	    wrapper = new FeedWrapper(row);
	    row.setTag(wrapper);
	} else {
	    wrapper = (FeedWrapper) row.getTag();
	}

	Feed feed = list.get(position);
	wrapper.setTitle(feed.getName());
	wrapper.setDescription(feed.getUrl());
	wrapper.setImage(feed.getImage());

	return row;
    }

}
