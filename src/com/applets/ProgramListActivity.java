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

public class ProgramListActivity extends BaseListActivity implements
	IAsyncTaskListener {

    private ProgramList programs;
    private ProgrammesDbAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.base_list);

	initActionBar(getString(R.string.program_list_title),
		R.id.base_list_actionbar);
	db = (ProgrammesDbAdapter) new ProgrammesDbAdapter(this).open();
	initList();

	registerForContextMenu(getListView());
    }

    @Override
    protected void onListItemClick(ListView listView, View view, int position,
	    long id) {
	super.onListItemClick(listView, view, position, id);

	Intent intent = new Intent(this, CourseListActivity.class);
	intent.putExtra(Program.class.getName(), id);

	startActivity(intent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
	    ContextMenuInfo menuInfo) {
	super.onCreateContextMenu(menu, v, menuInfo);
	menu.setHeaderTitle("Options");
	menu.add("Open PDF");
	menu.add("Open website");
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

    private String buildUrl() {
	return new StringBuilder().append(getString(R.string.host))
		.append(getString(R.string.api_programmes)).toString();
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {

	int id = item.getItemId();
	Intent intent = null;

	switch (id) {
	case 0:
	    String urlpdf = programs.get(item.getItemId()).getUrlPdf();
	    intent = new Intent(Intent.ACTION_VIEW);
	    intent.setData(Uri.parse(urlpdf));
	    startActivity(intent);
	    break;
	case 1:
	    String url = programs.get(item.getItemId()).getUrl();
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
	    Toast.makeText(this, getString(R.string.empty_update), Toast.LENGTH_SHORT).show();
	} else {
	    // insert new data in db
	    for (Model model : programs) {
		db.create(model);
	    }
	}
	dismissDialog(0);
	setListAdapter(new BaseCursorAdapter(this, db.getAll()));
    }

}
