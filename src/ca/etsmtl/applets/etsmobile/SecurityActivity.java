package ca.etsmtl.applets.etsmobile;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class SecurityActivity extends Activity {

	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		// home btn
		((ImageButton) findViewById(R.id.empty_nav_bar_home_btn))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {
						finish();
					}
				});
	}
}
