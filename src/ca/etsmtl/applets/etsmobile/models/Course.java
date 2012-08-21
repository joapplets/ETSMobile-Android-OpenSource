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
	
	public String getSigle() {
		return sigle;
	}

	public void setSigle(String sigle) {
		this.sigle = sigle;
	}

	public String getGroupe() {
		return groupe;
	}

	public void setGroupe(String groupe) {
		this.groupe = groupe;
	}

	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}

	public String getCote() {
		return cote;
	}

	public void setCote(String cote) {
		this.cote = cote;
	}

	public String getTitreCours() {
		return titreCours;
	}

	public void setTitreCours(String titreCours) {
		this.titreCours = titreCours;
	}

	@Override
	public String toString() {
		return this.sigle;
	}
}
