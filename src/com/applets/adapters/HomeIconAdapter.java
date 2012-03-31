package com.applets.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.applets.R;

public class HomeIconAdapter extends BaseAdapter {
	private final Context mContext;

	// references to icons
	private final Integer[] mIcons = { R.drawable.icon_profile,
			R.drawable.icon_news, R.drawable.icon_tour,
			R.drawable.icon_emergency, R.drawable.icon_schedule,
			R.drawable.icon_addressbook, R.drawable.icon_courses };

	// references to icons labels
	private final String[] mIconsLabels = { "Profil", "Nouvelles",
			"Tour guidé", "Sécurité", "Horaire", "Bottin", "Mes cours" };

	public HomeIconAdapter(final Context c) {
		mContext = c;
	}

	@Override
	public int getCount() {
		return mIcons.length;
	}

	@Override
	public Object getItem(final int position) {
		return null;
	}

	@Override
	public long getItemId(final int position) {
		return 0;
	}

	// TO-DO, switch to Wrapper /w Inflater
	// create a new ImageView for each item referenced by the Adapter
	@Override
	public View getView(final int position, final View convertView,
			final ViewGroup parent) {
		LinearLayout iconLayout;
		ImageView imageView;
		TextView iconLabel;
		if (convertView == null) { // if it's not recycled, initialize some
			// attributes
			iconLayout = new LinearLayout(mContext);
			iconLayout.setOrientation(LinearLayout.VERTICAL);
			iconLayout.setGravity(Gravity.CENTER_VERTICAL);

			imageView = new ImageView(mContext);
			imageView.setImageResource(mIcons[position]);
			LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			layout.gravity = Gravity.CENTER;
			imageView.setLayoutParams(layout);
			imageView.setScaleType(ImageView.ScaleType.CENTER);

			iconLabel = new TextView(mContext);
			layout = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			layout.gravity = Gravity.CENTER;
			iconLabel.setLayoutParams(layout);
			iconLabel.setText(mIconsLabels[position]);

			iconLayout.addView(imageView);
			iconLayout.addView(iconLabel);
		} else {
			iconLayout = (LinearLayout) convertView;
		}

		return iconLayout;
	}
}
