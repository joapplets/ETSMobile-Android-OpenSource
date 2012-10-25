package ca.etsmtl.applets.etsmobile.models;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

abstract public class Model implements Parcelable {

	protected ContentValues values;

	public Model() {
		// TODO Auto-generated constructor stub
	}

	protected Model(final Parcel in) {

	}

	@Override
	public int describeContents() {
		return 0;
	}

	public ContentValues getContentValues() {
		values = new ContentValues();
		return values;
	}

}
