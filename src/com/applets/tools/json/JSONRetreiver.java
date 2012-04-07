package com.applets.tools.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class JSONRetreiver extends AsyncTask<String, Integer, JSONObject> {

	public static final String[] requestTypes = new String[] { "post", "get" };

	private IAsyncTaskListener listener;

	public JSONRetreiver(final IAsyncTaskListener listener) {
		this.listener = listener;
	}

	@Override
	/**
	 * params[1] passer les valeur get comme ceci : key=value; key2=value2
	 * TODO code for post
	 */
	protected JSONObject doInBackground(String... params) {
		onPreExecute();
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		JSONObject json = new JSONObject();
		HttpUriRequest request;
		
		if (params[1] == requestTypes[0]) {
			request = postRequest(params);
		} else {
			request = getRequest(params);
		}

		try {
			HttpResponse r = client.execute(request);
			StatusLine status = r.getStatusLine();

			if (status.getStatusCode() == 200) {
				HttpEntity entity = r.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					onProgressUpdate(1);
					builder.append(line);
				}
				reader.close();
				try {
					json = new JSONObject(builder.toString());
				} catch (JSONException e) {
					onCancelled();
					Log.e(JSONRetreiver.class.toString(),
							"Failed to parse data :" + params[0]);
				}
			} else {
				onCancelled();
				Log.e(JSONRetreiver.class.toString(), "Failed to download file");
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();

		}
		return json;
	}

	private HttpUriRequest getRequest(String[] params) {
		HttpGet get = null;
		if (params.length > 1) {
			String paramString = getParamString(params);
			get = new HttpGet(params[0] + "?" + paramString);	
		}
		get.setHeader("Content-Type", "application/json;  charset=utf-8;");
		return get;
	}

	private HttpUriRequest postRequest(String[] params) {
		HttpPost post = null;
		if (params.length > 1) {
			String paramString = getParamString(params);
			post = new HttpPost(params[0] + "?" + paramString);
		}
		post.setHeader("Content-Type", "application/json;  charset=utf-8;");
		return post;
	}

	private String getParamString(String... params) {
		List<NameValuePair> getParams = new ArrayList<NameValuePair>();
		for (int i = 2; i < params.length; i++) {
			String[] tempKeyValue = params[i].split("=");
			getParams.add(new BasicNameValuePair(tempKeyValue[0],
					tempKeyValue[1]));
		}
		String paramString = URLEncodedUtils.format(getParams, "utf-8");
		return paramString;
	}

	@Override
	protected void onPostExecute(JSONObject result) {
		listener.onPostExecute(result);
	}
}
