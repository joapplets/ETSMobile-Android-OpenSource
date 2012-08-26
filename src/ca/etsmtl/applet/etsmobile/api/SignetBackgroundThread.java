package ca.etsmtl.applet.etsmobile.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;

import android.os.AsyncTask;

public class SignetBackgroundThread<T, E> extends AsyncTask<Void, Integer, T> {
	private String urlStr;
	private String action;
	private Object bodyParams;
	private Class<E> typeOfClass;
	private FetchType fetchType;
	
	public enum FetchType { ARRAY, OBJECT }
	
	public SignetBackgroundThread(String urlStr, String action, Object bodyParams, Class<E> typeOfClass, FetchType fetchType) {
		this.urlStr = urlStr;
		this.action = action;
		this.bodyParams = bodyParams;
		this.typeOfClass = typeOfClass;
		this.fetchType = fetchType;
	}

	@Override
	protected T doInBackground(Void... params) {
		T object = null;
		if (this.fetchType.equals(FetchType.ARRAY)) {
			object = this.fetchArray();
		} else if (this.fetchType.equals(FetchType.OBJECT)) {
			object = this.fetchObject();
		}
		
		return object;
	}
	
	private T fetchObject() {
		T object = null;
		
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(urlStr);
			sb.append("/");
			sb.append(action);

			Gson gson = new Gson();
			String bodyParamsString = gson.toJson(bodyParams);

			URL url = new URL(sb.toString());
			URLConnection conn = url.openConnection();
			conn.addRequestProperty("Content-Type", "application/json; charset=UTF-8");
			
			conn.setDoOutput(true);
		    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		    wr.write(bodyParamsString);
		    wr.flush();
		    
			StringWriter writer = new StringWriter();
			InputStream in = conn.getInputStream();
			IOUtils.copy(in, writer);
			in.close();
			wr.close();
			
			String jsonString = writer.toString();
	
	        JSONObject jsonObject;
	        jsonObject = new JSONObject(jsonString);
	        
	        JSONObject jsonRootArray;
	        jsonRootArray = jsonObject.getJSONObject("d");
            object = (T) gson.fromJson(jsonRootArray.toString(), typeOfClass);
	        
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return object;
	}
	
	private T fetchArray() {
		ArrayList<E> array = new ArrayList<E>();
		
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(urlStr);
			sb.append("/");
			sb.append(action);

			Gson gson = new Gson();
			String bodyParamsString = gson.toJson(bodyParams);
			
			URL url = new URL(sb.toString());
			URLConnection conn = url.openConnection();
			conn.addRequestProperty("Content-Type", "application/json");
			conn.addRequestProperty("charset", "utf-8");
			
			conn.setDoOutput(true);
		    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		    wr.write(bodyParamsString);
		    wr.flush();
		    
			StringWriter writer = new StringWriter();
			InputStream in = conn.getInputStream();
			IOUtils.copy(in, writer);
			in.close();
			wr.close();
			
			String jsonString = writer.toString();
			
			ArrayList<E> objectList = new ArrayList<E>();

            JSONObject jsonObject;
            jsonObject = new JSONObject(jsonString);
            
            JSONArray jsonRootArray;
            jsonRootArray = jsonObject.getJSONObject("d").getJSONArray("liste");
            
            for (int i = 0; i < jsonRootArray.length(); i++) {
                objectList.add(gson.fromJson(jsonRootArray.getJSONObject(i).toString(), typeOfClass));
            }
            
            array = objectList;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return (T) array;
	}
}
