package com.applets.models;

import android.content.ContentValues;

public abstract class Model implements Comparable<Model> {
    protected ContentValues _values;

    public Model() {
	_values = new ContentValues();
    }

    public ContentValues getValues() {
	return setValues();
    }

    abstract ContentValues setValues();
}
