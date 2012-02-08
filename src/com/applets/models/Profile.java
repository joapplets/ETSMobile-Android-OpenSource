package com.applets.models;

import java.util.ArrayList;

import android.content.ContentValues;
import android.os.Parcel;

public class Profile extends Model {

    private String bac;
    private String name;
    private final ArrayList<Semester> sessions;

    public Profile() {
	sessions = new ArrayList<Semester>();
    }

    @Override
    public int compareTo(final Model another) {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public int describeContents() {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    ContentValues setValues() {
	_values.put("name", name);
	_values.put("bac", bac);
	_values.put("session", name);

	return _values;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
	// TODO Auto-generated method stub
    }

}
