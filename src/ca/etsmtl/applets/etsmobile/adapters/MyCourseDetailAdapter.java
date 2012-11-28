package ca.etsmtl.applets.etsmobile.adapters;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import ca.etsmtl.applets.etsmobile.R;
import ca.etsmtl.applets.etsmobile.models.CourseEvaluation;
import ca.etsmtl.applets.etsmobile.models.EvaluationElement;

public class MyCourseDetailAdapter extends ArrayAdapter<EvaluationElement> {

	private static final int ITEM_VIEW_TYPE_LIST_ITEM = 0;
	private static final int ITEM_VIEW_TYPE_SEPARATOR = 1;
	private static final int ITEM_VIEW_TYPE_COUNT = 2;
	private final CourseEvaluation courseEvaluation;
	private int total;

	public MyCourseDetailAdapter(final Context context,
			final int textViewResourceId,
			final List<EvaluationElement> objects,
			final CourseEvaluation courseEvaluation) {
		super(context, textViewResourceId, objects);
		this.courseEvaluation = courseEvaluation;
		for (EvaluationElement evaluationElement : courseEvaluation
				.getEvaluationElements()) {
			if (evaluationElement.getEcartType() != null
					&& !evaluationElement.getEcartType().equals("")) {
				 //&& !(evaluationElement.getNote().equals(""))
				total += Integer.parseInt(evaluationElement.getPonderation());
			}
		}
	}

	@Override
	public int getCount() {
		int count = 0;
		if (super.getCount() > 0) {
			count = 8 + super.getCount();
		}
		return count;
	}

	@Override
	public EvaluationElement getItem(final int position) {
		EvaluationElement evaluationElement = null;

		if (position > 7) {
			evaluationElement = super.getItem(position - 8);
		}
		return evaluationElement;
	}

	@Override
	public int getItemViewType(final int position) {
		return position == 0 || position == 7 ? MyCourseDetailAdapter.ITEM_VIEW_TYPE_SEPARATOR
				: MyCourseDetailAdapter.ITEM_VIEW_TYPE_LIST_ITEM;
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
					type == MyCourseDetailAdapter.ITEM_VIEW_TYPE_LIST_ITEM ? R.layout.list_item_value
							: R.layout.list_separator, view, true);
		} else {
			view = (LinearLayout) convertView;
		}

		if (type == MyCourseDetailAdapter.ITEM_VIEW_TYPE_SEPARATOR) {
			if (position == 0) {
				((TextView) view.findViewById(R.id.textViewSeparator))
						.setText(getContext().getString(R.string.sommaire));
			} else {
				((TextView) view.findViewById(R.id.textViewSeparator))
						.setText(R.string.mesNotes);
			}
		} else {
			NumberFormat nf = new DecimalFormat("##,#");
			NumberFormat nfs = new DecimalFormat("##.#");
			switch (position) {
			case 1:
				((TextView) view.findViewById(R.id.textView))
						.setText(getContext().getString(R.string.cote));
				((TextView) view.findViewById(R.id.value))
						.setText(courseEvaluation.getCote());
				break;
			case 2:
				((TextView) view.findViewById(R.id.textView))
						.setText(getContext().getString(R.string.noteACejour));
				((TextView) view.findViewById(R.id.value))
						.setText(courseEvaluation.getNoteACeJour());
				break;
			case 3:
				((TextView) view.findViewById(R.id.textView))
						.setText(getContext().getString(R.string.moyenne));
				String m = courseEvaluation.getMoyenneClasse();
				try {
					double n = nf.parse(m).doubleValue();
					double moy = (n / total)*100;
					((TextView) view.findViewById(R.id.value)).setText(""
							+ nfs.format(moy));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				break;
			case 4:
				((TextView) view.findViewById(R.id.textView))
						.setText(getContext().getString(R.string.ecartType));
				((TextView) view.findViewById(R.id.value))
						.setText(courseEvaluation.getEcartTypeClasse());
				break;
			case 5:
				((TextView) view.findViewById(R.id.textView))
						.setText(getContext().getString(R.string.mediane));
				String n = courseEvaluation.getMedianeClasse();
				try {
					double med = (nf.parse(n).doubleValue() / total)*100;
					((TextView) view.findViewById(R.id.value)).setText(""
							+ nfs.format(med));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case 6:
				((TextView) view.findViewById(R.id.textView))
						.setText(getContext().getString(R.string.rangCentille));
				((TextView) view.findViewById(R.id.value))
						.setText(courseEvaluation.getRangCentileClasse());
				break;
			default:
				final EvaluationElement element = getItem(position);
				((TextView) view.findViewById(R.id.textView)).setText(element
						.getNom());
				((TextView) view.findViewById(R.id.value)).setText(element
						.getNote() + "/" + element.getCorrigeSur());
				break;
			}
		}

		return view;
	}

	@Override
	public int getViewTypeCount() {
		return MyCourseDetailAdapter.ITEM_VIEW_TYPE_COUNT;
	}

	@Override
	public boolean isEnabled(final int position) {
		return false;
	}
}
