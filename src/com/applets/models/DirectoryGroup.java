package com.applets.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import android.content.ContentValues;
import android.os.Parcel;

public class DirectoryGroup extends DirectoryEntry implements
		Collection<DirectoryEntry> {

	private final ArrayList<DirectoryEntry> entries;
	private String name;

	public DirectoryGroup() {
		entries = new ArrayList<DirectoryEntry>();

	}

	public DirectoryGroup(final String name, final String fax,
			final String email, final String phoneNumber, final String room) {
		super(fax, email, phoneNumber, room);
		this.name = name;
		entries = new ArrayList<DirectoryEntry>();
	}

	@Override
	public boolean add(final DirectoryEntry object) {
		// TODO Auto-generated method stub
		return entries.add(object);
	}

	@Override
	public boolean addAll(final Collection<? extends DirectoryEntry> arg0) {
		// TODO Auto-generated method stub
		return entries.addAll(arg0);
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		entries.clear();
	}

	@Override
	public int compareTo(final Model arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean contains(final Object object) {
		// TODO Auto-generated method stub
		return entries.contains(object);
	}

	@Override
	public boolean containsAll(final Collection<?> arg0) {
		// TODO Auto-generated method stub
		return entries.containsAll(arg0);
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return entries.isEmpty();
	}

	@Override
	public Iterator<DirectoryEntry> iterator() {
		// TODO Auto-generated method stub
		return entries.iterator();
	}

	@Override
	public boolean remove(final Object object) {
		// TODO Auto-generated method stub
		return entries.remove(object);
	}

	@Override
	public boolean removeAll(final Collection<?> arg0) {
		// TODO Auto-generated method stub
		return entries.removeAll(arg0);
	}

	@Override
	public boolean retainAll(final Collection<?> arg0) {
		// TODO Auto-generated method stub
		return entries.retainAll(arg0);
	}

	public void setName(final String name) {
		this.name = name;
	}

	@Override
	ContentValues setValues() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return entries.size();
	}

	@Override
	public Object[] toArray() {
		// TODO Auto-generated method stub
		return entries.toArray();
	}

	@Override
	public <T> T[] toArray(final T[] array) {
		// TODO Auto-generated method stub
		return entries.toArray(array);
	}

	@Override
	public String toString() {
		String s;
		DirectoryEntry entry;
		s = name + "\n";

		final Iterator<DirectoryEntry> it = entries.iterator();

		while (it.hasNext()) {
			entry = it.next();
			s = s + entry.toString() + "\n";
		}
		return s;
	}

	@Override
	public void writeToParcel(final Parcel arg0, final int arg1) {
		// TODO Auto-generated method stub

	}

}
