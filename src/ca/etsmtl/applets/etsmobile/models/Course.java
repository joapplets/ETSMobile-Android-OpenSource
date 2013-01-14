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
