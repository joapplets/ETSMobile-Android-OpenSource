package ca.etsmtl.applets.etsmobile.adapters;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ca.etsmtl.applets.etsmobile.NewsListActivity;
import ca.etsmtl.applets.etsmobile.R;
import ca.etsmtl.applets.etsmobile.models.News;

public class NewsListAdapter extends ArrayAdapter<News> {

	public class Holder {
		String guid;
		TextView title, image, date, description;

		public String getGuid() {
			return guid;
		}
	}

	private final Context c;
	private final Date date = new Date();
	private final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"dd MMMMMMMMMM yyyy");
	private final String FACEBOOK = NewsListActivity.FACEBOOK;
	private final ArrayList<News> newsList;
	private final String RSS_ETS = NewsListActivity.RSS_ETS;
	private final String TWITTER = NewsListActivity.TWITTER;

	private final Drawable webLogo, facebookLogo, twitterLogo;

	public NewsListAdapter(final Context context, final int resource,
			final ArrayList<News> newsList) {
		super(context, resource, newsList);
		this.newsList = newsList;
		c = context;
		webLogo = context.getResources().getDrawable(R.drawable.news_web_logo);
		facebookLogo = context.getResources().getDrawable(
				R.drawable.news_facebook_logo);
		twitterLogo = context.getResources().getDrawable(
				R.drawable.news_twitter_logo);

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
			holder.title.setSingleLine(true);
			holder.image = (TextView) convertView
					.findViewById(R.id.newsListItemLogo);
			holder.date = (TextView) convertView
					.findViewById(R.id.newsListItemDate);
			holder.description = (TextView) convertView
					.findViewById(R.id.newsListItemDescription);
			convertView.setTag(holder);

		} else {
			holder = (Holder) convertView.getTag();
		}

		final String source = newsList.get(position).getSource();

		if (source.equals(RSS_ETS)) {
			holder.image.setBackgroundDrawable(webLogo);
		}

		if (source.equals(FACEBOOK)) {
			holder.image.setBackgroundDrawable(facebookLogo);
		}

		if (source.equals(TWITTER)) {
			holder.image.setBackgroundDrawable(twitterLogo);
		}

		date.setTime(newsList.get(position).getPubDate());

		holder.title.setText(Html.fromHtml(newsList.get(position).getTitle()));
		holder.date.setText(dateFormat.format(date));

		holder.description.setText(Html.fromHtml(newsList.get(position)
				.getDescription()));

		final String guid = newsList.get(position).getGuid();
		holder.guid = guid;

		return convertView;
	}

}