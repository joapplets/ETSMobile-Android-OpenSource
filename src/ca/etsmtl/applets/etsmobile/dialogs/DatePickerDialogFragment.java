package ca.etsmtl.applets.etsmobile.dialogs;

import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;

import android.app.DatePickerDialog;
import android.content.Context;
import ca.etsmtl.applets.etsmobile.models.CurrentCalendar;

public class DatePickerDialogFragment extends DatePickerDialog implements
		Observer {

	public DatePickerDialogFragment(Context context, int theme,
			OnDateSetListener callBack, int year, int monthOfYear,
			int dayOfMonth) {
		super(context, theme, callBack, year, monthOfYear, dayOfMonth);
	}

	@Override
	public void update(Observable observable, Object arg1) {
		final Calendar current = ((CurrentCalendar) observable).getCalendar();
		
		final int year = current.get(Calendar.YEAR);
		final int monthOfYear = current.get(Calendar.MONTH);
		final int dayOfMonth = current.get(Calendar.DAY_OF_MONTH);
		
		super.updateDate(year, monthOfYear, dayOfMonth);
	}

}
