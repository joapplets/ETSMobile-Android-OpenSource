package ca.etsmtl.applets.etsmobile.models;

import lombok.Data;

import com.google.gson.annotations.SerializedName;

@Data
public class Session {
	@SerializedName("abrege")
	private String shortName;
	
	@SerializedName("auLong")
	private String longName;
	
	@Override
	public String toString() {
		return getShortName();
	}
}
