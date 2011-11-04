package com.applets.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.applets.R;
import com.applets.adapters.wrappers.ProgramWrapper;
import com.applets.models.Program;

public class ProgramListAdapter extends ArrayAdapter<Program> {

    private Activity context;
    private ArrayList<Program> list;

    public ProgramListAdapter(Activity context, ArrayList<Program> programs) {
	super(context, R.layout.program_list_row, programs);
	this.context = context;
	list = programs;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
	View row = convertView;
	ProgramWrapper wrapper;

	if (row == null) {
	    LayoutInflater inflater = context.getLayoutInflater();
	    row = inflater.inflate(R.layout.program_list_row, null);
	    wrapper = new ProgramWrapper(row);
	    row.setTag(wrapper);
	} else {
	    wrapper = (ProgramWrapper) row.getTag();
	}
	Program program = list.get(position);
	wrapper.setTitle(program.getName());
	wrapper.setDescription(program.getDescription());

	return row;
    }

}
