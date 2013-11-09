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

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class Course implements Serializable {

	private static final long serialVersionUID = 6504532940801622460L;

	@SerializedName("sigle")
	private String sigle;

	@SerializedName("groupe")
	private String groupe;

	@SerializedName("session")
	private String session;

	@SerializedName("cote")
	private String cote;

	@SerializedName("titreCours")
	private String titreCours;

	public String getCote() {
		return cote;
	}

	public String getGroupe() {
		return groupe;
	}

	public String getSession() {
		return session;
	}

	public String getSigle() {
		return sigle;
	}

	public String getTitreCours() {
		return titreCours;
	}

	public void setCote(final String cote) {
		this.cote = cote;
	}

	public void setGroupe(final String groupe) {
		this.groupe = groupe;
	}

	public void setSession(final String session) {
		this.session = session;
	}

	public void setSigle(final String sigle) {
		this.sigle = sigle;
	}

	public void setTitreCours(final String titreCours) {
		this.titreCours = titreCours;
	}

	@Override
	public String toString() {
		return sigle;
	}
}
