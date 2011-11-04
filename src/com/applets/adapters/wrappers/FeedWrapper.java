package com.applets.adapters.wrappers;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.applets.R;

public class FeedWrapper {

    private View base;

    private TextView title;
    private TextView description;
    private ImageView image;

    public FeedWrapper(View base) {
	this.base = base;
    }

    private TextView getDescription() {
	if (description == null) {
	    description = (TextView) base.findViewById(R.id.feed_list_row_desc);
	}
	return description;
    }

    private TextView getTitle() {
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

}