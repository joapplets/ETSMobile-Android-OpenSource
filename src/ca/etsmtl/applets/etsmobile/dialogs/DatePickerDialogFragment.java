/*******************************************************************************
 * Copyright 2013 Club ApplETS
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
