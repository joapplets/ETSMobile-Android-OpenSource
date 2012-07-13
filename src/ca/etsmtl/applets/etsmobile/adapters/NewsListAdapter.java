package ca.etsmtl.applets.etsmobile.adapters;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

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

public class NewsListAdapter extends ArrayAdapter<News>{

	private ArrayList<News> newsList;
	private Drawable webLogo, facebookLogo, twitterLogo;
	private Context c;
	private final String FACEBOOK = NewsListActivity.FACEBOOK;
	private final String RSS_ETS = NewsListActivity.RSS_ETS;
	private final String TWITTER = NewsListActivity.TWITTER;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMMMMMMMM yyyy");
	
	public NewsListAdapter(Context context, int resource, ArrayList<News> newsList) {
		super(context, resource, newsList);
		this.newsList = newsList;
		c = context;
		webLogo = context.getResources().getDrawable(R.drawable.news_web_logo);
		facebookLogo = context.getResources().getDrawable(R.drawable.news_facebook_logo);
		twitterLogo = context.getResources().getDrawable(R.drawable.news_twitter_logo);	
	}
	
    @Override
    public int getCount() {
        return newsList.size();
    }

    @Override
    public News getItem(int position) {
        return newsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		/**
		 * Méthode optimisée pour remplir le listview, je ne vais pas l'expliquer en détail
		 * car sur le net on peut trouver x1000 example, en plus du keynote google qui explique
		 * le pourquoi d'une version optimisée:
		 * 
		 * http://www.google.com/intl/fr-FR/events/io/2009/sessions/TurboChargeUiAndroidFast.html
		 */
		
		Holder holder;
		if(convertView == null){
			
			convertView = LayoutInflater.from(c).inflate(R.layout.news_list_item, null);
			
			holder = new Holder();
			holder.title = (TextView)convertView.findViewById(R.id.newsListItemTitle);
			holder.title.setSingleLine(true);
			holder.image = (TextView)convertView.findViewById(R.id.newsListItemLogo);
			holder.date = (TextView)convertView.findViewById(R.id.newsListItemDate);
			holder.description = (TextView)convertView.findViewById(R.id.newsListItemDescription);
			convertView.setTag(holder);
			
		}else{
			holder = (Holder)convertView.getTag();
		}
		
		String source = newsList.get(position).getSource();
		
		if(source.equals(RSS_ETS)){
			holder.image.setBackgroundDrawable(webLogo);
		}
		
		if(source.equals(FACEBOOK)){
			holder.image.setBackgroundDrawable(facebookLogo);
		}
		
		if(source.equals(TWITTER)){
			holder.image.setBackgroundDrawable(twitterLogo);
		}

		holder.title.setText(Html.fromHtml(newsList.get(position).getTitle()));
		holder.date.setText(dateFormat.format(newsList.get(position).getPubDate()));
		
		holder.description.setText(Html.fromHtml(newsList.get(position).getDescription()));
		
		String guid = newsList.get(position).getGuid();
		holder.guid = guid;
		
		return convertView;
	}
	
	public class Holder{
		TextView title, image, date, description;
		String guid;
		
		public String getGuid(){
			return guid;
		}
	}

}