package ca.etsmtl.applets.etsmobile.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ca.etsmtl.applets.etsmobile.R;
import ca.etsmtl.applets.etsmobile.models.BottinEntry;

public class BottinListAdapter extends ArrayAdapter<BottinEntry> {

	private class Holder {

		public TextView name;
	}

	private final List<BottinEntry> list;

	public BottinListAdapter(final Context context,
			final int textViewResourceId, final List<BottinEntry> list) {
		super(context, textViewResourceId);
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public BottinEntry getItem(final int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(final int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView,
			final ViewGroup parent) {

		Holder holder;
		if (convertView == null) {

			convertView = LayoutInflater.from(getContext()).inflate(
					android.R.layout.simple_list_item_1, null);

			holder = new Holder();
			holder.name = (TextView) convertView
					.findViewById(android.R.id.text1);
			holder.name.setSingleLine(true);
			// holder.image =
			// (TextView)convertView.findViewById(R.id.newsListItemLogo);
			// holder.date =
			// (TextView)convertView.findViewById(R.id.newsListItemDate);
			// holder.description =
			// (TextView)convertView.findViewById(R.id.newsListItemDescription);
			convertView.setTag(holder);

		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.name.setText(list.get(position).toString());

		return convertView;
	}
}
