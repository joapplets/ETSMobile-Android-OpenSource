package ca.etsmtl.applets.etsmobile.models;

import android.os.Parcel;
import android.os.Parcelable;

public class StudentProfile extends Model {
	private final String nom, prenom, codePerm, solde;

	public static final Creator<StudentProfile> CREATOR = new Parcelable.Creator<StudentProfile>() {
		@Override
		public StudentProfile createFromParcel(final Parcel in) {
			return new StudentProfile(in);
		}

		@Override
		public StudentProfile[] newArray(final int size) {
			return new StudentProfile[size];
		}
	};

	public StudentProfile(final Parcel in) {
		super(in);
		nom = in.readString();
		prenom = in.readString();
		codePerm = in.readString();
		solde = in.readString();
	}

	public StudentProfile(final String nom, final String prenom,
			final String codePerm, final String solde) {
		this.nom = nom;
		this.prenom = prenom;
		this.codePerm = codePerm;
		this.solde = solde;
	}

	public String getCodePerm() {
		return codePerm;
	}

	public String getNom() {
		return nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public String getSolde() {
		return solde;
	}

	@Override
	public void writeToParcel(final Parcel dst, final int arg1) {
		dst.writeString(nom);
		dst.writeString(prenom);
		dst.writeString(codePerm);
		dst.writeString(solde);
	}
}
