package ca.etsmtl.applets.etsmobile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import ca.etsmtl.applets.etsmobile.models.StudentProfile;
import ca.etsmtl.applets.etsmobile.tools.xml.XMLUserProfileParser;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

public class StudentProfileActivity extends Activity{

	private TextView nom, prenom, solde, codePermanent;
	
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.student_profile);
		
		nom = (TextView)findViewById(R.id.student_profile_lastname);
		prenom = (TextView)findViewById(R.id.student_profile_name);
		solde = (TextView)findViewById(R.id.student_profile_solde);
		codePermanent = (TextView)findViewById(R.id.student_profile_codePermanent);
		
		new UserProfileLoader().execute();
	}
	
	private class UserProfileLoader extends AsyncTask<Void, Void, InputStream>{

		OutputStreamWriter writer;
		
		@Override
		protected InputStream doInBackground(Void... params) {
			
			try {
				String data = 
					"<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
					"<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
					"<soap:Body>" +
					"<listeHoraireEtProf xmlns=\"http://etsmtl.ca/\">" +
					"<codeAccesUniversel>aj73330</codeAccesUniversel>" +
				    "<motPasse>76624</motPasse>" +
				    "<pSession>H2011</pSession>" +
				    "</listeHoraireEtProf>" +
				    "</soap:Body>" +
				    "</soap:Envelope>";
	            
				URL url = new URL("https://signets-ens.etsmtl.ca/Secure/WebServices/SignetsMobile.asmx");
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	            conn.setDoOutput(true);
	            conn.setRequestMethod("POST");
	            conn.setRequestProperty("Host", "signets-ens.etsmtl.ca");
	            conn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
	            conn.setRequestProperty("SOAPAction", "\"http://etsmtl.ca/listeHoraireEtProf\"");
	            
	            writer = new OutputStreamWriter(conn.getOutputStream());
	            
	            //write parameters
	            writer.write(data);
	            writer.flush();
	            
	            return conn.getInputStream();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(InputStream stream) {
			if(stream != null){
				
//				ByteArrayOutputStream bos= new ByteArrayOutputStream();
//				byte[] buffer = new byte[1024];
//				int i = 0;
//				try {
//					while((i = stream.read(buffer)) != -1){
//						bos.write(buffer, 0, i);
//					}
//					
//					buffer = bos.toByteArray();
//					String s = "";
//					for (byte b : buffer) {
//						s += (char)b;
//					}
//					System.out.println(s);
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
				
//				try {
//					SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
//					XMLUserProfileParser studentParser = new XMLUserProfileParser();
//					saxParser.parse(stream, studentParser);	
//					StudentProfile profile = studentParser.getStudentProfile();
//					stream.close();
//					
//					if(profile != null){
//						nom.setText(profile.getNom());
//						prenom.setText(profile.getPrenom());
//						codePermanent.setText(profile.getCodePerm());
//						solde.setText(profile.getSolde());
//					}
//				} catch (SAXException e) {
//					e.printStackTrace();
//				} catch (IOException e) {
//					e.printStackTrace();
//				} catch (ParserConfigurationException e) {
//					e.printStackTrace();
//				}
			}
		}	
	}
}
