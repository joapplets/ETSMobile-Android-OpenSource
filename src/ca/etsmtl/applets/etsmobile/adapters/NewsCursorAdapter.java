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
import ca.etsmtl.applets.etsmobile.services.NewsService;
import ca.etsmtl.applets.etsmobile.tools.db.NewsTableHelper;

public class NewsCursorAdapter extends CursorAdapter{

	private String source, title, description;
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
	
		ViewHolder holder = (ViewHolder) view.getTag(R.string.viewholdercontenttag);
		if(holder == null){
			holder = new ViewHolder();
			holder.title = (TextView)view.findViewById(R.id.newsListItemTitle);
			holder.date = (TextView)view.findViewById(R.id.newsListItemDate);
			holder.description = (TextView)view.findViewById(R.id.newsListItemDescription);
			holder.logo = (TextView)view.findViewById(R.id.newsListItemLogo);
			view.setTag(R.string.viewholdercontenttag, holder);
		}
		
		title = cursor.getString(cursor.getColumnIndex(NewsTableHelper.NEWS_TITLE));
		holder.title.setText(Html.fromHtml(title));
		
		holder.date.setText(dateFormat.format(cursor.getLong(cursor.getColumnIndex(NewsTableHelper.NEWS_DATE))));
		
		description = cursor.getString(cursor.getColumnIndex(NewsTableHelper.NEWS_DESCRIPTION));
		if(description.length() > 200){
			holder.description.setText(Html.fromHtml(description.substring(0, 199)));	
		}else{
			holder.description.setText(Html.fromHtml(description));
		}
		
		source = cursor.getString(cursor.getColumnIndex(NewsTableHelper.NEWS_SOURCE));
		if(source.equals(NewsService.RSS_ETS)){
			holder.logo.setBackgroundDrawable(webLogo);
		}
		if(source.equals(NewsService.FACEBOOK)){
			holder.logo.setBackgroundDrawable(facebookLogo);
		}
		if(source.equals(NewsService.TWITTER)){
			holder.logo.setBackgroundDrawable(twitterLogo);
		}
		
		view.setTag(R.string.viewholderidtag, cursor.getInt(cursor.getColumnIndex(NewsTableHelper.NEWS_ID)));
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.news_list_item, null);
		bindView(v, context, cursor);
		return v;
	}
	
	public static class ViewHolder{
		TextView title, date, description, logo;
	}
}
