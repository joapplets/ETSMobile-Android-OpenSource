package ca.etsmtl.applets.etsmobile.views;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import ca.etsmtl.applets.etsmobile.R;
import ca.etsmtl.applets.etsmobile.ScheduleDetailActivity;
import ca.etsmtl.applets.etsmobile.adapters.CalendarEventsAdapter;
import ca.etsmtl.applets.etsmobile.models.ActivityCalendar;
import ca.etsmtl.applets.etsmobile.models.CalendarCell;

public class CalendarEventsListView extends ListView implements Observer {

	Context context;
	CalendarEventsAdapter adapter;
	Date date;

	public CalendarEventsListView(final Context context) {
		super(context);

		this.context = context;
	}

	public CalendarEventsListView(final Context context,
			final AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public CalendarEventsListView(final Context context,
			final AttributeSet attrs, final int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void update(final Observable observable, final Object data) {
		final List<ActivityCalendar> events = (List<ActivityCalendar>) data;
		date = ((CalendarCell) observable).getDate();

		adapter = new CalendarEventsAdapter(context,
				R.layout.calendar_event_list_item, events);
		this.setDivider(getResources().getDrawable(
				R.drawable.divider_horizontal_light_opaque));
		setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(final AdapterView<?> arg0, final View arg1,
					final int position, final long arg3) {
				// TODO Auto-generated method stub

				final Resources res = context.getResources();

				final Bundle b = new Bundle();
				b.putString("cours", adapter.getItem(position).getCours());

				b.putString("local", adapter.getItem(position).getLocation());

				b.putString("date", new SimpleDateFormat("EEEE dd MMMM yyyy",
						Locale.CANADA_FRENCH).format(date));

				b.putString("hours", String.format(res
						.getString(R.string.calendar_event_detail_hours),
						adapter.getItem(position).getStartDate(), adapter
								.getItem(position).getEndDate()));

				b.putString("name", adapter.getItem(position).getName());

				final Intent nextActivity = new Intent(context,
						ScheduleDetailActivity.class);
				nextActivity.putExtras(b);

				context.startActivity(nextActivity);

			}

		});
		setAdapter(adapter);

	}

}
