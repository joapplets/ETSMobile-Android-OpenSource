package ca.etsmtl.applets.etsmobile.adapters;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint.Style;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import ca.etsmtl.applets.etsmobile.R;
import ca.etsmtl.applets.etsmobile.models.ActivityCalendar;

public class CalendarEventsAdapter extends ArrayAdapter<ActivityCalendar> {

	Context context;
	int layoutResourceId;
	List<ActivityCalendar> events;

	public CalendarEventsAdapter(final Context context,
			final int textViewResourceId, final List<ActivityCalendar> objects) {
		super(context, textViewResourceId, objects);
		layoutResourceId = textViewResourceId;
		events = objects;
		this.context = context;

	}

	@Override
	public View getView(final int position, final View convertView,
			final ViewGroup parent) {
		final LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		final View rowView = inflater.inflate(layoutResourceId, parent, false);
		final Resources res = context.getResources();

		// set title
		TextView textView = (TextView) rowView.findViewById(R.id.txt_title);

		textView.setText(String.format(res
				.getString(R.string.calendar_event_list_item_title), events
				.get(position).getStartDate(), events.get(position).getCours()));

		// set subtitle
		textView = (TextView) rowView.findViewById(R.id.txt_subtitle);

		textView.setText(String.format(res
				.getString(R.string.calendar_event_list_item_subtitle), events
				.get(position).getName(), events.get(position).getLocation()));

		// set indicator
		final ShapeDrawable indicator = new ShapeDrawable(new OvalShape());
		indicator.getPaint().setColor(events.get(position).getDrawableId());
		indicator.getPaint().setStyle(Style.FILL);

		final ImageView img = (ImageView) rowView
				.findViewById(R.id.img_indicator);

		img.setBackgroundDrawable(indicator);

		img.setAlpha(0);

		return rowView;
	}

}
