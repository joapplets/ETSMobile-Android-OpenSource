package ca.etsmtl.applets.etsmobile.models;

import java.io.Serializable;

import lombok.Data;

import com.google.gson.annotations.SerializedName;

@Data
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
	
	@Override
	public String toString() {
		return this.sigle;
	}
}
