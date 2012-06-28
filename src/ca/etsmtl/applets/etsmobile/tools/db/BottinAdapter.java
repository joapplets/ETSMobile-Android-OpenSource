package ca.etsmtl.applets.etsmobile.tools.db;

import android.app.Activity;

public class BottinAdapter {

	private static BottinAdapter instance;

	private BottinAdapter() {
		// TODO Auto-generated constructor stub
	}

	public static BottinAdapter getInstance(Activity bottinActivity) {
		if (instance == null) {
			instance = new BottinAdapter();
		}
		return instance;
	}

}
