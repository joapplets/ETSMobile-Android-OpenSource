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
package ca.etsmtl.applets.etsmobile.views;

import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ListView;
import ca.etsmtl.applets.etsmobile.R;
import ca.etsmtl.applets.etsmobile.adapters.CalendarEventsAdapter;
import ca.etsmtl.applets.etsmobile.models.ActivityCalendar;
import ca.etsmtl.applets.etsmobile.models.CalendarCell;

public class CalendarEventsListView extends ListView implements Observer {

	Context context;
	CalendarEventsAdapter adapter;
	Date date;
	protected ViewGroup rootView;
	protected ActivityCalendar selectedItem;

	public CalendarEventsListView(final Context context) {
		super(context);

		this.context = context;
		rootView = this;
	}

	public CalendarEventsListView(final Context context,
			final AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		rootView = this;
	}

	public CalendarEventsListView(final Context context,
			final AttributeSet attrs, final int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		rootView = this;
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
		// setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(final AdapterView<?> arg0, final View v,
		// final int position, final long arg3) {
		// // TODO Auto-generated method stub
		//
		// final Resources res = context.getResources();
		//
		// final Bundle b = new Bundle();
		// final ActivityCalendar item = adapter.getItem(position);
		// selectedItem = item;
		// b.putString("cours", item.getCours());
		//
		// if (item.getLocation() != null) {
		// b.putString("local", item.getLocation());
		// }
		//
		// b.putString("date", new SimpleDateFormat("EEEE dd MMMM yyyy",
		// Locale.CANADA_FRENCH).format(date));
		//
		// if (item.getStartDate() != null && item.getEndDate() != null) {
		// b.putString(
		// "hours",
		// String.format(
		// res.getString(R.string.calendar_event_detail_hours),
		// item.getStartDate(), item.getEndDate()));
		// }
		//
		// if (item.getName() != null) {
		// b.putString("name", item.getName());
		// }
		//
		// final Intent nextActivity = new Intent(context,
		// ScheduleDetailActivity.class);
		// nextActivity.putExtras(b);
		// }
		//
		// });
		setAdapter(adapter);

	}

	public void resetAdaper() {
		adapter = null;
		setAdapter(adapter);
	}

}
