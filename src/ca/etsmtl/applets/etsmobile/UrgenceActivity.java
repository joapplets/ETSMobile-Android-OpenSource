package ca.etsmtl.applets.etsmobile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.xml.sax.XMLReader;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.Html;
import android.text.Html.TagHandler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import ca.etsmtl.applets.etsmobile.views.NavBar;

public class UrgenceActivity extends Activity {
	private class MyTagHandler implements TagHandler {
		boolean first = true;
		String parent = null;
		int index = 1;

		@Override
		public void handleTag(final boolean opening, final String tag,
				final Editable output, final XMLReader xmlReader) {

			if (tag.equals("ul")) {
				parent = "ul";
			} else if (tag.equals("ol")) {
				parent = "ol";
			}
			if (tag.equals("li")) {
				if (parent.equals("ul")) {
					if (first) {
						output.append("\n\t•");
						first = false;
					} else {
						first = true;
					}
				} else {
					if (first) {
						output.append("\n\t" + index + ". ");
						first = false;
						index++;
					} else {
						first = true;
					}
				}
			}
		}
	}

	private static final String APPLICATION_PDF = "application/pdf";
	private static final String SDCARD = Environment
			.getExternalStorageDirectory().getPath();
	private int id;
	private NavBar navBar;
	private TextView txtView1;
	private TextView txtView2;
	private String pdf_raw;

	private String[] urgence;

	private void copyAssets() {
		final AssetManager assetManager = getAssets();
		String[] files = null;
		try {
			files = assetManager.list("");
		} catch (final IOException e) {
			Log.e("tag", e.getMessage());
		}
		for (final String filename : files) {
			InputStream in = null;
			OutputStream out = null;
			try {

				final File f = new File(UrgenceActivity.SDCARD + "/" + filename);
				if (!f.exists()) {
					in = assetManager.open(filename);
					out = new FileOutputStream(UrgenceActivity.SDCARD + "/"
							+ filename);
					copyFile(in, out);
					in.close();
					in = null;
					out.flush();
					out.close();
					out = null;
				}
			} catch (final Exception e) {
				Log.e("tag", e.getMessage());
			}
		}
		openPdf();
	}

	private void copyFile(final InputStream in, final OutputStream out)
			throws IOException {
		final byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
	}

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.urgence);
		id = getIntent().getExtras().getInt("id");

		navBar = (NavBar) findViewById(R.id.navBar1);
		navBar.setHomeAction(new OnClickListener() {

			@Override
			public void onClick(final View arg0) {
				finish();
			}
		});
		navBar.hideRightButton();
		navBar.hideTitle();
		navBar.hideLoading();

		txtView1 = (TextView) findViewById(R.id.urgence_title);
		txtView2 = (TextView) findViewById(R.id.urgence_txt);

		urgence = getResources().getStringArray(R.array.secu_urgence);

		int text = 0;
		switch (--id) {
		case 0:
			text = R.string.urgence_resum_bombe;
			pdf_raw = "appel_a_la_bombe_2009_04_01.pdf";
			break;
		case 1:
			text = R.string.urgence_resum_colis;
			pdf_raw = "colis_suspect_et_nrbc_2009_04_01.pdf";
			break;
		case 2:
			text = R.string.urgence_resum_feu;
			pdf_raw = "incendie_evacuation_urgence.pdf";
		case 3:
			text = R.string.urgence_resum_odeur;
			pdf_raw = "odeur_suspecte_et_fuite_gaz_2009_04_01.pdf";
			break;
		case 4:
			text = R.string.urgence_resum_pane_asc;
			pdf_raw = "panne_assenceur_2009_04_01.pdf";
			break;
		case 5:
			text = R.string.urgence_resum_panne_elec;
			pdf_raw = "panne_electrique_2009_04_01.pdf";
			break;
		case 6:
			text = R.string.urgence_resum_pers_arm;
			pdf_raw = "personne_armee_2009_04_01.pdf";
			break;
		case 7:
			text = R.string.urgence_resum_medic;
			pdf_raw = "urgence_cedicale_2009_04_01.pdf";
			break;
		default:
			break;
		}
		navBar.setTitle(urgence[id]);
		// txtView1.setText();
		final String string = getString(text);
		// http://stackoverflow.com/questions/3150400/html-list-tag-not-working-in-android-textview-what-can-i-do
		txtView2.setText(Html.fromHtml(string, null, new MyTagHandler()));

		findViewById(R.id.button1).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				copyAssets();
			}
		});
	}

	private void openPdf() {

		final Intent intent = new Intent(Intent.ACTION_VIEW);

		final Uri data = Uri.fromFile(new File(UrgenceActivity.SDCARD + "/"
				+ pdf_raw));
		intent.setDataAndType(data, UrgenceActivity.APPLICATION_PDF);

		startActivityForResult(intent, Activity.RESULT_OK);
	}
}
