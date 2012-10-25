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
			username = prefs.getString(UserCredentials.CODE_P, "");
			password = prefs.getString(UserCredentials.CODE_U, "");
		}
	}

	public UserCredentials(final String codeP, final String codeU) {
		username = codeP;
		password = codeU;
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
