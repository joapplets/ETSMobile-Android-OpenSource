package ca.etsmtl.applets.etsmobile.adapters;

import java.text.SimpleDateFormat;

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
import ca.etsmtl.applets.etsmobile.services.NewsFetcher;
import ca.etsmtl.applets.etsmobile.tools.db.NewsTable;

public class NewsCursorAdapter extends CursorAdapter{


	private TextView tempV;
	private String source;
	private Drawable webLogo, facebookLogo, twitterLogo;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMMMMMMMM yyyy");
	
	public NewsCursorAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
		webLogo = context.getResources().getDrawable(R.drawable.news_background_ets);
		facebookLogo = context.getResources().getDrawable(R.drawable.news_background_facebookets);
		twitterLogo = context.getResources().getDrawable(R.drawable.news_background_twitterets);	
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
	
		//title
		tempV = (TextView)view.findViewById(R.id.newsListItemTitle);
		tempV.setText(Html.fromHtml(cursor.getString(cursor.getColumnIndex(NewsTable.NEWS_TITLE))));
		
		//date TODO parser la date dans le bon format
		tempV = (TextView)view.findViewById(R.id.newsListItemDate);
		tempV.setText(dateFormat.format(cursor.getLong(cursor.getColumnIndex(NewsTable.NEWS_DATE))));
		
		//description
		tempV = (TextView)view.findViewById(R.id.newsListItemDescription);
		tempV.setText(Html.fromHtml(cursor.getString(cursor.getColumnIndex(NewsTable.NEWS_DESCRIPTION))));
		
		//logo
		tempV = (TextView)view.findViewById(R.id.newsListItemLogo);
		source = cursor.getString(cursor.getColumnIndex(NewsTable.NEWS_SOURCE));
		if(source.equals(NewsFetcher.RSS_ETS)){
			tempV.setBackgroundDrawable(webLogo);
		}
		if(source.equals(NewsFetcher.FACEBOOK)){
			tempV.setBackgroundDrawable(facebookLogo);
		}
		if(source.equals(NewsFetcher.TWITTER)){
			tempV.setBackgroundDrawable(twitterLogo);
		}
		
		//guid
		view.setTag(cursor.getInt(cursor.getColumnIndex(NewsTable.NEWS_ID)));
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.news_list_item, null);
		bindView(v, context, cursor);
		return v;
	}

}
