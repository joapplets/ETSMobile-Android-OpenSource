package ca.etsmtl.applets.etsmobile.views;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import ca.etsmtl.applets.etsmobile.models.CurrentCalendar;

public class CalendarTextView extends TextView implements Observer {

	public CalendarTextView(final Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CalendarTextView(final Context context, final AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public CalendarTextView(final Context context, final AttributeSet attrs,
			final int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update(final Observable observable, final Object data) {
		// TODO Auto-generated method stub
		final Calendar current = ((CurrentCalendar) observable).getCalendar();
		this.setText(new SimpleDateFormat("MMMM yyyy", Locale.CANADA_FRENCH)
				.format(current.getTime()));

	}

}
