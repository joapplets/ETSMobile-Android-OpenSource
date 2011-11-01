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

import com.applets.adapters.ProgramListAdapter;
import com.applets.baseactivity.BaseListActivity;
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
	initProgramList();

	registerForContextMenu(getListView());
    }

    @Override
    protected void onListItemClick(ListView listView, View view, int position,
	    long id) {
	super.onListItemClick(listView, view, position, id);

	openCourseList(position);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
	    ContextMenuInfo menuInfo) {
	super.onCreateContextMenu(menu, v, menuInfo);
	menu.setHeaderTitle("Options");
	menu.add("Open PDF");
	menu.add("Open website");
    }

    private void openCourseList(int position) {
	// Program program = programs.get(position);

	Intent intent = new Intent(this, CourseListActivity.class);
	startActivity(intent);
    }

    private void initProgramList() {
	db = (ProgrammesDbAdapter) new ProgrammesDbAdapter(this).open();
	final Cursor cursor = db.getAll();

	programs = new ProgramList();
	if (cursor.getCount() < 1) {
	    programs.getFeedsFromServer(buildUrl(), this);
	} else {
	    programs.clear();
	    while (cursor.moveToNext()) {
		programs.add(new Program(
			cursor.getString(cursor
				.getColumnIndex(ProgrammesDbAdapter.KEY_NAME)),
			cursor.getString(cursor
				.getColumnIndex(ProgrammesDbAdapter.KEY_SHORT_NAME)),
			cursor.getString(cursor
				.getColumnIndex(ProgrammesDbAdapter.KEY_DESCRIPTION)),
			cursor.getString(cursor
				.getColumnIndex(ProgrammesDbAdapter.KEY_URL)),
			cursor.getString(cursor
				.getColumnIndex(ProgrammesDbAdapter.KEY_URL_PDF)),
			cursor.getInt(cursor
				.getColumnIndex(ProgrammesDbAdapter.KEY_PROGRAMME_ID))

		));
	    }
	    setListAdapter(new ProgramListAdapter(this, programs));
	}
	cursor.close();
	db.close();
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
	setListAdapter(new ProgramListAdapter(this, programs));

	// save result to database
	db.open();
	for (Program p : programs) {
	    db.create(p);
	}
	db.close();
    }

    @Override
    public void onProgressUpdate(Integer[] values) {
    }

}
