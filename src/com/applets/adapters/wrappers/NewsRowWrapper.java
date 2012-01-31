package com.applets.adapters.wrappers;

import android.text.Html;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.applets.R;

public class NewsRowWrapper {

	private TextView description = null;
	private ImageView image;
	private TextView name = null;
	private final View view;

	public NewsRowWrapper(final View base) {
		view = base;
	}

	private TextView getDescription() {
		if (description == null) {
			description = (TextView) view.findViewById(R.id.base_row_desc);
		}
		return description;
	}

	private ImageView getImage() {
		if (image == null) {
			image = (ImageView) view.findViewById(R.id.base_row_image);
		}
		return image;
	}

	private TextView getName() {
		if (name == null) {
			name = (TextView) view.findViewById(R.id.base_row_title);
		}
		return name;
	}

	public void setDescription(final String description) {
		getDescription().setText(Html.fromHtml(description));
		getDescription().setAutoLinkMask(Linkify.WEB_URLS);
	}

	public void setImage(final String image) {
		// getImage().setImageResource(R.drawable.spacer_middle);
	}

	public void setTitle(final String title) {
		getName().setText(title);
	}
}