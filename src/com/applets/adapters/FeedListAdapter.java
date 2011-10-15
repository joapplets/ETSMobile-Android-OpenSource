package com.applets.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.applets.R;
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

    private class FeedWrapper {
	private TextView title;
	private TextView description;

	private View base;
	private ImageView image;

	public FeedWrapper(View base) {
	    this.base = base;
	}

	public TextView getDescription() {
	    if (description == null) {
		description = (TextView) base
			.findViewById(R.id.feed_list_row_desc);
	    }
	    return description;
	}

	public TextView getTitle() {
	    if (title == null) {
		title = (TextView) base.findViewById(R.id.feed_list_row_title);
	    }
	    return title;
	}

	private ImageView getImage() {
	    if (image == null) {
		image = (ImageView) base.findViewById(R.id.feed_list_row_image);
	    }
	    return image;
	}

	public void setTitle(String text) {
	    getTitle().setText(text);
	}

	public void setDescription(String text) {
	    getDescription().setText(text);
	}

	public void setImage(String image) {
	    // if (image == "") {
	    // getImage().setImageResource(R.drawable.spacer_middle);
	    // } else {
	    // try {
	    // Drawable draw = new ImageFetcher().execute(image).get();
	    // getImage().setImageDrawable(draw);
	    // } catch (InterruptedException e) {
	    // // TODO Auto-generated catch block
	    // e.printStackTrace();
	    // } catch (ExecutionException e) {
	    // // TODO Auto-generated catch block
	    // e.printStackTrace();
	    // }
	    // }
	}

    }
}
