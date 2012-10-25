package ca.etsmtl.applets.etsmobile.adapters;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

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
import ca.etsmtl.applets.etsmobile.models.CalendarEvent;

public class CalendarEventsAdapter extends ArrayAdapter<CalendarEvent> {

	Context context;
	int layoutResourceId;
	List<CalendarEvent> events;

	public CalendarEventsAdapter(final Context context,
			final int textViewResourceId, final List<CalendarEvent> objects) {
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
				.getString(R.string.calendar_event_list_item_title),
				new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.CANADA_FRENCH)
						.format(events.get(position).getStartDate()), events
						.get(position).getName()));

		// set subtitle
		textView = (TextView) rowView.findViewById(R.id.txt_subtitle);

		textView.setText(String.format(res
				.getString(R.string.calendar_event_list_item_subtitle), events
				.get(position).getEventType(), events.get(position)
				.getLocation()));

		// set indicator
		final ShapeDrawable indicator = new ShapeDrawable(new OvalShape());
		indicator.getPaint().setColor(events.get(position).getEventColor());
		indicator.getPaint().setStyle(Style.FILL);

		final ImageView img = (ImageView) rowView
				.findViewById(R.id.img_indicator);

		img.setBackgroundDrawable(indicator);

		img.setAlpha(0);

		return rowView;
	}

}
