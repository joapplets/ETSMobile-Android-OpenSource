package ca.etsmtl.applets.etsmobile;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
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
import ca.etsmtl.applets.etsmobile.providers.BottinContentProvider;
import ca.etsmtl.applets.etsmobile.services.BottinService;
import ca.etsmtl.applets.etsmobile.services.BottinService.BottinBinder;
import ca.etsmtl.applets.etsmobile.tools.db.BottinTableHelper;

public class BottinListActivity extends ListActivity implements
		OnClickListener, TextWatcher, OnItemClickListener {
	private final static String SERVICE = "ca.etsmtl.applets.etsmobile.services.BottinFetcher";

	/**
	 * SimpleCursorAdapter INFO
	 */
	private static final String[] PROJECTION = new String[] {
			BottinTableHelper.BOTTIN_NOM, BottinTableHelper.BOTTIN_PRENOM,
			BottinTableHelper.BOTTIN_TIRE, BottinTableHelper.BOTTIN_SERVICE };
	private static final int[] TXT_VIEWS = new int[] {
			R.id.bottin_list_item_nom, R.id.bottin_list_item_prenom,
			R.id.bottin_list_item_poste, R.id.bottin_list_item_service };
	/**
	 * Dialogs
	 */
	private static final int ALERT_INIT_BOTTIN = 0;
	private static final int ALERT_LOADING = 1;
	protected static final String LOG_TAG = "BottinListActivity";

	/**
	 * Db cols to show in list
	 */
	private static final String[] DB_COLS = new String[] {
			BottinTableHelper.BOTTIN__ID, BottinTableHelper.BOTTIN_NOM,
			BottinTableHelper.BOTTIN_PRENOM, BottinTableHelper.BOTTIN_TIRE,
			BottinTableHelper.BOTTIN_SERVICE };

	private static final String[] SELECTION_ARGS = new String[] { "%", "%",
			"%", "%", "%", "%" };

	private Cursor allEntryCursor;
	// Handler uiHandler;

	private SimpleCursorAdapter simpleCursor;
	private TextView txtView;

	/**
	 * SERVICE
	 */
	private ServiceConnection connection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			new ManualFetcher().execute((BottinBinder) service);
		}
	};

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base_list);

		// uiHandler = new Handler();
		allEntryCursor = managedQuery(BottinContentProvider.CONTENT_URI,
				DB_COLS, null, SELECTION_ARGS, " nom ASC");
		// cursor adapter is faster
		simpleCursor = new SimpleCursorAdapter(this, R.layout.bottin_list_item,
				allEntryCursor, PROJECTION, TXT_VIEWS);

		simpleCursor.setFilterQueryProvider(new FilterQueryProvider() {

			@Override
			public Cursor runQuery(CharSequence constraint) {
				Log.d(LOG_TAG, "filter input  :" + constraint);
				return managedQuery(BottinContentProvider.CONTENT_URI, DB_COLS,
						null, new String[] { (String) constraint }, "nom ASC");
			}
		});
		setListAdapter(simpleCursor);
		getListView().setOnItemClickListener(this);

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
	protected void onPause() {
		try {
			unbindService(connection);
		} catch (IllegalArgumentException e) {
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		if (allEntryCursor.getCount() == 0) {
			showDialog(ALERT_INIT_BOTTIN);
			connectToFetcherService();
		} else {
			allEntryCursor = managedQuery(BottinContentProvider.CONTENT_URI,
					DB_COLS, null, null, "nom ASC");
			simpleCursor.notifyDataSetChanged();
		}
		super.onResume();
	}

	private void connectToFetcherService() {
		Intent i = new Intent(this, BottinService.class);
		if (!serviceIsRunning()) {
			startService(i);
		}
		bindService(i, connection, BIND_AUTO_CREATE);
	}

	private boolean serviceIsRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (service.service.getClassName().equals(SERVICE)) {
				return true;
			}
		}
		return false;
	}

	private class ManualFetcher extends AsyncTask<BottinBinder, Void, Void> {

		@Override
		protected void onPreExecute() {
			// footer.setVisibility(View.VISIBLE);
			// footer.startAnimation(showFooter());
			// footerVisible = true;
			showDialog(ALERT_LOADING);
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(BottinBinder... params) {
			BottinBinder binder = params[0];
			if (binder != null) {
				binder.startFetching();
				while (binder.isWorking()) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			try {
				// footer.startAnimation(hideFooter());
				// footerVisible = false;
				dismissDialog(ALERT_LOADING);
				unbindService(connection);
			} catch (IllegalArgumentException e) {
			}
			super.onPostExecute(result);
		}

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog d;
		switch (id) {
		case ALERT_INIT_BOTTIN:
			AlertDialog.Builder builder = new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setMessage(R.string.bottin_init_alert)
					.setTitle("Attention")
					.setPositiveButton(R.string.yes,
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {

									// new BottinLoader().execute();
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

		case ALERT_LOADING:
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
