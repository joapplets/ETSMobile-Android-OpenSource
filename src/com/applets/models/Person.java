package com.applets.models;

import android.content.ContentValues;
import android.os.Parcel;

public class Person extends DirectoryEntry {

    private String firstName;
    private String lastName;
    private String title;
    private String department;

    public Person() {

    }

    public Person(String lastName, String firstName, String phoneNumber,
	    String fax, String email, String title, String department,
	    String room) {
	super(fax, email, phoneNumber, room);

	this.firstName = firstName;
	this.lastName = lastName;
	this.title = title;
	this.department = department;
    }

    @Override
    public String getName() {
	// TODO Auto-generated method stub
	return this.firstName + " " + this.lastName;
    }

    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    public String getFirstName() {
	return firstName;
    }

    public void setLastName(String lastName) {
	this.lastName = lastName;
    }

    public String getLastName() {
	return lastName;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getTitle() {
	return title;
    }

    public void setDepartment(String department) {
	this.department = department;
    }

    public String getDepartment() {
	return department;
    }

    @Override
    public String toString() {
	return "Person:" + this.lastName + ", " + this.firstName
		+ this.getPhoneNumber() + "\nfax:" + this.getFax() + "\nemail:"
		+ this.getEmail() + "\n" + this.title + "\n" + this.department
		+ "\nroom" + this.getRoom();
    }

    @Override
    public int compareTo(Model another) {
	return 0;
    }

    @Override
    ContentValues setValues() {
	return null;
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

}
