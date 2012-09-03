package ca.etsmtl.applets.etsmobile.models;

import android.content.SharedPreferences;

import com.google.gson.annotations.SerializedName;

public class UserCredentials {

	public static final String CODE_U = "codeU";

	public static final String CODE_P = "codeP";

	@SerializedName("motPasse")
	private String password;

	@SerializedName("codeAccesUniversel")
	private String username;

	public UserCredentials(final SharedPreferences prefs) {
		if (prefs != null) {
			username = prefs.getString(CODE_P, "");
			password = prefs.getString(CODE_U, "");
		}
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public void setUsername(final String username) {
		this.username = username;
	}
}
