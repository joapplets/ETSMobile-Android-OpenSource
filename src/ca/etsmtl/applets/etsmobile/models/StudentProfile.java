package ca.etsmtl.applets.etsmobile.models;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class StudentProfile extends Model {
	private String nom, prenom, codePerm, soldeTotal;

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

	private List<StudentPrograms> programms;

	public StudentProfile() {
	}

	public StudentProfile(final Parcel in) {
		super(in);
		nom = in.readString();
		prenom = in.readString();
		codePerm = in.readString();
		soldeTotal = in.readString();
	}

	public StudentProfile(final String nom, final String prenom,
			final String codePerm, final String solde) {
		this.nom = nom;
		this.prenom = prenom;
		this.codePerm = codePerm;
		soldeTotal = solde;
	}

	public String getCodePerm() {
		return codePerm != null ? codePerm.trim() : "";
	}

	public String getNom() {
		return nom != null ? nom.trim() : "";
	}

	public String getPrenom() {
		return prenom != null ? prenom.trim() : "";
	}

	public String getSolde() {
		return soldeTotal != null ? soldeTotal.trim() : "";
	}

	@Override
	public void writeToParcel(final Parcel dst, final int arg1) {
		dst.writeString(nom);
		dst.writeString(prenom);
		dst.writeString(codePerm);
		dst.writeString(soldeTotal);
	}

	@Override
	public String toString() {
		return "" + nom + "" + prenom + "" + codePerm + "" + soldeTotal;
	}

	public void setStudentPrograms(List<StudentPrograms> programms2) {
		this.programms = programms2;
	}

	public List<StudentPrograms> getStudentPrograms() {
		return programms;
	}

	public StudentPrograms getActiveStudentProfile() {

		StudentPrograms activeProgram = null;

		for (StudentPrograms program : programms) {
			if (program.getStatut().equals("actif")) {
				activeProgram = program;
			}
		}

		return activeProgram;
	}
}
