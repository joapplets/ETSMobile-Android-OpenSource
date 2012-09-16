package ca.etsmtl.applets.etsmobile.views;

import ca.etsmtl.applets.etsmobile.ETSMobileActivity;
import ca.etsmtl.applets.etsmobile.R;
import ca.etsmtl.applets.etsmobile.CalendarActivity;
import ca.etsmtl.applets.etsmobile.ctrls.CourseListAdapter;
import ca.etsmtl.applets.etsmobile.utils.CourseList;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


public class CourseActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        
        LinearLayout linearLayoutCourse = new LinearLayout(this.getApplicationContext());
        linearLayoutCourse.setOrientation(LinearLayout.VERTICAL);
        
        TextView textView = new TextView(this.getApplicationContext());
    	textView.setText("Cours");
    	textView.setTextSize(35);
    	linearLayoutCourse.addView(textView);
    	
    	View lineDivider1 = new View(this.getApplicationContext());
        lineDivider1.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 1 ));
        lineDivider1.setBackgroundColor(Color.WHITE);
        linearLayoutCourse.addView(lineDivider1);
    
        CourseListAdapter courseListAdapter = new CourseListAdapter(this.getApplicationContext());
        ListView listView = new ListView(this.getApplicationContext());
        listView.setAdapter(courseListAdapter);
        listView.setScrollbarFadingEnabled(false);
        
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

        	public void onItemClick(AdapterView<?> parentView, View childeView, int position,
        			long id) {
        		//call eventDetailActivity...
        		//ici on appelle une nouvelle activity pour affichier les details de l'evenement
        		Log.v("onListViewClick_Course", "symbol:" + CourseList.getInstance().getItem(position).getCourseSymbol());
        		
        		Intent cours = new Intent(CourseActivity.this, CourseDetailsActivity.class);
        		cours.putExtra("COURS_SYMBOL", CourseList.getInstance().getItem(position).getCourseSymbol());
    	    	startActivity(cours);
    	    	
        	}
		});
        
        linearLayoutCourse.addView(listView);
        setContentView(linearLayoutCourse);

    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_coursactivity, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.return_menu:
            	Intent menuActivity = new Intent(CourseActivity.this, ETSMobileActivity.class);
    	    	startActivity(menuActivity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
}
