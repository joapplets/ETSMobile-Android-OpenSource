package ca.etsmtl.applets.etsmobile.api;

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

import android.os.AsyncTask;

import com.google.gson.Gson;

/**
 * Use this class to make async JSON GET/POST to signETS
 * 
 * @author Vincent Seguin
 * 
 * @param <T>
 * @param <E>
 */
public class SignetBackgroundThread<T, E> extends AsyncTask<Void, Integer, T> {
    public enum FetchType {
	ARRAY, OBJECT
    }

    private final String urlStr;
    private final String action;
    private final Object bodyParams;
    private final Class<E> typeOfClass;

    private final FetchType fetchType;
    private final String liste;

    public SignetBackgroundThread(final String urlStr, final String action,
	    final Object bodyParams, final Class<E> typeOfClass, final FetchType fetchType) {
	this.urlStr = urlStr;
	this.action = action;
	this.bodyParams = bodyParams;
	this.typeOfClass = typeOfClass;
	this.fetchType = fetchType;
	this.liste = "liste";
    }

    public SignetBackgroundThread(final String urlStr, final String action,
	    final Object bodyParams, final Class<E> typeOfClass, final FetchType fetchType,
	    final String liste) {
	this.urlStr = urlStr;
	this.action = action;
	this.bodyParams = bodyParams;
	this.typeOfClass = typeOfClass;
	this.fetchType = fetchType;
	this.liste = liste;
    }

    @Override
    protected T doInBackground(final Void... params) {
	T object = null;
	if (this.fetchType.equals(FetchType.ARRAY)) {
	    object = this.fetchArray();
	} else if (this.fetchType.equals(FetchType.OBJECT)) {
	    object = this.fetchObject();
	}

	return object;
    }

    @SuppressWarnings("unchecked")
    private T fetchArray() {
	ArrayList<E> array = new ArrayList<E>();

	try {
	    final StringBuilder sb = new StringBuilder();
	    sb.append(urlStr);
	    sb.append("/");
	    sb.append(action);

	    final Gson gson = new Gson();
	    final String bodyParamsString = gson.toJson(bodyParams);

	    final URL url = new URL(sb.toString());
	    final URLConnection conn = url.openConnection();
	    conn.addRequestProperty("Content-Type", "application/json");
	    conn.addRequestProperty("charset", "utf-8");

	    conn.setDoOutput(true);
	    final OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
	    wr.write(bodyParamsString);
	    wr.flush();

	    final StringWriter writer = new StringWriter();
	    final InputStream in = conn.getInputStream();
	    IOUtils.copy(in, writer);
	    in.close();
	    wr.close();

	    final String jsonString = writer.toString();

	    final ArrayList<E> objectList = new ArrayList<E>();

	    JSONObject jsonObject;
	    jsonObject = new JSONObject(jsonString);

	    JSONArray jsonRootArray;
	    jsonRootArray = jsonObject.getJSONObject("d").getJSONArray(liste);

	    for (int i = 0; i < jsonRootArray.length(); i++) {
		objectList.add(gson
			.fromJson(jsonRootArray.getJSONObject(i).toString(), typeOfClass));
	    }

	    array = objectList;
	} catch (final IOException e) {
	    e.printStackTrace();
	} catch (final JSONException e) {
	    e.printStackTrace();
	}

	return (T) array;
    }

    @SuppressWarnings("unchecked")
    private T fetchObject() {
	T object = null;

	try {
	    final StringBuilder sb = new StringBuilder();
	    sb.append(urlStr);
	    sb.append("/");
	    sb.append(action);

	    final Gson gson = new Gson();
	    final String bodyParamsString = gson.toJson(bodyParams);

	    final URL url = new URL(sb.toString());
	    final URLConnection conn = url.openConnection();
	    conn.addRequestProperty("Content-Type", "application/json; charset=UTF-8");

	    conn.setDoOutput(true);
	    final OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
	    wr.write(bodyParamsString);
	    wr.flush();

	    final StringWriter writer = new StringWriter();
	    final InputStream in = conn.getInputStream();
	    IOUtils.copy(in, writer);
	    in.close();
	    wr.close();

	    final String jsonString = writer.toString();

	    JSONObject jsonObject;
	    jsonObject = new JSONObject(jsonString);

	    JSONObject jsonRootArray;
	    jsonRootArray = jsonObject.getJSONObject("d");
	    object = (T) gson.fromJson(jsonRootArray.toString(), typeOfClass);
	    android.util.Log.d("JSON", jsonRootArray.toString());
	} catch (final MalformedURLException e) {
	    e.printStackTrace();
	} catch (final IOException e) {
	    e.printStackTrace();
	} catch (final JSONException e) {
	    e.printStackTrace();
	}
	return object;
    }
}
