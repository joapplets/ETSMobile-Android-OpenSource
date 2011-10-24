package com.applets.models;

public abstract class DirectoryEntry extends Model {

    private String fax;
    private String email;
    private String phoneNumber;
    private String room;

    public DirectoryEntry() {

    }

    public DirectoryEntry(String fax, String email, String phoneNumber,
	    String room) {
	this.fax = fax;
	this.email = email;
	this.phoneNumber = phoneNumber;
	this.room = room;

    }

    public void setFax(String fax) {
	this.fax = fax;
    }

    public String getFax() {
	return fax;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getEmail() {
	return email;
    }

    public void setPhoneNumber(String phoneNumber) {
	this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
	return phoneNumber;
    }

    public void setRoom(String room) {
	this.room = room;
    }

    public String getRoom() {
	return room;
    }

    public abstract String getName();

    public abstract String toString();
}
