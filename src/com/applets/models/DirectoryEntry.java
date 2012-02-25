package com.applets.models;

public abstract class DirectoryEntry extends Model {

	private String email;
	private String fax;
	private String phoneNumber;
	private String room;

	public DirectoryEntry() {

	}

	public DirectoryEntry(final String fax, final String email,
			final String phoneNumber, final String room) {
		this.fax = fax;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.room = room;

	}

	public String getEmail() {
		return email;
	}

	public String getFax() {
		return fax;
	}

	public abstract String getName();

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getRoom() {
		return room;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public void setFax(final String fax) {
		this.fax = fax;
	}

	public void setPhoneNumber(final String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setRoom(final String room) {
		this.room = room;
	}

	@Override
	public abstract String toString();
}
