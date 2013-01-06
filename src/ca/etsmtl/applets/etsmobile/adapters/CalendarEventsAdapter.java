package ca.etsmtl.applets.etsmobile.adapters;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import ca.etsmtl.applets.etsmobile.R;
import ca.etsmtl.applets.etsmobile.models.ActivityCalendar;

public class CalendarEventsAdapter extends ArrayAdapter<ActivityCalendar> {

	public class Holder {

		public TextView text1;
		public TextView text2;
		public ImageView img;

	}

	Context context;
	int layoutResourceId;
	List<ActivityCalendar> events;

	/*
	private final static int[] dots = new int[] { R.drawable.kal_marker_red,
			R.drawable.kal_marker_fuchsia, R.drawable.kal_marker_green,
			R.drawable.kal_marker_lime, R.drawable.kal_marker_maroon,
			R.drawable.kal_marker_navy, R.drawable.kal_marker_aqua,
			R.drawable.kal_marker_yellow, R.drawable.kal_marker_black };
	*/

	public CalendarEventsAdapter(final Context context,
			final int textViewResourceId, final List<ActivityCalendar> objects) {
		super(context, textViewResourceId, objects);
		layoutResourceId = textViewResourceId;
		events = objects;
		this.context = context;

	}

	@Override
	public View getView(final int position, View convertView,
			final ViewGroup parent) {
		final LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		Holder holder = null;

		final ActivityCalendar event = events.get(position);
		final Resources res = context.getResources();

		if (convertView == null) {
			convertView = inflater.inflate(layoutResourceId, parent, false);
			holder = new Holder();
			holder.text1 = (TextView) convertView.findViewById(R.id.txt_title);
			holder.text2 = (TextView) convertView
					.findViewById(R.id.txt_subtitle);
			holder.img = (ImageView) convertView
					.findViewById(R.id.img_indicator);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		holder.text1.setText(String.format(
				res.getString(R.string.calendar_event_list_item_title),
				event.getStartDate(), event.getCours()));
		holder.text2.setText(String.format(
				res.getString(R.string.calendar_event_list_item_subtitle),
				event.getName(), event.getLocation()));

		final int resid = event.getDrawableResId();
		holder.img.setImageDrawable(res
				.getDrawable(events.get(position).getDrawableResId()));
		return convertView;
	}

}
