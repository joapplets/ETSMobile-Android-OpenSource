package ca.etsmtl.applets.etsmobile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import ca.etsmtl.applets.etsmobile.adapters.BottinListAdapter;
import ca.etsmtl.applets.etsmobile.models.BottinEntry;
import ca.etsmtl.applets.etsmobile.tools.db.BottinAdapter;
import ca.etsmtl.applets.etsmobile.tools.xml.XMLAppletsHandler;
import ca.etsmtl.applets.etsmobile.tools.xml.XMLBottinParser;
import ca.etsmtl.applets.etsmobile.tools.xml.XMLParser;

public class BottinActivity extends ListActivity implements OnItemClickListener {

	private ListView listView;
	private List<BottinEntry> list;
	private BottinListAdapter bottinAdapter;
	private BottinAdapter bottinDB;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base_list);

		// TODO : get bottin entries from DB
		bottinDB = BottinAdapter.getInstance(this);

		list = new ArrayList<BottinEntry>();
		listView = getListView();
		listView.setOnItemClickListener(this);
		bottinAdapter = new BottinListAdapter(getApplicationContext(),
				R.layout.news_list_item, list);

		listView.setAdapter(bottinAdapter);

		new BottinLoader().execute();
	}

	private class BottinLoader extends AsyncTask<Void, Void, InputStream> {

		OutputStreamWriter writer;

		@Override
		protected InputStream doInBackground(Void... params) {

			try {
				String data = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
						+ "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
						+ "<soap:Body>"
						+ "<Recherche xmlns=\"http://etsmtl.ca/\">"
						// + "<FiltreNom>string</FiltreNom>"
						// + "<FiltrePrenom>string</FiltrePrenom>"
						// + "<FiltreServiceCode>string</FiltreServiceCode>"
						+ "</Recherche>" + "</soap:Body>" + "</soap:Envelope>";

				// TODO: set filters

				URL url = new URL(
						"http://etsmtl.ca/cmspages/webservice.asmx?op=Recherche");
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setDoOutput(true);
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Host", "etsmtl.ca");
				conn.setRequestProperty("Content-Type",
						"text/xml; charset=utf-8");
				conn.setRequestProperty("SOAPAction",
						"\"http://etsmtl.ca/Recherche\"");

				writer = new OutputStreamWriter(conn.getOutputStream());

				// write parameters
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
			if (stream != null) {
				XMLAppletsHandler handler = new XMLBottinParser(
						BottinActivity.this);
				XMLParser xml = new XMLParser(stream, handler,
						BottinActivity.this);
				List<BottinEntry> newList = xml.getBottinEntries();

				list.clear();
				list.addAll(newList);
				bottinAdapter.notifyDataSetChanged();
			}
		}

	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Create BottinEntryViewActivity

	}
}
