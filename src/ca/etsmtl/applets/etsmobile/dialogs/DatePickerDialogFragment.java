package ca.etsmtl.applets.etsmobile.dialogs;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import ca.etsmtl.applets.etsmobile.models.CurrentCalendar;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.DatePicker;

public class DatePickerDialogFragment extends DatePickerDialog implements
		Observer {

	public DatePickerDialogFragment(Context context, int theme,
			OnDateSetListener callBack, int year, int monthOfYear,
			int dayOfMonth) {
		super(context, theme, callBack, year, monthOfYear, dayOfMonth);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update(Observable observable, Object arg1) {
		// TODO Auto-generated method stub
		final Calendar current = ((CurrentCalendar) observable).getCalendar();
		int year = current.get(Calendar.YEAR);
		int monthOfYear = current.get(Calendar.MONTH);
		int dayOfMonth = current.get(Calendar.DAY_OF_MONTH);
		Log.v("DatePickerDialog", "DatePickerDialog: observer date=" + year
				+ " " + monthOfYear + " " + dayOfMonth);
		super.updateDate(year, monthOfYear, dayOfMonth);
	}

}
