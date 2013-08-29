package ca.etsmtl.applets.etsmobile.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.widget.ListView;
import ca.etsmtl.applets.etsmobile.adapters.NewsCursorAdapter;
import ca.etsmtl.applets.etsmobile.listeners.NewsListSelectedItemListener;
import ca.etsmtl.applets.etsmobile.providers.ETSMobileContentProvider;
import ca.etsmtl.applets.etsmobile.services.NewsService;
import ca.etsmtl.applets.etsmobile.tools.db.NewsTableHelper;

public class NewsListFragment extends ListFragment implements
		LoaderManager.LoaderCallbacks<Cursor> {

	// private final static String TAG =
	// "ca.etsmtl.applets.etsmobile.fragments.NewsListFragment";
	private NewsListSelectedItemListener selectedItemListener;
	private NewsCursorAdapter adapter;

	public static final int ID = 1;

	@Override
	public void onAttach(final Activity activity) {
		super.onAttach(activity);
		try {
			selectedItemListener = (NewsListSelectedItemListener) activity;
		} catch (final ClassCastException e) {
			throw new ClassCastException(
					activity.toString()
							+ "l'activité doit implémenter l'interface NewsListSelectedItemListener");
		}
	}

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getLoaderManager().initLoader(NewsListFragment.ID, null, this);
		adapter = new NewsCursorAdapter(getActivity().getApplicationContext(),
				null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		setListAdapter(adapter);
	}

	@Override
	public Loader<Cursor> onCreateLoader(final int id, final Bundle bundle) {
		final String[] projection = { NewsTableHelper.NEWS_ID,
				NewsTableHelper.NEWS_GUID, NewsTableHelper.NEWS_TITLE,
				NewsTableHelper.NEWS_DATE, NewsTableHelper.NEWS_DESCRIPTION,
				NewsTableHelper.NEWS_SOURCE };

		final SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getActivity()
						.getApplicationContext());
		final ArrayList<String> source = new ArrayList<String>();
		if (prefs.getBoolean("rssETS", true)) {
			source.add(NewsService.RSS_ETS);
		}
		if (prefs.getBoolean("facebook", true)) {
			source.add(NewsService.FACEBOOK);
		}
		if (prefs.getBoolean("twitter", true)) {
			source.add(NewsService.TWITTER);
		}
		if (prefs.getBoolean("interface", true)) {
			source.add(NewsService.INTERFACE);
		}

		final String[] s = new String[source.size()];
		for (int i = 0; i < source.size(); i++) {
			s[i] = source.get(i);
		}

		final CursorLoader cursorLoader = new CursorLoader(

		getActivity(), ETSMobileContentProvider.CONTENT_URI_NEWS, projection,
				null, s, NewsTableHelper.NEWS_DATE + " DESC");

		return cursorLoader;
	}

	@Override
	public void onDestroy() {
		getLoaderManager().destroyLoader(NewsListFragment.ID);
		super.onDestroy();
	}

	@Override
	public void onListItemClick(final ListView l, final View v,
			final int position, final long id) {
		selectedItemListener.onItemClick(v);
	}

	@Override
	public void onLoaderReset(final Loader<Cursor> loader) {
		adapter.swapCursor(null);
	}

	@Override
	public void onLoadFinished(final Loader<Cursor> loader, final Cursor cursor) {
		adapter.swapCursor(cursor);
	}

}
