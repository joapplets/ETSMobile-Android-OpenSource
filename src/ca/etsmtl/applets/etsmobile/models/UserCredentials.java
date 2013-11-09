/*******************************************************************************
 * Copyright 2013 Club ApplETS
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package ca.etsmtl.applets.etsmobile.models;

import android.content.SharedPreferences;

import com.google.gson.annotations.SerializedName;

public class UserCredentials {

	public static final String CODE_U = "codeU";

	public static final String CODE_P = "codeP";

	public static final String APPT = "appt";

	public static final String REZ = "rez";

	@SerializedName("motPasse")
	private String password = "";

	@SerializedName("codeAccesUniversel")
	private String username = "";

	private String phase = "";

	private String appt = "";

	public UserCredentials(final SharedPreferences prefs) {
		if (prefs != null) {
			username = prefs.getString(UserCredentials.CODE_P, "");
			password = prefs.getString(UserCredentials.CODE_U, "");
			phase = prefs.getString(UserCredentials.REZ, "");
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

	public String getPhase() {
		return phase;
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

	public void setPhase(String p) {
		phase = p;
	}

	public void setAppt(String appt) {
		this.appt = appt;
	}

	public boolean isEmployee() {
		return username.equals("empl") && password.equals("empl");
	}

	public boolean hasBandwithInfo() {
		return appt != null && phase != null;
	}

	public boolean isLoggedIn() {
		return !"".equals(password) && !"".equals(username);
	}

}
