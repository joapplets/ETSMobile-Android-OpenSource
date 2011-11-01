package com.applets.adapters.wrappers;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.applets.R;

public class CourseWrapper {
    private View view;
    private TextView title = null;
    private TextView description = null;
    private ImageView image;

    public CourseWrapper(View base) {
	view = base;
    }

    public void setImage(String image) {
	getImage().setImageResource(R.drawable.spacer_middle);
    }

    private ImageView getImage() {
	if (image == null) {
	    image = (ImageView) view.findViewById(R.id.program_list_row_image);
	}
	return image;
    }

    private TextView getTitle() {
	if (title == null) {
	    title = (TextView) view.findViewById(R.id.program_list_row_title);
	}
	return title;
    }

    private TextView getDescription() {
	if (description == null) {
	    description = (TextView) view
		    .findViewById(R.id.program_list_row_desc);
	}
	return description;
    }

    public void setDescription(String description) {
	getDescription().setText(description);
    }

    public void setTitle(String title) {
	getTitle().setText(title);
    }
}