package ca.etsmtl.applets.etsmobile.views;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
import ca.etsmtl.applets.etsmobile.R;
import ca.etsmtl.applets.etsmobile.adapters.CalendarEventsAdapter;
import ca.etsmtl.applets.etsmobile.models.ActivityCalendar;

public class CalendarEventsListView extends ListView implements Observer {

	Context context;

	public CalendarEventsListView(final Context context) {
		super(context);

		this.context = context;
		// TODO Auto-generated constructor stub

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
		// TODO Auto-generated method stub
		final List<ActivityCalendar> events = (List<ActivityCalendar>) data;

		setAdapter(new CalendarEventsAdapter(context,
				R.layout.calendar_event_list_item, events));

	}

}
