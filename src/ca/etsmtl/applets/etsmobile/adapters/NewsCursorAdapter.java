/*******************************************************************************
 * Copyright 2013 Club ApplETS
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package ca.etsmtl.applets.etsmobile.adapters;

import java.text.SimpleDateFormat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.CursorAdapter;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import ca.etsmtl.applets.etsmobile.R;
import ca.etsmtl.applets.etsmobile.services.NewsService;
import ca.etsmtl.applets.etsmobile.tools.db.NewsTableHelper;

public class NewsCursorAdapter extends CursorAdapter {

	public static class ViewHolder {
		TextView title, date, description, logo;
	}

	private String source, title, description;
	private final Drawable webLogo, facebookLogo, twitterLogo, interfaceLogo;

	@SuppressLint("SimpleDateFormat")
	private final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"dd MMMMMMMMMM yyyy");

	public NewsCursorAdapter(final Context context, final Cursor c,
			final int flags) {
		super(context, c, flags);
		webLogo = context.getResources().getDrawable(
				R.drawable.news_background_ets);
		facebookLogo = context.getResources().getDrawable(
				R.drawable.news_background_facebookets);
		twitterLogo = context.getResources().getDrawable(
				R.drawable.news_background_twitterets);

		interfaceLogo = context.getResources().getDrawable(
				R.drawable.news_background_interfaceets);
	}

	@Override
	public void bindView(final View view, final Context context,
			final Cursor cursor) {

		ViewHolder holder = (ViewHolder) view
				.getTag(R.string.viewholdercontenttag);
		if (holder == null) {
			holder = new ViewHolder();
			holder.title = (TextView) view.findViewById(R.id.newsListItemTitle);
			holder.date = (TextView) view.findViewById(R.id.newsListItemDate);
			holder.description = (TextView) view
					.findViewById(R.id.newsListItemDescription);
			holder.logo = (TextView) view.findViewById(R.id.newsListItemLogo);
			view.setTag(R.string.viewholdercontenttag, holder);
		}

		title = cursor.getString(cursor
				.getColumnIndex(NewsTableHelper.NEWS_TITLE));
		holder.title.setText(Html.fromHtml(title));

		holder.date.setText(dateFormat.format(cursor.getLong(cursor
				.getColumnIndex(NewsTableHelper.NEWS_DATE))));

		description = cursor.getString(cursor
				.getColumnIndex(NewsTableHelper.NEWS_DESCRIPTION));
		if (description.length() > 200) {
			holder.description.setText(Html.fromHtml(description.substring(0,
					180)));
		} else {
			holder.description.setText(Html.fromHtml(description));
		}

		source = cursor.getString(cursor
				.getColumnIndex(NewsTableHelper.NEWS_SOURCE));
		if (source.equals(NewsService.RSS_ETS)) {
			holder.logo.setBackgroundDrawable(webLogo);
		}
		if (source.equals(NewsService.FACEBOOK)) {
			holder.logo.setBackgroundDrawable(facebookLogo);
		}
		if (source.equals(NewsService.TWITTER)) {
			holder.logo.setBackgroundDrawable(twitterLogo);
		}
		if (source.equals(NewsService.INTERFACE)) {
			holder.logo.setBackgroundDrawable(interfaceLogo);
		}

		view.setTag(R.string.viewholderidtag,
				cursor.getInt(cursor.getColumnIndex(NewsTableHelper.NEWS_ID)));
	}

	@Override
	public View newView(final Context context, final Cursor cursor,
			final ViewGroup parent) {
		final LayoutInflater inflater = LayoutInflater.from(context);
		final View v = inflater.inflate(R.layout.news_list_item, null);
		bindView(v, context, cursor);
		return v;
	}
}
