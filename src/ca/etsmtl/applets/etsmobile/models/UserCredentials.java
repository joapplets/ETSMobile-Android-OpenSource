package ca.etsmtl.applets.etsmobile.models;

import lombok.Data;
import android.content.SharedPreferences;

import com.google.gson.annotations.SerializedName;

@Data
public class UserCredentials {
	
	@SerializedName("motPasse")
	private String password;
	
	@SerializedName("codeAccesUniversel")
	private String username;
	
	public UserCredentials(SharedPreferences prefs) {
		if (prefs != null) {
			password = prefs.getString("codeP", "");
			username = prefs.getString("codeU", "");
		}
	}
}
