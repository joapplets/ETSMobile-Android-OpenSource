package ca.etsmtl.applets.etsmobile.adapters;

import java.util.List;

import ca.etsmtl.applets.etsmobile.R;
import ca.etsmtl.applets.etsmobile.models.CourseEvaluation;
import ca.etsmtl.applets.etsmobile.models.EvaluationElement;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyCourseDetailAdapter extends ArrayAdapter<EvaluationElement>{

	private static final int ITEM_VIEW_TYPE_LIST_ITEM = 0;
	private static final int ITEM_VIEW_TYPE_SEPARATOR = 1;
	private static final int ITEM_VIEW_TYPE_COUNT = 2;
	private CourseEvaluation courseEvaluation;
	
	public MyCourseDetailAdapter(Context context, int textViewResourceId, List<EvaluationElement> objects, CourseEvaluation courseEvaluation) {
		super(context, textViewResourceId, objects);
		this.courseEvaluation = courseEvaluation;
	}
	
	@Override
	public int getViewTypeCount() {
		return ITEM_VIEW_TYPE_COUNT;
	}
	
	@Override
	public int getItemViewType(int position) {
		return position == 0 || position == 7 ? ITEM_VIEW_TYPE_SEPARATOR : ITEM_VIEW_TYPE_LIST_ITEM;
	}
	
	@Override
	public int getCount() {
		int count = 0;
		if (super.getCount() > 0) {
			count = 8 + super.getCount();
		}
		return  count;
	}
	
	@Override
	public EvaluationElement getItem(int position) {
		EvaluationElement evaluationElement = null;
		
		if (position > 7) {
			evaluationElement = super.getItem(position - 8);
		}
		return evaluationElement;
	}
	
	@Override
	public boolean isEnabled(int position) {
		return false;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LinearLayout view;
		final int type = getItemViewType(position);

		if (convertView == null) {
			view = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater li;
			li = (LayoutInflater) getContext().getSystemService(inflater);
			li.inflate(type == ITEM_VIEW_TYPE_LIST_ITEM ? R.layout.list_item_value : R.layout.list_separator, view, true);
		} else {
			view = (LinearLayout) convertView;
		}
		
		if (type == ITEM_VIEW_TYPE_SEPARATOR) {
			if (position == 0) {
				((TextView) view.findViewById(R.id.textViewSeparator)).setText(getContext().getString(R.string.sommaire));
			} else {
				((TextView) view.findViewById(R.id.textViewSeparator)).setText(R.string.mesNotes);
			}
		} else {
			switch (position) {
			case 1:
				((TextView) view.findViewById(R.id.textView)).setText(getContext().getString(R.string.cote));
				((TextView) view.findViewById(R.id.value)).setText(courseEvaluation.getCote());
				break;
			case 2:
				((TextView) view.findViewById(R.id.textView)).setText(getContext().getString(R.string.noteACejour));
				((TextView) view.findViewById(R.id.value)).setText(courseEvaluation.getNoteACeJour());
				break;
			case 3:
				((TextView) view.findViewById(R.id.textView)).setText(getContext().getString(R.string.moyenne));
				((TextView) view.findViewById(R.id.value)).setText(courseEvaluation.getMoyenneClasse());
				break;
			case 4:
				((TextView) view.findViewById(R.id.textView)).setText(getContext().getString(R.string.ecartType));
				((TextView) view.findViewById(R.id.value)).setText(courseEvaluation.getEcartTypeClasse());
				break;
			case 5:
				((TextView) view.findViewById(R.id.textView)).setText(getContext().getString(R.string.mediane));
				((TextView) view.findViewById(R.id.value)).setText(courseEvaluation.getMedianeClasse());
				break;
			case 6:
				((TextView) view.findViewById(R.id.textView)).setText(getContext().getString(R.string.rangCentille));
				((TextView) view.findViewById(R.id.value)).setText(courseEvaluation.getRangCentileClasse());
				break;
			default:
				EvaluationElement element = getItem(position);
				((TextView) view.findViewById(R.id.textView)).setText(element.getNom());
				((TextView) view.findViewById(R.id.value)).setText(element.getNote());
				break;
			}
		}

		return view;
	}
}
