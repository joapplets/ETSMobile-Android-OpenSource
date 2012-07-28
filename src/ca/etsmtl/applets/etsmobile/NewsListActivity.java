package ca.etsmtl.applets.etsmobile;

import java.util.Calendar;
import java.util.TimeZone;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import ca.etsmtl.applets.etsmobile.fragments.NewsListFragment;
import ca.etsmtl.applets.etsmobile.listeners.NewsListSelectedItemListener;
import ca.etsmtl.applets.etsmobile.preferences.NewsListPreferences;
import ca.etsmtl.applets.etsmobile.receivers.NewsAlarmReceiver;
import ca.etsmtl.applets.etsmobile.services.NewsService;
import ca.etsmtl.applets.etsmobile.services.NewsService.NewsFetcherBinder;

public class NewsListActivity extends FragmentActivity implements NewsListSelectedItemListener, OnClickListener, AnimationListener{

	//private final static String TAG ="ca.etsmtl.applets.etsmobile.NewsListActivityV2";
	private final static String SERVICE = "ca.etsmtl.applets.etsmobile.services.NewsFetcher";
	private TextView footer;
	private boolean footerVisible = false;
	
	private ServiceConnection connection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			new ManualFetcher().execute((NewsFetcherBinder)service);
		}
	};
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.news_list_fragment);
		
		ImageButton btnHome = (ImageButton)findViewById(R.id.base_list_home_btn);
		Button btnSources = (Button)findViewById(R.id.base_list_source_btn);
		
		btnHome.setOnClickListener(this);
		btnSources.setOnClickListener(this);
		
		footer = (TextView)findViewById(R.id.listView_loading);
		
		setAlarm();
	}

	@Override
	protected void onResume() {
		NewsListFragment frag = (NewsListFragment)getSupportFragmentManager().
		  										  findFragmentById(R.id.newsList_fragment);

		frag.getListView().setVerticalFadingEdgeEnabled(true);
		frag.getListView().setFadingEdgeLength(12);
		frag.getLoaderManager().restartLoader(NewsListFragment.ID, null, frag);

		SharedPreferences prefs = getSharedPreferences("dbpref", MODE_PRIVATE);
		if(prefs.getBoolean("isEmpty", true)){
			connectToFetcherService();
		}
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		try {
			unbindService(connection);
		} catch (IllegalArgumentException e) {}
		super.onPause();
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.layout.news_list_menu, menu);
		return true;
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
	public void onItemClick(View v){
		
		// On crée un nouveau intent qui va nous permettre de lancer
		// la nouvelle activity
		
		Intent intent = new Intent(getApplicationContext(), SingleNewsActivity.class);
		intent.putExtra("id", (Integer)v.getTag());
		
		// On lance l'intent qui va créer la nouvelle activity.			
		startActivity(intent);
	}
	
	@Override
	public void onAnimationEnd(Animation animation) {
		if(!footerVisible){
			footer.setVisibility(View.GONE);
		}
	}

	@Override
	public void onAnimationRepeat(Animation animation) {}

	@Override
	public void onAnimationStart(Animation animation) {}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		
		switch (item.getItemId()) {
		case R.id.newsListMenuUpdate:
			connectToFetcherService();
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
		Animation animation = new TranslateAnimation(0, 0, 40, 0);
		animation.setDuration(500);
		animation.setFillEnabled(true);
		animation.setFillAfter(true);
		animation.setAnimationListener(this);
		footerVisible = true;
		return animation;
    }
	
	private boolean serviceIsRunning(){
    	ActivityManager manager = (ActivityManager)getSystemService(ACTIVITY_SERVICE);
    	for(RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
    		if(service.service.getClassName().equals(SERVICE)){
    			return true;		
    		}
    	}
    	return false;
	}
	
	private void connectToFetcherService(){
		Intent i = new Intent(this, NewsService.class);
		if(!serviceIsRunning()){
			startService(i);
		}
		bindService(i, connection, BIND_AUTO_CREATE);
	}
	
	private class ManualFetcher extends AsyncTask<NewsFetcherBinder, Void, Void>{

		@Override
		protected void onPreExecute() {
			footer.setVisibility(View.VISIBLE);
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
			}catch(IllegalArgumentException e){}
			super.onPostExecute(result);
		}
		
	}

		
}
