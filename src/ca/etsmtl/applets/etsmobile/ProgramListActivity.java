package ca.etsmtl.applets.etsmobile;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import ca.etsmtl.applets.etsmobile.models.Programme;

public class ProgramListActivity extends ListActivity {

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base_list);
		registerForContextMenu(getListView());
	}

	@Override
	public void onCreateContextMenu(final ContextMenu menu, final View v,
			final ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.setHeaderTitle("Options");
		menu.add("Open PDF");
		menu.add("Open website");
	}

	@Override
	protected void onListItemClick(final ListView listView, final View view,
			final int position, final long id) {
		super.onListItemClick(listView, view, position, id);

		final Intent intent = new Intent(this, CourseListActivity.class);
		intent.putExtra(Programme.class.getName(), id);
		startActivity(intent);
	}

	@Override
	public boolean onMenuItemSelected(final int featureId, final MenuItem item) {

		final int id = item.getItemId();
		Intent intent = null;

		switch (id) {
		case 0:
			// final String urlpdf = programs.get(item.getItemId()).getUrlPdf();
			intent = new Intent(Intent.ACTION_VIEW);
			// intent.setData(Uri.parse(urlpdf));
			startActivity(intent);
			break;
		case 1:
			// final String url = programs.get(item.getItemId()).getUrl();
			intent = new Intent(Intent.ACTION_VIEW);
			// intent.setData(Uri.parse(url));
			startActivity(intent);
			break;
		default:
			break;
		}

		return super.onMenuItemSelected(featureId, item);
	}

}
