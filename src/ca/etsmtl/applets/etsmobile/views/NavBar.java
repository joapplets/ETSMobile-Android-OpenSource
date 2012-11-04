package ca.etsmtl.applets.etsmobile.views;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import ca.etsmtl.applets.etsmobile.ETSMobileActivity;
import ca.etsmtl.applets.etsmobile.R;

public class NavBar extends RelativeLayout {

	private RelativeLayout root_layout;
	private ImageButton homeBtn;
	private ImageView imageTitle;
	private Button rightBtn;
	private ProgressBar loading;
	private TextView txtTitle;

	public NavBar(final Context context) {
		super(context);

	}

	public NavBar(final Context context, final AttributeSet attrs) {
		super(context, attrs);

		final LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		root_layout = (RelativeLayout) layoutInflater.inflate(R.layout.nav_bar,
				this, true);

		if (!isInEditMode()) {
			init();
		}
	}

	public void hideHome() {
		homeBtn.setVisibility(View.INVISIBLE);
	}

	public void hideLoading() {
		loading.setVisibility(View.GONE);
	}

	public void hideRightButton() {
		rightBtn.setVisibility(View.INVISIBLE);
	}

	public void hideTitle() {
		imageTitle.setVisibility(View.INVISIBLE);
	}

	private void init() {
		homeBtn = (ImageButton) root_layout
				.findViewById(R.id.base_bar_home_btn);
		imageTitle = (ImageView) root_layout
				.findViewById(R.id.base_bar_img_title);
		imageTitle.setVisibility(View.INVISIBLE);
		txtTitle = (TextView) root_layout.findViewById(R.id.base_bar_title);
		rightBtn = (Button) root_layout.findViewById(R.id.base_bar_source_btn);
		loading = (ProgressBar) root_layout.findViewById(R.id.base_bar_loading);
		homeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				getContext().startActivity(
						new Intent(getContext(), ETSMobileActivity.class));
			}
		});
	}

	public void setHomeAction(final OnClickListener l) {
		homeBtn.setOnClickListener(l);
	}

	public void setTitle(final int resId) {
		imageTitle.setVisibility(View.VISIBLE);
		imageTitle.setImageResource(resId);
	}

	public void setTitle(final String title) {
		imageTitle.setVisibility(View.INVISIBLE);
		txtTitle.setText(title);
		txtTitle.setVisibility(View.VISIBLE);
	}

	public void showLoading() {
		loading.setVisibility(View.VISIBLE);
	}

}
