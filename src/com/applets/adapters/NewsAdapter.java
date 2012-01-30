package com.applets.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.applets.R;
import com.applets.adapters.wrappers.NewsRowWrapper;
import com.applets.models.Model;
import com.applets.models.News;

public class NewsAdapter extends ArrayAdapter<Model> {
    private final Activity context;

    public NewsAdapter(final Activity context, final ArrayList<Model> news) {
	super(context, R.layout.basic_row, news);
	this.context = context;
    }

    @Override
    public View getView(final int position, final View convertView,
	    final ViewGroup parent) {
	View row = convertView;
	NewsRowWrapper wrapper;

	if (row == null) {
	    final LayoutInflater inflater = context.getLayoutInflater();
	    row = inflater.inflate(R.layout.basic_row, null);
	    wrapper = new NewsRowWrapper(row);
	    row.setTag(wrapper);
	} else {
	    wrapper = (NewsRowWrapper) row.getTag();
	}
	final News news = (News) getItem(position);
	wrapper.setTitle(news.toString());
	wrapper.setDescription(news.getDescription());
	wrapper.setImage(news.getImage());

	return row;
    }
}