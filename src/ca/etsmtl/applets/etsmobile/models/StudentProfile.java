package ca.etsmtl.applets.etsmobile.models;

import android.os.Parcel;
import android.os.Parcelable;

public class StudentProfile extends Model {
	private String nom, prenom, codePerm, solde;

	public StudentProfile(String nom, String prenom, String codePerm,
			String solde) {
		this.nom = nom;
		this.prenom = prenom;
		this.codePerm = codePerm;
		this.solde = solde;
	}

	public StudentProfile(Parcel in) {
		super(in);
		nom = in.readString();
		prenom = in.readString();
		codePerm = in.readString();
		solde = in.readString();
	}

	public String getCodePerm() {
		return codePerm;
	}

	public String getSolde() {
		return solde;
	}

	public String getNom() {
		return nom;
	}

	public String getPrenom() {
		return prenom;
	}

	@Override
	public void writeToParcel(Parcel dst, int arg1) {
		dst.writeString(nom);
		dst.writeString(prenom);
		dst.writeString(codePerm);
		dst.writeString(solde);
	}
	
	public static final Creator<StudentProfile> CREATOR = new Parcelable.Creator<StudentProfile>() {
		public StudentProfile createFromParcel(Parcel in) {
			return new StudentProfile(in);
		}

		public StudentProfile[] newArray(int size) {
			return new StudentProfile[size];
		}
	};
}
