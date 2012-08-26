package ca.etsmtl.applets.etsmobile.models;

import android.content.SharedPreferences;

import com.google.gson.annotations.SerializedName;

public class UserCredentials {
	
	@SerializedName("motPasse")
	private String password;
	
	@SerializedName("codeAccesUniversel")
	private String username;
	
	public UserCredentials(SharedPreferences prefs) {
		if (prefs != null) {
			username = prefs.getString("codeP", "");
			password = prefs.getString("codeU", "");
		}
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
