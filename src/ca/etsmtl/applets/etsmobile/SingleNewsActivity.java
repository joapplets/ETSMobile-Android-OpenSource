package ca.etsmtl.applets.etsmobile;

import java.text.SimpleDateFormat;
import java.util.Date;

import ca.etsmtl.applets.etsmobile.models.News;
import ca.etsmtl.applets.etsmobile.tools.db.NewsDB;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class SingleNewsActivity extends Activity{

	private TextView title, dateView,description;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	private Date date = new Date();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// On set le layout
		setContentView(R.layout.news_view);
		title = (TextView)findViewById(R.id.newsViewTitle);
		dateView = (TextView)findViewById(R.id.newsViewDate);
		description = (TextView)findViewById(R.id.newsViewDescription);
		description.setMovementMethod(new ScrollingMovementMethod());
		
		// On va chercher les param passés par le bundle et on associe
		// les valeurs aux champs respectifs.
		Bundle bundle = getIntent().getExtras();
		
		NewsDB newsDB = NewsDB.getInstance(this);
		News n = newsDB.getNewsByGUID((String) bundle.getCharSequence("guid"));
		
		if(n != null){
			title.setText(Html.fromHtml(n.getTitle()));
			date.setTime(n.getPubDate());
			dateView.setText(dateFormat.format(date));
			description.setText(Html.fromHtml(n.getDescription()));
		}else{
			description.setText("NEWS NON TROUVÉE");
		}
		
	}
	
}
