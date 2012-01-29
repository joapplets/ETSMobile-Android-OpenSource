package com.applets.models;

import android.content.ContentValues;
import android.os.Parcelable;

/**
 * Abstract representation of some data. All models are comparable and
 * Parcelable.
 * 
 * @author Philippe David
 * @version 1.0
 */
public abstract class Model implements Comparable<Model>, Parcelable {

    /**
     * Android sqlite id, every entry has one
     */
    protected long _id;
    /**
     * Object Values for SQLite
     */
    protected ContentValues _values;

    /**
     * Base Constructor, inits the object
     */
    public Model() {
	_values = new ContentValues();
    }

    /**
     * Returns the SQLite row _id
     * 
     * @return long
     */
    public long getID() {
	return _id;
    }

    /**
     * Get the object Values in a key->value object
     * 
     * @return ContentValues
     */
    public ContentValues getValues() {
	return setValues();
    }

    /**
     * Children models must implement them self how values are stored
     * 
     * @return ContentValue
     */
    abstract ContentValues setValues();

}
