package ca.etsmtl.applets.etsmobile.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import ca.etsmtl.applets.etsmobile.R;
import ca.etsmtl.applets.etsmobile.models.Course;

public class MyCourseAdapter extends ArrayAdapter<Course> {
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
	public View getView(final int position, final View convertView,
			final ViewGroup parent) {

		LinearLayout view;
		final int type = getItemViewType(position);

		if (convertView == null) {
			view = new LinearLayout(getContext());
			final String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater li;
			li = (LayoutInflater) getContext().getSystemService(inflater);
			li.inflate(
					type == MyCourseAdapter.ITEM_VIEW_TYPE_LIST_ITEM ? R.layout.course_list_item
							: R.layout.list_separator, view, true);
		} else {
			view = (LinearLayout) convertView;
		}

		if (type == MyCourseAdapter.ITEM_VIEW_TYPE_SEPARATOR) {
			((TextView) view.findViewById(R.id.textViewSeparator))
					.setText(getContext().getString(R.string.mesCours));
		} else {
			((TextView) view.findViewById(R.id.title))
					.setText(getItem(position).toString());
			((TextView) view.findViewById(R.id.subtitle)).setText(getItem(
					position).getTitreCours());
		}

		return view;
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
