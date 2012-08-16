package ca.etsmtl.applets.etsmobile;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.concurrent.ExecutionException;

import ca.etsmtl.applet.etsmobile.api.SignetBackgroundThread;
import ca.etsmtl.applet.etsmobile.api.SignetBackgroundThread.FetchType;
import ca.etsmtl.applets.etsmobile.models.CourseEvaluation;
import ca.etsmtl.applets.etsmobile.models.EvaluationElement;
import ca.etsmtl.applets.etsmobile.models.UserCredentials;
import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MyCourseDetailActivity extends Activity {
	
	private CourseEvaluation courseEvaluation = new CourseEvaluation();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_course_detail_view);
		
		UserCredentials creds = new UserCredentials(PreferenceManager.getDefaultSharedPreferences(this));
		
		final String session = getIntent().getExtras().getString("session");
		final String sigle = getIntent().getExtras().getString("sigle");
		final String coteFinale = getIntent().getExtras().getString("cote");
		final String groupe = getIntent().getExtras().getString("groupe");

		if (creds.getPassword() != null && creds.getUsername() != null && ! "".equals(creds.getPassword()) && ! "".equals(creds.getUsername())) {
			Dictionary<String, String> requestParams = new Hashtable<String, String>();
			requestParams.put("codeAccesUniversel", creds.getUsername());
			requestParams.put("motPasse", creds.getPassword());
			requestParams.put("pSigle", sigle);
			requestParams.put("pGroupe", groupe);
			requestParams.put("pSession", session);
			
			final SignetBackgroundThread<CourseEvaluation ,CourseEvaluation> signetBackgroundThead = 
					new SignetBackgroundThread<CourseEvaluation, CourseEvaluation>(
							"https://signets-ens.etsmtl.ca/Secure/WebServices/SignetsMobile.asmx", 
							"listeElementsEvaluation",
							requestParams,
							CourseEvaluation.class,
							FetchType.OBJECT);
			
			signetBackgroundThead.execute();
			
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						courseEvaluation = signetBackgroundThead.get();
						
						runOnUiThread(new Runnable() {
							public void run() {
								TextView txt1 = (TextView) findViewById(R.id.text1);
								TextView txt2 = (TextView) findViewById(R.id.text2);
								txt1.setText(coteFinale);
								txt2.setText(courseEvaluation.getNoteACeJour());
								
								ListView evaluationListView = (ListView) findViewById(R.id.evaluationListView);
								ArrayAdapter<EvaluationElement> myCoursesAdapter = new ArrayAdapter<EvaluationElement>(getApplicationContext(), android.R.layout.simple_list_item_1, courseEvaluation.getEvaluationElements());
								evaluationListView.setAdapter(myCoursesAdapter);
							}
						});
						
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}
				}
			}).start();
		} else {
			Toast.makeText(this, getString(R.string.usernamePasswordRequired), Toast.LENGTH_LONG).show();
		}
	}
}
