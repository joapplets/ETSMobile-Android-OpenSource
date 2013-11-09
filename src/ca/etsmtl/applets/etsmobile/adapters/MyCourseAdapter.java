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
package ca.etsmtl.applets.etsmobile.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import ca.etsmtl.applets.etsmobile.R;
import ca.etsmtl.applets.etsmobile.models.Course;

public class MyCourseAdapter extends ArrayAdapter<Course> {
	public class ViewHolder {

		public TextView txtViewSeparator;
		public TextView txtTitle;
		public TextView txtSubTitle;

	}

	private static final int ITEM_VIEW_TYPE_LIST_ITEM = 0;
	private static final int ITEM_VIEW_TYPE_SEPARATOR = 1;
	private static final int ITEM_VIEW_TYPE_COUNT = 2;

	public MyCourseAdapter(final Context context, final int textViewResourceId,
			final List<Course> objects) {
		super(context, textViewResourceId, objects);
	}

	@Override
	public int getCount() {
		int count = 0;
		if (super.getCount() > 0) {
			count = super.getCount() + 1;
		}

		return count;
	}

	@Override
	public Course getItem(final int position) {
		return super.getItem(position - 1);
	}

	@Override
	public int getItemViewType(final int position) {
		return position != 0 ? MyCourseAdapter.ITEM_VIEW_TYPE_LIST_ITEM
				: MyCourseAdapter.ITEM_VIEW_TYPE_SEPARATOR;
	}

	@Override
	public View getView(final int position, View convertView,
			final ViewGroup parent) {

		final int type = getItemViewType(position);
		ViewHolder holder = null;

		if (convertView == null) {

			holder = new ViewHolder();

			convertView = LayoutInflater
					.from(getContext())
					.inflate(
							type == MyCourseAdapter.ITEM_VIEW_TYPE_LIST_ITEM ? R.layout.course_list_item
									: R.layout.list_separator, null);

			holder.txtViewSeparator = (TextView) convertView
					.findViewById(R.id.textViewSeparator);
			holder.txtTitle = (TextView) convertView.findViewById(R.id.title);
			holder.txtSubTitle = (TextView) convertView
					.findViewById(R.id.subtitle);

			convertView.setTag(holder);
		} else {

			holder = (ViewHolder) convertView.getTag();
		}

		if (type == MyCourseAdapter.ITEM_VIEW_TYPE_SEPARATOR) {
			holder.txtViewSeparator.setText(R.string.mesCours);
		} else {
			holder.txtTitle.setText(getItem(position).toString());
			holder.txtSubTitle.setText(getItem(position).getTitreCours());
		}

		return convertView;
	}

	@Override
	public int getViewTypeCount() {
		return MyCourseAdapter.ITEM_VIEW_TYPE_COUNT;
	}

	@Override
	public boolean isEnabled(final int position) {
		return getItemViewType(position) != MyCourseAdapter.ITEM_VIEW_TYPE_SEPARATOR;
	}
}
