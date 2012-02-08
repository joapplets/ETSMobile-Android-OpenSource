package com.applets.models;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

public class TourStop extends Model {

    /**
     * Parcel Creator
     */
    public static final Parcelable.Creator<TourStop> CREATOR = new Parcelable.Creator<TourStop>() {
	@Override
	public TourStop createFromParcel(final Parcel in) {
	    return new TourStop(in);
	}

	@Override
	public TourStop[] newArray(final int size) {
	    return new TourStop[size];
	}
    };
    private String name = "";
    private String QR_CODE_REF = "";
    private final Integer ressource_id;

    private String text = "";

    public TourStop(final Parcel in) {
	name = in.readString();
	text = in.readString();
	QR_CODE_REF = in.readString();
	ressource_id = in.readInt();

    }

    public TourStop(final String name, final String text,
	    final Integer ressource_id, final String ref) {
	this.name = name;
	this.text = text;
	this.ressource_id = ressource_id;
	QR_CODE_REF = ref;
    }

    @Override
    public int compareTo(final Model another) {
	return 0;
    }

    @Override
    public int describeContents() {
	// TODO Auto-generated method stub
	return 0;
    }

    public String getName() {
	return name;
    }

    public String getQR_CODE_REF() {
	return QR_CODE_REF;
    }

    public Integer getRessource_id() {
	return ressource_id;
    }

    public String getText() {
	return text;
    }

    @Override
    public ContentValues setValues() {
	_values.put("name", name);
	_values.put("text", text);
	_values.put("ressource_id", ressource_id);
	return _values;
    }

    @Override
    public void writeToParcel(final Parcel dest, final int flags) {
	dest.writeString(name);
	dest.writeString(text);
	dest.writeString(QR_CODE_REF);
	dest.writeInt(ressource_id);
    }

}
