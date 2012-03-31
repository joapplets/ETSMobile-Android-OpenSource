package com.applets.adapters.wrappers;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.applets.R;

public class NewsRowWrapper {

	private TextView date;
	private TextView description = null;
	private ImageView image;
	private TextView name = null;
	private final View view;

	public NewsRowWrapper(final View base) {
		view = base;
	}

	private TextView getDate() {
		if (date == null) {
			date = (TextView) view.findViewById(R.id.base_row_date);
		}
		return date;
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

	public void setDate(final String date) {
		getDate().setText(date);
	}

	public void setDescription(final String description) {
		getDescription().setText(Html.fromHtml(description));
	}

	public void setImage(final String image) throws MalformedURLException,
			IOException {
		Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(
				"http://" + image).getContent());
		bitmap = Bitmap.createBitmap(bitmap, 0, 0, 105, 105);
		getImage().setImageBitmap(bitmap);
	}

	public void setTitle(final String title) {
		getName().setText(Html.fromHtml(title));
	}

}