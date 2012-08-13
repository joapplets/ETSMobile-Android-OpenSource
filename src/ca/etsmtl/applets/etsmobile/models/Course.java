package ca.etsmtl.applets.etsmobile.models;

import lombok.Data;

import com.google.gson.annotations.SerializedName;

@Data
public class Course {
	
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
