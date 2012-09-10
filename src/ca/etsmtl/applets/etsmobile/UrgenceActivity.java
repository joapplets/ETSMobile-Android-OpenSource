package ca.etsmtl.applets.etsmobile;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.etsmt.applets.etsmobile.views.NavBar;

public class UrgenceActivity extends Activity {
	private int id;
	private NavBar navBar;
	private TextView txtView1;
	private TextView txtView2;
	private int pdf_raw;
	private String[] urgence;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.urgence);
		id = getIntent().getExtras().getInt("id");

		navBar = (NavBar) findViewById(R.id.navBar1);

		txtView1 = (TextView) findViewById(R.id.urgence_title);
		txtView2 = (TextView) findViewById(R.id.urgence_txt);

		urgence = getResources().getStringArray(R.array.secu_urgence);

		pdf_raw = 0;
		int text = 0;
		switch (id) {
		case 0:
			text = R.string.urgence_resum_bombe;
			pdf_raw = R.raw.appel_a_la_bombe_2009_04_01;
			break;
		case 1:
			text = R.string.urgence_resum_colis;
			pdf_raw = R.raw.colis_suspect_et_nrbc_2009_04_01;
			break;
		case 2:
			text = R.string.urgence_resum_feu;
			pdf_raw = R.raw.incendie_evacuation_urgence;
		case 3:
			text = R.string.urgence_resum_odeur;
			pdf_raw = R.raw.odeur_suspecte_et_fuite_gaz_2009_04_01;
			break;
		case 4:
			text = R.string.urgence_resum_pane_asc;
			pdf_raw = R.raw.panne_assenceur_2009_04_01;
			break;
		case 5:
			text = R.string.urgence_resum_panne_elec;
			pdf_raw = R.raw.panne_electrique_2009_04_01;
			break;
		case 6:
			text = R.string.urgence_resum_pers_arm;
			pdf_raw = R.raw.personne_armee_2009_04_01;
			break;
		case 7:
			text = R.string.urgence_resum_medic;
			pdf_raw = R.raw.urgence_cedicale_2009_04_01;
			break;
		default:
			break;
		}

		txtView1.setText(urgence[id]);
		txtView2.setText(text);
		findViewById(R.id.button1).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openPdf();
			}
		});
	}

	private void openPdf() {

		final Intent intent = new Intent(Intent.ACTION_VIEW,
				Uri.parse("android.resource://" + getPackageName() + "/"
						+ pdf_raw));
		intent.setType("application/pdf");
		startActivity(intent);
	}
}
