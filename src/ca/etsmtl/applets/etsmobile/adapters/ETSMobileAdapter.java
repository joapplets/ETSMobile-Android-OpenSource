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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import ca.etsmtl.applets.etsmobile.ETSMobileActivity;
import ca.etsmtl.applets.etsmobile.R;

/**
 * {@link ETSMobileActivity} Grid adapter
 * 
 * @author Phil
 * 
 */
public class ETSMobileAdapter extends BaseAdapter {

	class Holder {
		ImageView image;
		TextView text;
	}

	private final Context context;

	private final Integer[] icons = { R.drawable.icon_profile,
			R.drawable.icon_news, R.drawable.icon_emergency,
			R.drawable.icon_schedule, R.drawable.icon_addressbook,
			R.drawable.icon_courses, R.drawable.icon_biblio,
			R.drawable.icon_comments, R.drawable.icon_news };
	private final String[] iconsLabels;

	public ETSMobileAdapter(final Context context) {
		this.context = context;
		iconsLabels = context.getResources()
				.getStringArray(R.array.main_labels);
	}

	@Override
	public int getCount() {
		return icons.length;
	}

	@Override
	public Object getItem(final int arg0) {
		return null;
	}

	@Override
	public long getItemId(final int arg0) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertedView,
			final ViewGroup arg2) {

		/**
		 * Petit truc pour loader plus vite les items. Voir le site suivant pour
		 * mieux comprendre :
		 * http://www.google.com/intl/fr-FR/events/io/2009/sessions
		 * /TurboChargeUiAndroidFast.html
		 */
		Holder holder;
		if (convertedView == null) {

			convertedView = LayoutInflater.from(context).inflate(
					R.layout.gridview_item, null);

			holder = new Holder();

			holder.image = (ImageView) convertedView
					.findViewById(R.id.grid_item_image);
			holder.text = (TextView) convertedView
					.findViewById(R.id.grid_item_title);

			convertedView.setTag(holder);

		} else {
			holder = (Holder) convertedView.getTag();
		}

		holder.image.setImageResource(icons[position]);
		holder.text.setText(iconsLabels[position]);

		return convertedView;
	}
}
