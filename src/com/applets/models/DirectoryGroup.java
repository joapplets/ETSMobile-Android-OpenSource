package com.applets.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import android.content.ContentValues;

public class DirectoryGroup extends DirectoryEntry implements
	Collection<DirectoryEntry> {

    private String name;
    private ArrayList<DirectoryEntry> entries;

    public DirectoryGroup() {
	this.entries = new ArrayList<DirectoryEntry>();

    }

    public DirectoryGroup(String name, String fax, String email,
	    String phoneNumber, String room) {
	super(fax, email, phoneNumber, room);
	this.name = name;
	this.entries = new ArrayList<DirectoryEntry>();
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getName() {
	// TODO Auto-generated method stub
	return this.name;
    }

    public boolean add(DirectoryEntry object) {
	// TODO Auto-generated method stub
	return this.entries.add(object);
    }

    public boolean addAll(Collection<? extends DirectoryEntry> arg0) {
	// TODO Auto-generated method stub
	return this.entries.addAll(arg0);
    }

    public void clear() {
	// TODO Auto-generated method stub
	this.entries.clear();
    }

    public boolean contains(Object object) {
	// TODO Auto-generated method stub
	return this.entries.contains(object);
    }

    public boolean containsAll(Collection<?> arg0) {
	// TODO Auto-generated method stub
	return this.entries.containsAll(arg0);
    }

    public boolean isEmpty() {
	// TODO Auto-generated method stub
	return this.entries.isEmpty();
    }

    public Iterator<DirectoryEntry> iterator() {
	// TODO Auto-generated method stub
	return this.entries.iterator();
    }

    public boolean remove(Object object) {
	// TODO Auto-generated method stub
	return this.entries.remove(object);
    }

    public boolean removeAll(Collection<?> arg0) {
	// TODO Auto-generated method stub
	return this.entries.removeAll(arg0);
    }

    public boolean retainAll(Collection<?> arg0) {
	// TODO Auto-generated method stub
	return this.entries.retainAll(arg0);
    }

    public int size() {
	// TODO Auto-generated method stub
	return this.entries.size();
    }

    public Object[] toArray() {
	// TODO Auto-generated method stub
	return this.entries.toArray();
    }

    public <T> T[] toArray(T[] array) {
	// TODO Auto-generated method stub
	return this.entries.toArray(array);
    }

    public String toString() {
	String s;
	DirectoryEntry entry;
	s = this.name + "\n";

	Iterator<DirectoryEntry> it = this.entries.iterator();

	while (it.hasNext()) {
	    entry = it.next();
	    s = s + entry.toString() + "\n";
	}
	return s;
    }

    @Override
    public int compareTo(Model arg0) {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    ContentValues setValues() {
	// TODO Auto-generated method stub
	return null;
    }

}
