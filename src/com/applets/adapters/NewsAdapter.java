package com.applets.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.applets.R;
import com.applets.adapters.wrappers.NewsWrapper;
import com.applets.models.Model;
import com.applets.models.News;

public class NewsAdapter extends ArrayAdapter<Model> {
    private Activity context;

    public NewsAdapter(Activity context, ArrayList<Model> news) {
	super(context, R.layout.news_row, news);
	this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	View row = convertView;
	NewsWrapper wrapper;

	if (row == null) {
	    LayoutInflater inflater = context.getLayoutInflater();
	    row = inflater.inflate(R.layout.news_row, null);
	    wrapper = new NewsWrapper(row);
	    row.setTag(wrapper);
	} else {
	    wrapper = (NewsWrapper) row.getTag();
	}
	News news = (News) getItem(position);
	wrapper.setTitle(news.toString());
	wrapper.setDescription(news.getDescription());
	wrapper.setImage(news.getImage());

	return row;
    }
}