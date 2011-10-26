package com.applets;

import java.util.ArrayList;

import android.content.Intent;
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

public class ProgramListActivity extends BaseListActivity {

    private ArrayList<Program> programs = new ArrayList<Program>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.base_list);

	initActionBar(getString(R.string.program_list_title),
		R.id.base_list_actionbar);
	initProgramList();

	setListAdapter(new ProgramListAdapter(this, programs));
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
	Program program = programs.get(position);

	Intent intent = new Intent(this, CourseListActivity.class);
	startActivity(intent);
    }

    private void initProgramList() {
	programs = new ProgramList(getString(R.string.host)+getString(R.string.api_programmes), this);

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

}
