package com.applets.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.applets.R;
import com.applets.models.News;

public class NewsListAdapter extends ArrayAdapter<News> {

	public class Holder {
		String guid;
		TextView title, image;

		public String getGuid() {
			return guid;
		}
	}

	private final Context c;
	private final Drawable facebookLogo;
	private final ArrayList<News> newsList;

	private final Drawable webLogo;

	public NewsListAdapter(final Context context, final int resource,
			final ArrayList<News> newsList) {
		super(context, resource, newsList);
		this.newsList = newsList;
		c = context;
		webLogo = context.getResources().getDrawable(R.drawable.news_web_logo);
		facebookLogo = context.getResources().getDrawable(
				R.drawable.news_facebook_logo);
	}

	@Override
	public int getCount() {
		return newsList.size();
	}

	@Override
	public News getItem(final int position) {
		return newsList.get(position);
	}

	@Override
	public long getItemId(final int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView,
			final ViewGroup parent) {

		/**
		 * Méthode optimisée pour remplir le listview, je ne vais pas
		 * l'expliquer en détail car sur le net on peut trouver x1000 example,
		 * en plus du keynote google qui explique le pourquoi d'une version
		 * optimisée:
		 * 
		 * http://www.google.com/intl/fr-FR/events/io/2009/sessions/
		 * TurboChargeUiAndroidFast.html
		 */

		Holder holder;
		if (convertView == null) {

			convertView = LayoutInflater.from(c).inflate(
					R.layout.news_list_item, null);

			holder = new Holder();
			holder.title = (TextView) convertView
					.findViewById(R.id.newsListItemTitle);
			holder.image = (TextView) convertView
					.findViewById(R.id.newsListItemLogo);
			convertView.setTag(holder);

		} else {
			holder = (Holder) convertView.getTag();
		}

		final String source = newsList.get(position).getSource();

		if (source.equals("rssETS")) {
			holder.image.setBackgroundDrawable(webLogo);
		}

		if (source.equals("facebook")) {
			holder.image.setBackgroundDrawable(facebookLogo);
		}

		String titleString = newsList.get(position).getTitle();

		if (titleString.length() > 68) {
			titleString = titleString.substring(0, 67) + " ...";
		}

		holder.title.setText(Html.fromHtml(titleString));

		final String guid = newsList.get(position).getGuid();
		holder.guid = guid;

		return convertView;
	}

}