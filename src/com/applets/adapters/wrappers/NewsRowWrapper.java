package com.applets.adapters.wrappers;

import android.text.Html;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.applets.R;

public class NewsRowWrapper {

    private View view;
    private TextView name = null;
    private TextView description = null;
    private ImageView image;

    public NewsRowWrapper(View base) {
	view = base;
    }

    private ImageView getImage() {
	if (image == null) {
	    image = (ImageView) view.findViewById(R.id.news_row_image);
	}
	return image;
    }

    private TextView getName() {
	if (name == null) {
	    name = (TextView) view.findViewById(R.id.news_row_title);
	}
	return name;
    }

    private TextView getDescription() {
	if (description == null) {
	    description = (TextView) view.findViewById(R.id.news_row_desc);
	}
	return description;
    }

    public void setImage(String image) {
	getImage().setImageResource(R.drawable.spacer_middle);
    }

    public void setDescription(String description) {
	getDescription().setText(Html.fromHtml(description));
	getDescription().setAutoLinkMask(Linkify.WEB_URLS);
    }

    public void setTitle(String title) {
	getName().setText(title);
    }
}