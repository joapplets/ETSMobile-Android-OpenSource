package com.applets.adapters.wrappers;

import android.text.Html;
import android.text.util.Linkify;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.applets.R;

public class NewsWrapper {

	private TextView description = null;
	private ImageView image;
	private TextView title = null;
	private final View view;

	public NewsWrapper(final View base) {
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

	private TextView getTitle() {
		if (title == null) {
			title = (TextView) view.findViewById(R.id.base_row_title);
		}
		return title;
	}

	public void setDescription(final String description) {
		getDescription().setText(Html.fromHtml(description));
		getDescription().setAutoLinkMask(Linkify.WEB_URLS);
	}

	public void setImage(final String image) {
		// getImage().setImageResource(R.drawable.spacer_middle);
	}

	public void setTitle(final String title) {
		getTitle().setText(title);
	}
}