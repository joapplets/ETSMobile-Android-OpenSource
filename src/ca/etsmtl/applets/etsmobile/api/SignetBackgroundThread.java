/*******************************************************************************
 * Copyright 2013 Club ApplETS
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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

import com.google.gson.Gson;

/**
 * Use this class to make async JSON GET/POST to signETS
 * 
 * @author Vincent Seguin, Philipp David, Micheal Bernier
 * 
 * @param <T>
 *            Type
 * @param <E>
 *            Entity
 */
public class SignetBackgroundThread<T, E> {
	public enum FetchType {
		ARRAY, OBJECT
	}

	private final String urlStr;
	private final String action;
	private final Object bodyParams;
	private final Class<E> typeOfClass;

	private final String liste;

	public SignetBackgroundThread(final String urlStr, final String action,
			final Object bodyParams, final Class<E> typeOfClass) {
		this.urlStr = urlStr;
		this.action = action;
		this.bodyParams = bodyParams;
		this.typeOfClass = typeOfClass;
		this.liste = "liste";
	}

	public SignetBackgroundThread(final String urlStr, final String action,
			final Object bodyParams, final Class<E> typeOfClass,
			final String liste) {
		this.urlStr = urlStr;
		this.action = action;
		this.bodyParams = bodyParams;
		this.typeOfClass = typeOfClass;
		this.liste = liste;
	}

	@SuppressWarnings("unchecked")
	public T fetchArray() {
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
			final OutputStreamWriter wr = new OutputStreamWriter(
					conn.getOutputStream());
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
			android.util.Log.d("JSON", jsonRootArray.toString());
			for (int i = 0; i < jsonRootArray.length(); i++) {
				objectList.add(gson.fromJson(jsonRootArray.getJSONObject(i)
						.toString(), typeOfClass));
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
	public T fetchObject() {
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
			conn.addRequestProperty("Content-Type",
					"application/json; charset=UTF-8");

			conn.setDoOutput(true);
			final OutputStreamWriter wr = new OutputStreamWriter(
					conn.getOutputStream());
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
