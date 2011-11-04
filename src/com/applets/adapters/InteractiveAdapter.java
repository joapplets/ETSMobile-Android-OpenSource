package com.applets.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.applets.R;

public class InteractiveAdapter extends BaseAdapter {
    private Context mContext;
    // references to our images
    private Integer[] mThumbIds = { R.drawable.sample_2, R.drawable.sample_3,
	    R.drawable.sample_4, R.drawable.sample_5, R.drawable.sample_6,
	    R.drawable.sample_7 };

    public InteractiveAdapter(Context c) {
	mContext = c;
    }

    @Override
    public int getCount() {
	return mThumbIds.length;
    }

    @Override
    public Object getItem(int position) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public long getItemId(int position) {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public int getItemViewType(int position) {
	return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	ImageView imageView;
	if (convertView == null) { // if it's not recycled, initialize some
				   // attributes
	    imageView = new ImageView(mContext);
	    imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
	    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
	    imageView.setPadding(8, 8, 8, 8);
	} else {
	    imageView = (ImageView) convertView;
	}

	imageView.setImageResource(mThumbIds[position]);
	return imageView;
    }

    @Override
    public int getViewTypeCount() {
	return 1;
    }

}
