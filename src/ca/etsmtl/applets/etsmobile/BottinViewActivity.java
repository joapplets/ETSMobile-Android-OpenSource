package ca.etsmtl.applets.etsmobile;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;
import ca.etsmtl.applets.etsmobile.providers.ETSMobileContentProvider;
import ca.etsmtl.applets.etsmobile.tools.db.BottinTableHelper;

import com.etsmt.applets.etsmobile.views.NavBar;

public class BottinViewActivity extends Activity {

	private TextView nomView;
	private TextView prenomView;
	private TextView serviceView;
	private TextView emplView;
	private TextView titreView;
	private TextView courrielView;
	private TextView phoneView;
	private NavBar navBar;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bottin_view);

		final Long b = (Long) getIntent().getExtras().get("id");
		// long id = b.getLong("id");

		nomView = (TextView) findViewById(R.id.bottin_view_nom);
		prenomView = (TextView) findViewById(R.id.bottin_view_prenom);
		serviceView = (TextView) findViewById(R.id.bottin_view_service);
		emplView = (TextView) findViewById(R.id.bottin_view_empl);
		titreView = (TextView) findViewById(R.id.bottin_view_titre);
		courrielView = (TextView) findViewById(R.id.bottin_view_courriel);
		phoneView = (TextView) findViewById(R.id.bottin_view_phone);

		final Cursor cursor = managedQuery(Uri.withAppendedPath(
				ETSMobileContentProvider.CONTENT_URI_BOTTIN, b.toString()),
				BottinTableHelper.AVAILABLE, null, null, null);
		if (cursor.getCount() > 0 && cursor.moveToFirst()) {
			final CharSequence nom = cursor.getString(cursor
					.getColumnIndex(BottinTableHelper.BOTTIN_NOM));
			nomView.setText(nom);
			prenomView.setText(cursor.getString(cursor
					.getColumnIndex(BottinTableHelper.BOTTIN_PRENOM)));
			serviceView.setText(cursor.getString(cursor
					.getColumnIndex(BottinTableHelper.BOTTIN_SERVICE)));
			emplView.setText(cursor.getString(cursor
					.getColumnIndex(BottinTableHelper.BOTTIN_EMPLACEMENT)));
			titreView.setText(cursor.getString(cursor
					.getColumnIndex(BottinTableHelper.BOTTIN_TIRE)));
			courrielView.setText(cursor.getString(cursor
					.getColumnIndex(BottinTableHelper.BOTTIN_COURRIEL)));
			phoneView.setText(cursor.getString(cursor
					.getColumnIndex(BottinTableHelper.BOTTIN_TELBUREAU)));
		}

		navBar = (NavBar) findViewById(R.id.navBar1);
		navBar.setTitle(R.drawable.navbar_phonebook_title);
		navBar.hideRightButton();

	}
}
