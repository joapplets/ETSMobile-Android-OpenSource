package ca.etsmtl.applets.etsmobile.models;

import android.content.SharedPreferences;

import com.google.gson.annotations.SerializedName;

public class UserCredentials {

    public static final String CODE_U = "codeU";

    public static final String CODE_P = "codeP";

    public static final String APPT = "appt";

    public static final String REZ = "rez";

    @SerializedName("motPasse")
    private String password;

    @SerializedName("codeAccesUniversel")
    private String username;

    private String rez;

    private String appt;

    public UserCredentials(final SharedPreferences prefs) {
	if (prefs != null) {
	    username = prefs.getString(UserCredentials.CODE_P, "");
	    password = prefs.getString(UserCredentials.CODE_U, "");
	    rez = prefs.getString(UserCredentials.REZ, "");
	    appt = prefs.getString(UserCredentials.APPT, "");
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

    public String getRez() {
	return rez;
    }

    public String getAppt() {
	return appt;
    }

    public void setPassword(final String password) {
	this.password = password;
    }

    public void setUsername(final String username) {
	this.username = username;
    }

    public void setRez(String rez2) {
	rez = rez2;
    }

    public void setAppt(String appt) {
	this.appt = appt;
    }
}
