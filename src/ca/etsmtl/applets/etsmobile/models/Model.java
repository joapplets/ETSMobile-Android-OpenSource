package ca.etsmtl.applets.etsmobile.models;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

abstract public class Model implements Parcelable {

	protected ContentValues values;

	public Model() {
		// TODO Auto-generated constructor stub
	}
	
	protected Model(Parcel in){
		
	}

	public ContentValues getContentValues() {
		values = new ContentValues();
		return values;
	}

	@Override
	public int describeContents() {
		return 0;
	}

}
