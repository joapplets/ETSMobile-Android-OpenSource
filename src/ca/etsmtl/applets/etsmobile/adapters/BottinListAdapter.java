package ca.etsmtl.applets.etsmobile.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import ca.etsmtl.applets.etsmobile.R;
import ca.etsmtl.applets.etsmobile.models.BottinEntry;

public class BottinListAdapter extends ArrayAdapter<BottinEntry> {

	private final List<BottinEntry> list;

	public BottinListAdapter(Context context, int textViewResourceId,
			List<BottinEntry> list) {
		super(context, textViewResourceId);
		this.list = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Holder holder;
		if(convertView == null){
			
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_list_item, null);
			
			holder = new Holder();
//			holder.title = (TextView)convertView.findViewById(R.id.newsListItemTitle);
//			holder.title.setSingleLine(true);
//			holder.image = (TextView)convertView.findViewById(R.id.newsListItemLogo);
//			holder.date = (TextView)convertView.findViewById(R.id.newsListItemDate);
//			holder.description = (TextView)convertView.findViewById(R.id.newsListItemDescription);
			convertView.setTag(holder);
			
		}
		return super.getView(position, convertView, parent);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public BottinEntry getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	private class Holder{}
}
