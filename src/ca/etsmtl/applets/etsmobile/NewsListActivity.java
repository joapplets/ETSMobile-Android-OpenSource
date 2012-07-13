package ca.etsmtl.applets.etsmobile;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;
import java.util.TimeZone;

import ca.etsmtl.applets.etsmobile.adapters.NewsListAdapter;
import ca.etsmtl.applets.etsmobile.adapters.NewsListAdapter.Holder;
import ca.etsmtl.applets.etsmobile.models.News;
import ca.etsmtl.applets.etsmobile.preferences.NewsListPreferences;
import ca.etsmtl.applets.etsmobile.receivers.NewsAlarmReceiver;
import ca.etsmtl.applets.etsmobile.services.NewsFetcher;
import ca.etsmtl.applets.etsmobile.services.NewsFetcher.NewsFetcherBinder;
import ca.etsmtl.applets.etsmobile.tools.db.NewsAdapter;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

public class NewsListActivity extends Activity implements AnimationListener, OnClickListener, Observer{
	
	public final static String RSS_ETS = "rssETS";
	public final static String FACEBOOK = "facebook";
	public final static String TWITTER = "twitter";
	
	private final static String SERVERSERVICE = "ca.etsmtl.applets.etsmobile.services.NewsFetcher";
	
	private NewsListAdapter newsAdapter;
	private NewsAdapter newsDB = null;
	private ArrayList<News> newsArray;
	private ListView listView = null;
	private boolean footerVisible = false;
	private Handler handler;
	private boolean binded = false;
	private LinearLayout footer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.news_list_view);
		newsDB = NewsAdapter.getInstance(this);
		newsDB.addObserver(this);
		handler = new Handler();
		newsArray = newsDB.getAllNews();
		initializeListView();
		setAlarm();
		//fillData();

		if(newsArray.size() == 0){
			Intent i = new Intent(this, NewsFetcher.class);
			startService(i);
			binded = bindService(i, connection, BIND_AUTO_CREATE);
		}
		
		ImageButton btnHome = (ImageButton)findViewById(R.id.base_list_home_btn);
		btnHome.setOnClickListener(this);
		
		Button btnSources = (Button)findViewById(R.id.base_list_source_btn);
		btnSources.setOnClickListener(this);
		
		footer = (LinearLayout)findViewById(R.id.listView_footer);
	}
	
	private void setAlarm() {
		Intent toAlarm = new Intent(this, NewsAlarmReceiver.class);
		PendingIntent toDownload = PendingIntent.getBroadcast(this, 0, toAlarm, PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager alarms = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		
		Calendar updateTime = Calendar.getInstance();
	    updateTime.setTimeZone(TimeZone.getTimeZone("GMT"));
	    
	    updateTime.set(Calendar.HOUR_OF_DAY, 6);
	    updateTime.set(Calendar.MINUTE, 00);
	    alarms.setInexactRepeating(AlarmManager.RTC_WAKEUP, updateTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, toDownload);
	    
	    updateTime.set(Calendar.HOUR_OF_DAY, 12);
	    alarms.setInexactRepeating(AlarmManager.RTC_WAKEUP, updateTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, toDownload);
	    
	    updateTime.set(Calendar.HOUR_OF_DAY, 18);
	    alarms.setInexactRepeating(AlarmManager.RTC_WAKEUP, updateTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, toDownload);
	}

	@Override
	protected void onResume() {
		refreshListView();
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		if(binded){
			unbindService(connection);
		}
		if(footerVisible){
			footer.startAnimation(hideFooter());
		}
		super.onPause();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.layout.news_list_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		
		switch (item.getItemId()) {
		case R.id.newsListMenuUpdate:
			Intent i = new Intent(this, NewsFetcher.class);
			if(!serviceIsRunning()){
				startService(i);
			}
			binded = bindService(i, connection, BIND_AUTO_CREATE);
			break;
		case R.id.newsListMenuPreferences:
			intent = new Intent(getApplicationContext(), NewsListPreferences.class);
			break;
		default:
			break;
		}
		
		if(intent != null){
			startActivity(intent);
		}
		
		return true;
	}
	
    private void initializeListView(){
    		
		// On va chercher la listView dans le layout
		listView = (ListView)findViewById(R.id.listView);
		
		// On crée l'adapter qui permets de remplir la listview de nouvelles
		newsAdapter = new NewsListAdapter(getApplicationContext(), R.layout.news_list_item, newsArray);
		
		// On lui associe un adapter
		listView.setAdapter(newsAdapter);
		
		// Ce qui arrive quand on click sur les éléments...
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				
				// On crée un nouveau intent qui va nous permettre de lancer
				// la nouvelle activity
				Intent intent = new Intent(getApplicationContext(), SingleNewsActivity.class);
				
				Holder holder = (Holder)arg1.getTag();
				intent.putExtra("guid", holder.getGuid());
				
				// On lance l'intent qui va créer la nouvelle activity.			
				startActivity(intent);
				
			}
		});
    }

    private synchronized void refreshListView(){
		if(newsAdapter != null){
			handler.post(new Runnable() {	
				@Override
				public void run() {
					newsArray.clear();
					newsArray.addAll(newsDB.getAllNews());
					newsAdapter.notifyDataSetChanged();
				}
			});
		}
    }
    
    private Animation hideFooter(){
		Animation animation = new TranslateAnimation(0, 0, 0, 40);
		animation.setDuration(500);
		animation.setFillEnabled(true);
		animation.setFillAfter(true);
		animation.setAnimationListener(this);
		footerVisible = false;
		return animation;
    }
    
    private Animation showFooter(){
    	LinearLayout footer = (LinearLayout)findViewById(R.id.listView_footer);
		footer.setVisibility(View.VISIBLE);
		Animation animation = new TranslateAnimation(0, 0, 40, 0);
		animation.setDuration(500);
		animation.setFillEnabled(true);
		animation.setFillAfter(true);
		animation.setAnimationListener(this);
		footerVisible = true;
		return animation;
    }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.base_list_home_btn:
			finish();
			break;
		case R.id.base_list_source_btn:
			Intent intent = new Intent(getApplicationContext(), NewsListPreferences.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onAnimationRepeat(Animation arg0) {}

	@Override
	public void onAnimationStart(Animation arg0) {}

	@Override
	public void onAnimationEnd(Animation arg0) {
		if(!footerVisible){
	    	LinearLayout footer = (LinearLayout)findViewById(R.id.listView_footer);
			footer.setVisibility(View.GONE);
		}
	}

	@Override
	public void update(Observable observable, Object data) {
		if(data instanceof String){
			String message = (String)data;
			
			if(message.equals("News db updated")){
				refreshListView();
			}
		}
	}

	private boolean serviceIsRunning(){
    	ActivityManager manager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
    	for(RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
    		if(service.service.getClassName().equals(SERVERSERVICE)){
    			return true;		
    		}
    	}
    	return false;
	}
	
	private ServiceConnection connection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			new ForceFetch().execute((NewsFetcherBinder)service);
		}
	};
	
	private class ForceFetch extends AsyncTask<NewsFetcherBinder, Void, Void>{

		@Override
		protected void onPreExecute() {
			footer.startAnimation(showFooter());
			footerVisible = true;
			super.onPreExecute();
		}
		
		@Override
		protected Void doInBackground(NewsFetcherBinder... params) {
			NewsFetcherBinder binder = params[0];
			if(binder != null){
				binder.startFetching();
				while(binder.isWorking()){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {}
				}
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			try{
				footer.startAnimation(hideFooter());
				footerVisible = false;
				unbindService(connection);
				binded = false;
			}catch(IllegalArgumentException e){}
			super.onPostExecute(result);
		}
		
	}
}
