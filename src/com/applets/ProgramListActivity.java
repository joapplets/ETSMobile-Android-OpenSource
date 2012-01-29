package com.applets;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.applets.adapters.BaseCursorAdapter;
import com.applets.baseactivity.BaseListActivity;
import com.applets.models.Model;
import com.applets.models.Program;
import com.applets.models.ProgramList;
import com.applets.utils.db.ProgrammesDbAdapter;
import com.applets.utils.xml.IAsyncTaskListener;
import com.markupartist.android.widget.actionbar.R;

public class ProgramListActivity extends BaseListActivity implements
	IAsyncTaskListener {

    private ProgrammesDbAdapter db;
    private ProgramList programs;

    private String buildUrl() {
	return new StringBuilder().append(getString(R.string.host))
		.append(getString(R.string.api_programmes)).toString();
    }

    private void initList() {

	final Cursor cursor = db.getAll();

	programs = new ProgramList();
	if (cursor.getCount() < 1) {
	    showDialog(0);
	    programs.execute(buildUrl(), this);
	} else {
	    setListAdapter(new BaseCursorAdapter(this, cursor));
	}
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.base_list);

	initActionBar(getString(R.string.program_list_title),
		R.id.base_list_actionbar);
	db = (ProgrammesDbAdapter) new ProgrammesDbAdapter(this).open();
	initList();

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
	intent.putExtra(Program.class.getName(), id);

	startActivity(intent);
    }

    @Override
    public boolean onMenuItemSelected(final int featureId, final MenuItem item) {

	final int id = item.getItemId();
	Intent intent = null;

	switch (id) {
	case 0:
	    final String urlpdf = programs.get(item.getItemId()).getUrlPdf();
	    intent = new Intent(Intent.ACTION_VIEW);
	    intent.setData(Uri.parse(urlpdf));
	    startActivity(intent);
	    break;
	case 1:
	    final String url = programs.get(item.getItemId()).getUrl();
	    intent = new Intent(Intent.ACTION_VIEW);
	    intent.setData(Uri.parse(url));
	    startActivity(intent);
	    break;
	default:
	    break;
	}

	return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public void onPostExecute() {
	if (programs.size() == 0) {
	    Toast.makeText(this, getString(R.string.empty_update),
		    Toast.LENGTH_SHORT).show();
	} else {
	    // insert new data in db
	    for (final Model model : programs) {
		db.create(model);
	    }
	}
	dismissDialog(0);
	setListAdapter(new BaseCursorAdapter(this, db.getAll()));
    }

}
