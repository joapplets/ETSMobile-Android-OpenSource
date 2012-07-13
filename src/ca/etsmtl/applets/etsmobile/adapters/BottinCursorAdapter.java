package ca.etsmtl.applets.etsmobile.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import ca.etsmtl.applets.etsmobile.R;
import ca.etsmtl.applets.etsmobile.tools.db.SQLDBHelper;

public class BottinCursorAdapter extends SimpleCursorAdapter {
	private final Context context;
	private final Cursor c;

	private class Holder {

		public TextView prenom;
		public TextView nom;
		public TextView service;
	}

	@SuppressLint("ParserError")
	@SuppressWarnings("deprecation")
	public BottinCursorAdapter(Context context, Cursor c) {
		super(context, R.layout.bottin_list_item, c,
				new String[] { SQLDBHelper.BOTTIN_NOM },
				new int[] { R.layout.bottin_list_item });
		this.context = context;
		this.c = c;

	}

	@Override
	public void bindView(View convertView, Context ctx, Cursor c) {

		Holder holder = (Holder) convertView.getTag();
		holder.prenom.setText(c.getString(c.getColumnIndex("nom")));
	}

	@Override
	public View newView(Context ctx, Cursor c, ViewGroup group) {
		Holder holder;
		View convertView = LayoutInflater.from(ctx).inflate(
				R.layout.bottin_list_item, null);

		holder = new Holder();
		holder.prenom = (TextView) convertView
				.findViewById(R.id.bottin_list_item_prenom);
		holder.prenom.setSingleLine(true);
		holder.prenom.setText(c.getString(c.getColumnIndex("prenom")));

		holder.nom = (TextView) convertView
				.findViewById(R.id.bottin_list_item_nom);
		holder.nom.setSingleLine(true);
		holder.nom.setText(c.getString(c.getColumnIndex("nom")));

		holder.service = (TextView) convertView
				.findViewById(R.id.bottin_list_item_service);
		holder.service.setSingleLine(true);
		holder.service.setText(c.getString(c.getColumnIndex("service")));

		convertView.setTag(holder);
		return convertView;
	}

	@Override
	public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
		if (getFilterQueryProvider() != null) {
			return getFilterQueryProvider().runQuery(constraint);
		}

		StringBuilder buffer = null;
		String[] args = null;
		if (constraint != null) {
			buffer = new StringBuilder();
			buffer.append("UPPER(");
			buffer.append(SQLDBHelper.BOTTIN_NOM);
			buffer.append(") GLOB ?");
			args = new String[] { constraint.toString().toUpperCase() + "*" };
		}

		// return context.getContentResolver().query(SQLDBHelper.BOTTIN_TABLE,
		// null, buffer == null ? null : buffer.toString(), args,
		// SQLDBHelper.BOTTIN_NOM + " ASC");
		return null;

	}
}
