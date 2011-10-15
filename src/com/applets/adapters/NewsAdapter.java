package com.applets.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.text.Html;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.applets.R;
import com.applets.models.Model;
import com.applets.models.News;

public class NewsAdapter extends ArrayAdapter<Model> {
    private Activity context;

    public NewsAdapter(Activity context, ArrayList<Model> articles) {
	super(context, R.layout.news_row, articles);
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

    private class NewsWrapper {

	private View view;
	private TextView title = null;
	private TextView description = null;
	private ImageView image;

	public NewsWrapper(View base) {
	    view = base;
	}

	public void setImage(String image) {
	    getImage().setImageResource(R.drawable.spacer_middle);
	}

	private ImageView getImage() {
	    if (image == null) {
		image = (ImageView) view.findViewById(R.id.news_row_image);
	    }
	    return image;
	}

	private TextView getTitle() {
	    if (title == null) {
		title = (TextView) view.findViewById(R.id.news_row_title);
	    }
	    return title;
	}

	public TextView getDescription() {
	    if (description == null) {
		description = (TextView) view.findViewById(R.id.news_row_desc);
	    }
	    return description;
	}

	public void setDescription(String description) {
	    getDescription().setText(Html.fromHtml(description));
	    getDescription().setAutoLinkMask(Linkify.WEB_URLS);
	}

	public void setTitle(String title) {
	    getTitle().setText(title);
	}
    }
}