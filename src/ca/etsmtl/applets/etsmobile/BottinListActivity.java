package ca.etsmtl.applets.etsmobile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FilterQueryProvider;
import android.widget.ImageButton;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import ca.etsmtl.applets.etsmobile.models.BottinEntry;
import ca.etsmtl.applets.etsmobile.tools.db.BottinDBAdapter;
import ca.etsmtl.applets.etsmobile.tools.db.SQLDBHelper;
import ca.etsmtl.applets.etsmobile.tools.xml.XMLAppletsHandler;
import ca.etsmtl.applets.etsmobile.tools.xml.XMLBottinHandler;
import ca.etsmtl.applets.etsmobile.tools.xml.XMLParser;

public class BottinListActivity extends ListActivity implements
		OnClickListener, TextWatcher, OnItemClickListener {
	private static final int[] VIEWS = new int[] {
			R.id.bottin_list_item_prenom, R.id.bottin_list_item_nom,
			R.id.bottin_list_item_service };
	private static final String[] PROJECTION = new String[] {
			SQLDBHelper.BOTTIN_NOM, SQLDBHelper.BOTTIN_PRENOM,
			SQLDBHelper.BOTTIN_SERVICE, SQLDBHelper.BOTTIN_ID };
	private static final int ALERT_INIT_BOTTIN = 0;
	protected static final String LOG_TAG = "BottinListActivity";

	/**
	 * Get full bottin from ets. no filters needed, so keep full db on phone.
	 * 
	 * @author Philippe David
	 * @version 1.0
	 * 
	 */
	private class BottinLoader extends AsyncTask<Void, Void, InputStream> {

		private static final int BOTTIN_LOADING = 1;

		/**
		 * Change the SimpleCursorAdapter with a full new list from DB
		 * 
		 * @author Philippe David
		 * @version 1.0
		 */
		private final class RefreshCursor implements Runnable {
			@Override
			public void run() {
				simpleCursor.changeCursor(bottinDB.getAll());
				simpleCursor.notifyDataSetChanged();
			}
		}

		private OutputStreamWriter writer;

		@Override
		protected InputStream doInBackground(final Void... params) {

			try {
				final String request = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
						+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
						+ "<soap:Body>"
						+ "<Recherche xmlns=\"http://etsmtl.ca/\">"
						+ "<FiltreNom></FiltreNom>"
						+ "<FiltrePrenom></FiltrePrenom>"
						+ "<FiltreServiceCode></FiltreServiceCode>"
						+ "</Recherche>" + "</soap:Body>" + "</soap:Envelope>";
				final URL url = new URL(
						"http://etsmtl.ca/cmspages/webservice.asmx?op=Recherche");
				final HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setDoOutput(true);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Host", "etsmtl.ca");
				conn.setRequestProperty("Content-Type",
						"text/xml; charset=utf-8");
				conn.setRequestProperty("SOAPAction",
						"\"http://etsmtl.ca/Recherche\"");

				writer = new OutputStreamWriter(conn.getOutputStream());

				writer.write(request);
				writer.flush();

				final InputStream stream = conn.getInputStream();
				if (stream != null) {
					final XMLAppletsHandler xmlHandler = new XMLBottinHandler(
							BottinListActivity.this);
					final XMLParser xml = new XMLParser(stream, xmlHandler,
							BottinListActivity.this);
					final List<BottinEntry> newList = xml.getBottinEntries();

					// insert all, with transaction
					bottinDB.insertAll(newList);

				}

			} catch (final MalformedURLException e) {
				e.printStackTrace();
			} catch (final IOException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(BOTTIN_LOADING);
		}

		@Override
		protected void onPostExecute(final InputStream stream) {
			dismissDialog(BOTTIN_LOADING);
			uiHandler.post(new RefreshCursor());
		}
	}

	private Cursor allEntryCursor;
	private BottinDBAdapter bottinDB;
	Handler uiHandler;

	private SimpleCursorAdapter simpleCursor;
	private TextView txtView;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base_list);
		uiHandler = new Handler();
		bottinDB = BottinDBAdapter.getInstance(getApplicationContext());

		allEntryCursor = bottinDB.getAll();
		startManagingCursor(allEntryCursor);

		// cursor adapter is faster
		simpleCursor = new SimpleCursorAdapter(getApplicationContext(),
				R.layout.bottin_list_item, allEntryCursor, PROJECTION, VIEWS);

		simpleCursor.setFilterQueryProvider(new FilterQueryProvider() {

			@Override
			public Cursor runQuery(CharSequence constraint) {
				Log.d(LOG_TAG, "filter input  :" + constraint);
				return bottinDB.getAllWhere((String) constraint);
			}
		});
		setListAdapter(simpleCursor);
		getListView().setOnItemClickListener(this);

		// check if need to update.
		// TODO : Add timer ? Check each Month ? Push Notification ?
		// ets might not change the list that soon, soo ...
		if (allEntryCursor.getCount() < 1) {
			showDialog(ALERT_INIT_BOTTIN);
		}

		/**
		 * SEARCH NAV BAR TODO: Create custom View -> SearchBar
		 * */
		// home btn
		((ImageButton) findViewById(R.id.search_nav_bar_home_btn))
				.setOnClickListener(this);

		// init textview with filter options
		txtView = (TextView) findViewById(R.id.search_nav_bar_autotxt);
		txtView.addTextChangedListener(this);

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog d;
		switch (id) {
		case 0:
			AlertDialog.Builder builder = new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setMessage(R.string.bottin_init_alert)
					.setTitle("Attention")
					.setPositiveButton(R.string.yes,
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {

									new BottinLoader().execute();
									dismissDialog(ALERT_INIT_BOTTIN);
								}
							})
					.setNegativeButton(R.string.no,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									BottinListActivity.this.finish();
								}
							});
			d = builder.create();
			break;

		case 1:
			d = ProgressDialog.show(this, "", "Loading. Please wait...", true);
			break;
		default:
			d = null;
			break;
		}
		return d;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_nav_bar_home_btn:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		Log.d(LOG_TAG, s + " - " + start + " - " + before + " - " + count + "");
		if (simpleCursor != null) {
			simpleCursor.getFilter().filter(s);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void afterTextChanged(Editable s) {
	}

	@Override
	public void onItemClick(AdapterView<?> vc, View view, int position, long id) {
		Intent intent = new Intent(getApplicationContext(),
				BottinViewActivity.class);
		intent.putExtra("id", id);
		startActivity(intent);
	}
}
