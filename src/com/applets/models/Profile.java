package com.applets.models;

import java.util.ArrayList;

import android.content.ContentValues;
import android.os.Parcel;

public class Profile extends Model {

    private ArrayList<Semester> sessions;
    private String name;
    private String bac;

    public Profile() {
	sessions = new ArrayList<Semester>();
    }

    @Override
    public int compareTo(Model another) {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public int describeContents() {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
	// TODO Auto-generated method stub
    }

    @Override
    ContentValues setValues() {
	_values.put("name", name);
	_values.put("bac", bac);
	_values.put("session", name);

	return _values;
    }

}
