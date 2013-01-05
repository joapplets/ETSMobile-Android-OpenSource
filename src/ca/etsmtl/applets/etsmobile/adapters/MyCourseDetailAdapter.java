package ca.etsmtl.applets.etsmobile.adapters;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

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
	final String inflater = Context.LAYOUT_INFLATER_SERVICE;
	NumberFormat nf = DecimalFormat.getInstance(Locale.CANADA_FRENCH);
	NumberFormat nfs = new DecimalFormat("##.#");
	private final CourseEvaluation courseEvaluation;
	private double total;
	private LayoutInflater li;

	public MyCourseDetailAdapter(final Context context,
			final int textViewResourceId,
			final List<EvaluationElement> objects,
			final CourseEvaluation courseEvaluation) {
		super(context, textViewResourceId, objects);
		this.courseEvaluation = courseEvaluation;

		NumberFormat nf = new DecimalFormat("##,#");
		NumberFormat nfs = new DecimalFormat("##.#");
		for (EvaluationElement evaluationElement : courseEvaluation
				.getEvaluationElements()) {
			if (evaluationElement.getEcartType() != null
					&& !evaluationElement.getEcartType().equals("")) {
				try {
					total += nf.parse(evaluationElement.getPonderation())
							.doubleValue();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					Log.e("MyCourseDetailAdapter Parse Error",
							"MyCourseDetailAdapter Parse Error "
									+ e.getMessage());
				}
			}
		}
		total = ((int) (total * 100)) / 100.0;

		li = (LayoutInflater) getContext().getSystemService(inflater);
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
	public View getView(final int position, View convertView,
			final ViewGroup parent) {

		View view;
		final int type = getItemViewType(position);

		if (convertView == null) {
			view = li
					.inflate(
							type == MyCourseDetailAdapter.ITEM_VIEW_TYPE_LIST_ITEM ? R.layout.list_item_value
									: R.layout.list_separator, parent, false);
		} else {
			view = convertView;
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
				String note = courseEvaluation.getNoteACeJour();
				try {
					double notes = nf.parse(note).doubleValue();
					double vraiNote = (notes * total) / 100.0;
					((TextView) view.findViewById(R.id.value)).setText(""
							+ nfs.format(vraiNote) + "/" + nfs.format(total)
							+ " (" + note + "%)");
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
				break;
			case 3:
				((TextView) view.findViewById(R.id.textView))
						.setText(getContext().getString(R.string.moyenne));
				String m = courseEvaluation.getMoyenneClasse();
				try {
					double n = nf.parse(m).doubleValue();
					// double moy = (n / total)*100;
					((TextView) view.findViewById(R.id.value)).setText(""
							+ nfs.format(n) + "/" + nfs.format(total));
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
					double med = nf.parse(n).doubleValue();
					((TextView) view.findViewById(R.id.value)).setText(""
							+ nfs.format(med) + "/" + nfs.format(total));
				} catch (ParseException e) {
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
				Log.d("TAG", "nom:" + element.getNom());
				((TextView) view.findViewById(R.id.textView)).setText(element
						.getNom());
				try {
					String notee = element.getNote();
					String sur = element.getCorrigeSur();
					double sur100 = 0;
					if (notee != "") {
						sur100 = ((nf.parse(notee).doubleValue()) / nf.parse(
								sur).doubleValue()) * 100;

						((TextView) view.findViewById(R.id.value))
								.setText(element.getNote() + "/"
										+ element.getCorrigeSur() + " ("
										+ nfs.format(sur100) + "%)");
					} else {
						((TextView) view.findViewById(R.id.value)).setText("");
					}

				} catch (ParseException e) {
					e.printStackTrace();
				}

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
