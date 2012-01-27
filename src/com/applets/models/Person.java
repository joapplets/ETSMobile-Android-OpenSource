package com.applets.models;

import android.content.ContentValues;
import android.os.Parcel;

public class Person extends DirectoryEntry {

	private String department;
	private String firstName;
	private String lastName;
	private String title;

	public Person() {

	}

	public Person(final String lastName, final String firstName,
			final String phoneNumber, final String fax, final String email,
			final String title, final String department, final String room) {
		super(fax, email, phoneNumber, room);

		this.firstName = firstName;
		this.lastName = lastName;
		this.title = title;
		this.department = department;
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

	public String getDepartment() {
		return department;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return firstName + " " + lastName;
	}

	public String getTitle() {
		return title;
	}

	public void setDepartment(final String department) {
		this.department = department;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	public void setTitle(final String title) {
		this.title = title;
	}


	@Override
	ContentValues setValues() {
		return null;
	}


	@Override
	public String toString() {
		return "Person:" + lastName + ", " + firstName + getPhoneNumber()
				+ "\nfax:" + getFax() + "\nemail:" + getEmail() + "\n" + title
				+ "\n" + department + "\nroom" + getRoom();
	}

	@Override
	public void writeToParcel(final Parcel dest, final int flags) {
		// TODO Auto-generated method stub

	}

}
