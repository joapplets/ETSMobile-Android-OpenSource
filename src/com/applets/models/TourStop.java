package com.applets.models;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

public class TourStop extends Model {

    private String name = "";
    private String text = "";
    private Integer ressource_id;
    private String QR_CODE_REF = "";

    public TourStop(String name, String text, Integer ressource_id, String ref) {
	this.name = name;
	this.text = text;
	this.ressource_id = ressource_id;
	this.QR_CODE_REF = ref;
    }

    public TourStop(Parcel in) {
	this.name = in.readString();
	this.text = in.readString();
	this.QR_CODE_REF = in.readString();
	this.ressource_id = in.readInt();

    }

    public String getName() {
	return name;
    }

    public String getText() {
	return text;
    }

    public Integer getRessource_id() {
	return ressource_id;
    }

    public String getQR_CODE_REF() {
	return QR_CODE_REF;
    }

    @Override
    public int compareTo(Model another) {
	return 0;
    }

    @Override
    public ContentValues setValues() {
	_values.put("name", name);
	_values.put("text", text);
	_values.put("ressource_id", ressource_id);
	return _values;
    }

    @Override
    public int describeContents() {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
	dest.writeString(name);
	dest.writeString(text);
	dest.writeString(QR_CODE_REF);
	dest.writeInt(this.ressource_id);
    }
    
    /**
     * Parcel Creator
     */
    public static final Parcelable.Creator<TourStop> CREATOR = new Parcelable.Creator<TourStop>() {
        @Override
	public TourStop createFromParcel(Parcel in) {
            return new TourStop(in);
        }
 
        @Override
	public TourStop[] newArray(int size) {
            return new TourStop[size];
        }
    };
    

}
