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
import android.widget.TextView;
import ca.etsmtl.applets.etsmobile.R;
import ca.etsmtl.applets.etsmobile.models.CourseEvaluation;
import ca.etsmtl.applets.etsmobile.models.EvaluationElement;

public class MyCourseDetailAdapter extends ArrayAdapter<EvaluationElement> {

    public class ViewHolder {

	public TextView txtViewSeparator;
	public TextView txtView;
	public TextView txtViewValue;

    }

    private static final int ITEM_VIEW_TYPE_LIST_ITEM = 0;
    private static final int ITEM_VIEW_TYPE_SEPARATOR = 1;
    private static final int ITEM_VIEW_TYPE_COUNT = 2;
    final String inflater = Context.LAYOUT_INFLATER_SERVICE;
    NumberFormat nf = DecimalFormat.getInstance(Locale.CANADA_FRENCH);
    NumberFormat nfs = new DecimalFormat("##.#");
    private final CourseEvaluation courseEvaluation;
    private double total;
    private final LayoutInflater li;

    public MyCourseDetailAdapter(final Context context, final int textViewResourceId,
	    final List<EvaluationElement> objects, final CourseEvaluation courseEvaluation) {
	super(context, textViewResourceId, objects);
	this.courseEvaluation = courseEvaluation;

	// parse exams results
	final NumberFormat nf = new DecimalFormat("##,#");
	new DecimalFormat("##.#");
	for (final EvaluationElement evaluationElement : courseEvaluation.getEvaluationElements()) {
	    if (evaluationElement.getEcartType() != null
		    && !evaluationElement.getEcartType().equals("")) {
		try {
		    total += nf.parse(evaluationElement.getPonderation()).doubleValue();
		} catch (final ParseException e) {
		    Log.e("MyCourseDetailAdapter Parse Error", "MyCourseDetailAdapter Parse Error "
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
    public View getView(final int position, View convertView, final ViewGroup parent) {

	final int type = getItemViewType(position);
	ViewHolder holder = null;
	if (convertView == null) {
	    holder = new ViewHolder();

	    convertView = li
		    .inflate(
			    type == MyCourseDetailAdapter.ITEM_VIEW_TYPE_LIST_ITEM ? R.layout.list_item_value
				    : R.layout.list_separator, parent, false);
	    holder.txtViewSeparator = (TextView) convertView.findViewById(R.id.textViewSeparator);
	    holder.txtView = (TextView) convertView.findViewById(R.id.textView);
	    holder.txtViewValue = (TextView) convertView.findViewById(R.id.value);

	    convertView.setTag(holder);
	} else {
	    holder = (ViewHolder) convertView.getTag();
	}

	if (type == MyCourseDetailAdapter.ITEM_VIEW_TYPE_SEPARATOR) {
	    if (position == 0) {
		holder.txtViewSeparator.setText(R.string.sommaire);
	    } else {
		holder.txtViewSeparator.setText(R.string.mesNotes);
	    }
	} else {
	    switch (position) {
	    case 1:// COURS EVAL
		holder.txtView.setText(R.string.cote);
		holder.txtViewValue.setText(courseEvaluation.getCote());
		break;
	    case 2:// NOTE À CE JOUR
		holder.txtView.setText(R.string.noteACejour);
		final String note = courseEvaluation.getNoteACeJour();
		try {
		    final double notes = nf.parse(note).doubleValue();
		    final double vraiNote = (notes * total) / 100.0;
		    holder.txtViewValue.setText("" + nfs.format(vraiNote) + "/" + nfs.format(total)
			    + " (" + note + "%)");
		} catch (final ParseException e1) {
		    e1.printStackTrace();
		}
		break;
	    case 3:// MOYENNE
		holder.txtView.setText(R.string.moyenne);
		final String m = courseEvaluation.getMoyenneClasse();
		try {
		    final double n = nf.parse(m).doubleValue();
		    // double moy = (n / total)*100;
		    holder.txtViewValue.setText("" + nfs.format(n) + "/" + nfs.format(total));
		} catch (final ParseException e) {
		    e.printStackTrace();
		}
		break;
	    case 4:// ÉCART TYPE
		holder.txtView.setText(R.string.ecartType);
		holder.txtViewValue.setText(courseEvaluation.getEcartTypeClasse());
		break;
	    case 5:// MÉDIANE
		holder.txtView.setText(R.string.mediane);
		final String n = courseEvaluation.getMedianeClasse();
		try {
		    final double med = nf.parse(n).doubleValue();
		    holder.txtViewValue.setText("" + nfs.format(med) + "/" + nfs.format(total));
		} catch (final ParseException e) {
		    e.printStackTrace();
		}

		break;
	    case 6:// RAND CENTILLE
		holder.txtView.setText(R.string.rangCentille);
		holder.txtViewValue.setText(courseEvaluation.getRangCentileClasse());
		break;
	    default:// ELSE
		final EvaluationElement element = getItem(position);
		Log.d("TAG", "nom:" + element.getNom());
		holder.txtView.setText(element.getNom());
		try {
		    final String notee = element.getNote();
		    final String sur = element.getCorrigeSur();
		    double sur100 = 0;
		    if (notee != "") {
			sur100 = ((nf.parse(notee).doubleValue()) / nf.parse(sur).doubleValue()) * 100;

			holder.txtViewValue.setText(element.getNote() + "/"
				+ element.getCorrigeSur() + " (" + nfs.format(sur100) + "%)");
		    } else {
			holder.txtViewValue.setText("");
		    }

		} catch (final ParseException e) {
		    e.printStackTrace();
		}

		break;
	    }
	}

	return convertView;
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
