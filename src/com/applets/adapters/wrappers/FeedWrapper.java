package com.applets.adapters.wrappers;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.markupartist.android.widget.actionbar.R;

public class FeedWrapper {

    private final View base;

    private TextView description;
    private ImageView image;
    private TextView title;

    public FeedWrapper(final View base) {
	this.base = base;
    }

    private TextView getDescription() {
	if (description == null) {
	    description = (TextView) base.findViewById(R.id.feed_list_row_desc);
	}
	return description;
    }

    private ImageView getImage() {
	if (image == null) {
	    image = (ImageView) base.findViewById(R.id.feed_list_row_image);
	}
	return image;
    }

    private TextView getTitle() {
	if (title == null) {
	    title = (TextView) base.findViewById(R.id.feed_list_row_title);
	}
	return title;
    }

    public void setDescription(final String text) {
	getDescription().setText(text);
    }

    public void setImage(final String image) {
	// if (image == "") {
	getImage();
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

    public void setTitle(final String text) {
	getTitle().setText(text);
    }

}