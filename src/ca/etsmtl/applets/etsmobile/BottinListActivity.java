package ca.etsmtl.applets.etsmobile;

import java.lang.ref.WeakReference;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AlphabetIndexer;
import android.widget.FilterQueryProvider;
import android.widget.SectionIndexer;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import ca.etsmtl.applets.etsmobile.services.BottinService;
import ca.etsmtl.applets.etsmobile.services.BottinService.BottinBinder;
import ca.etsmtl.applets.etsmobile.tools.db.BottinEntryDataSource;
import ca.etsmtl.applets.etsmobile.tools.db.BottinTableHelper;

public class BottinListActivity extends ListActivity implements TextWatcher,
		OnItemClickListener {

	private class ManualFetcher extends AsyncTask<BottinBinder, Void, Void> {

		private final WeakReference<BottinListActivity> ref;

		public ManualFetcher(WeakReference<BottinListActivity> ref) {
			this.ref = ref;
		}

		@Override
		protected Void doInBackground(final BottinBinder... params) {
			final BottinBinder binder = params[0];
			if (binder != null) {
				binder.startFetching();
				while (binder.isWorking()) {
					try {
						Thread.sleep(1000);
					} catch (final InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(final Void result) {
			try {
				unbindService(connection);
				dismissDialog(BottinListActivity.ALERT_LOADING);

				final BottinListActivity act = ref.get();

				if (act != null && !act.isFinishing()) {

					act.setListAdapter(new MyCursorAdapter(act,
							act.bottinEntryDataSource.getAllAsCursor(),
							BottinEntryDataSource.allColumns, TXT_VIEWS));
				}

			} catch (final IllegalArgumentException e) {
			}
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			showDialog(BottinListActivity.ALERT_LOADING);
			super.onPreExecute();
		}

	}

	private class MyCursorAdapter extends SimpleCursorAdapter implements
			SectionIndexer {

		private final AlphabetIndexer mAlphabetIndexer;

		public MyCursorAdapter(Context context, Cursor cursor,
				String[] strings, int[] is) {
			super(context, R.layout.bottin_list_item, cursor, strings, is);

			mAlphabetIndexer = new AlphabetIndexer(cursor,
					cursor.getColumnIndex(BottinTableHelper.BOTTIN_NOM),
					"ABCDEFGHIJKLMNOPQRTSUVWXYZ");
			mAlphabetIndexer.setCursor(cursor);// Sets a new cursor as the data
			// set and resets the cache of
			// indices.

		}

		/**
		 * Bind an existing view to the data pointed to by cursor
		 */
		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			final TextView txtView = (TextView) view
					.findViewById(android.R.id.text1);
			final String nom = cursor.getString(cursor.getColumnIndex("nom"));
			final String prenom = cursor.getString(cursor
					.getColumnIndex("prenom"));
			txtView.setText(nom + ", " + prenom);
		}

		/**
		 * Performs a binary search or cache lookup to find the first row that
		 * matches a given section's starting letter.
		 */
		@Override
		public int getPositionForSection(int sectionIndex) {
			return mAlphabetIndexer.getPositionForSection(sectionIndex);
		}

		/**
		 * Returns the section index for a given position in the list by
		 * querying the item and comparing it with all items in the section
		 * array.
		 */
		@Override
		public int getSectionForPosition(int position) {
			return mAlphabetIndexer.getSectionForPosition(position);
		}

		/**
		 * Returns the section array constructed from the alphabet provided in
		 * the constructor.
		 */
		@Override
		public Object[] getSections() {
			return mAlphabetIndexer.getSections();
		}

		/**
		 * Makes a new view to hold the data pointed to by cursor.
		 */
		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
			final LayoutInflater inflater = LayoutInflater.from(context);
			final View newView = inflater.inflate(
					android.R.layout.simple_list_item_1, parent, false);
			return newView;
		}

		@Override
		public void changeCursor(Cursor cursor) {

			mAlphabetIndexer.setCursor(cursor);
			super.changeCursor(cursor);

		}
	}

	private final static String SERVICE = "ca.etsmtl.applets.etsmobile.services.BottinFetcher";
	/**
	 * SimpleCursorAdapter INFO
	 */

	private static final int[] TXT_VIEWS = new int[] {
			R.id.bottin_list_item_nom, R.id.bottin_list_item_prenom };
	/**
	 * Dialogs
	 */
	private static final int ALERT_INIT_BOTTIN = 0;
	private static final int ALERT_LOADING = 1;

	protected static final String LOG_TAG = "BottinListActivity";

	// Handler uiHandler;
	private SimpleCursorAdapter simpleCursorAdapter;
	private TextView txtView;

	/**
	 * SERVICE
	 */
	private final ServiceConnection connection = new ServiceConnection() {

		@Override
		public void onServiceConnected(final ComponentName name,
				final IBinder service) {
			new ManualFetcher(new WeakReference<BottinListActivity>(
					BottinListActivity.this)).execute((BottinBinder) service);
		}

		@Override
		public void onServiceDisconnected(final ComponentName name) {
		}
	};

	private BottinEntryDataSource bottinEntryDataSource;
	private int count;

	@Override
	public void afterTextChanged(final Editable s) {
	}

	@Override
	public void beforeTextChanged(final CharSequence s, final int start,
			final int count, final int after) {
	}

	private void connectToFetcherService() {
		final Intent i = new Intent(this, BottinService.class);
		if (!serviceIsRunning()) {
			startService(i);
		}
		bindService(i, connection, Context.BIND_AUTO_CREATE);
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base_list);

		getListView().setOnItemClickListener(this);

		// init textview with filter options
		txtView = (TextView) findViewById(R.id.search_nav_bar_autotxt);
		txtView.addTextChangedListener(this);

		bottinEntryDataSource = new BottinEntryDataSource(
				getApplicationContext());

		final Cursor allAsCursor = bottinEntryDataSource
				.getAllAsCursor(BottinEntryDataSource.allColumns);
		allAsCursor.moveToFirst();
		count = allAsCursor.getCount();
		simpleCursorAdapter = new MyCursorAdapter(this, allAsCursor,
				BottinEntryDataSource.allColumns, TXT_VIEWS);

		if (count == 0) {
			showDialog(BottinListActivity.ALERT_INIT_BOTTIN);
		} else {

			simpleCursorAdapter = new MyCursorAdapter(this, allAsCursor,
					BottinEntryDataSource.allColumns, TXT_VIEWS);

			simpleCursorAdapter
					.setFilterQueryProvider(new FilterQueryProvider() {

						@Override
						public Cursor runQuery(final CharSequence constraint) {

							String where = null;
							String[] args = new String[BottinEntryDataSource.allColumns.length - 1];
							if (constraint != "") {
								for (int i = 0; i < args.length; i++) {
									args[i] = "%" + (String) constraint + "%";
								}
								where = "nom LIKE ? OR prenom LIKE ?";
							} else {
								args = null;
							}

							return bottinEntryDataSource.getAllAsCursor(where,
									args);
						}
					});

			setListAdapter(simpleCursorAdapter);
		}

	}

	@Override
	protected Dialog onCreateDialog(final int id) {
		Dialog d;
		switch (id) {
		case ALERT_INIT_BOTTIN:
			final AlertDialog.Builder builder = new AlertDialog.Builder(this)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setMessage(R.string.bottin_init_alert)
					.setTitle("Attention")
					.setPositiveButton(R.string.yes,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(
										final DialogInterface dialog,
										final int which) {

									connectToFetcherService();
									dismissDialog(BottinListActivity.ALERT_INIT_BOTTIN);
								}
							})
					.setNegativeButton(R.string.no,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(
										final DialogInterface dialog,
										final int which) {
									dialog.cancel();
									dialog.dismiss();
									unbindService(connection);
								}
							});
			d = builder.create();
			break;

		case ALERT_LOADING:
			d = new ProgressDialog(this);
			d.setTitle(R.string.loading);
			d.setCancelable(true);
			break;
		default:
			d = null;
			break;
		}
		return d;
	}

	@Override
	public void onItemClick(final AdapterView<?> vc, final View view,
			final int position, final long id) {

		final Intent intent = new Intent(getApplicationContext(),
				BottinViewActivity.class);
		intent.putExtra("id", id);

		startActivity(intent);
	}

	@Override
	protected void onPause() {
		try {
			unbindService(connection);
		} catch (final IllegalArgumentException e) {
		}
		super.onPause();
	}

	@Override
	public void onTextChanged(CharSequence s, final int start,
			final int before, final int count) {
		if (simpleCursorAdapter != null) {
			if (s == "") {
				s = "%";
			}
			simpleCursorAdapter.getFilter().filter(s);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.calendar_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.calendar_force_update:
			showDialog(BottinListActivity.ALERT_INIT_BOTTIN);
			break;

		default:
			break;
		}
		return true;
	}

	private boolean serviceIsRunning() {
		final ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (final RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (service.service.getClassName().equals(
					BottinListActivity.SERVICE)) {
				return true;
			}
		}
		return false;
	}
}
