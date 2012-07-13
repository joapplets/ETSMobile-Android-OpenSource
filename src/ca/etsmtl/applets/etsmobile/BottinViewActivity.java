package ca.etsmtl.applets.etsmobile;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import ca.etsmtl.applets.etsmobile.models.BottinEntry;
import ca.etsmtl.applets.etsmobile.tools.db.BottinDBAdapter;

public class BottinViewActivity extends Activity {

	private TextView nomView;
	private TextView prenomView;
	private TextView serviceView;
	private TextView emplView;
	private TextView titreView;
	private TextView courrielView;
	private TextView phoneView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bottin_view);

		Bundle b = getIntent().getExtras();
		long id = b.getLong("id");

		nomView = (TextView) findViewById(R.id.bottin_view_nom);
		prenomView = (TextView) findViewById(R.id.bottin_view_prenom);
		serviceView = (TextView) findViewById(R.id.bottin_view_service);
		emplView = (TextView) findViewById(R.id.bottin_view_empl);
		titreView = (TextView) findViewById(R.id.bottin_view_titre);
		courrielView = (TextView) findViewById(R.id.bottin_view_courriel);
		phoneView = (TextView) findViewById(R.id.bottin_view_phone);

		BottinEntry bottinEntry = BottinDBAdapter.getInstance(this)
				.getEntry(id);

		nomView.setText(bottinEntry.getNom());
		prenomView.setText(bottinEntry.getPrenom());
		serviceView.setText(bottinEntry.getService());
		emplView.setText(bottinEntry.getEmplacement());
		titreView.setText(bottinEntry.getTitre());
		courrielView.setText(bottinEntry.getCourriel());
		phoneView.setText(bottinEntry.getTelBureau());

		((ImageButton) findViewById(R.id.search_nav_bar_home_btn))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						BottinViewActivity.this.finish();
					}
				});

	}
}
