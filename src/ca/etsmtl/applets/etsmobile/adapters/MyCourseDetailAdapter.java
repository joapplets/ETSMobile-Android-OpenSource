package ca.etsmtl.applets.etsmobile.adapters;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import ca.etsmtl.applets.etsmobile.R;
import ca.etsmtl.applets.etsmobile.models.CourseEvaluation;
import ca.etsmtl.applets.etsmobile.models.EvaluationElement;

public class MyCourseDetailAdapter extends BaseAdapter {

	public class ViewHolder {

		public TextView txtViewSeparator;
		public TextView txtView;
		public TextView txtViewValue;
		public TextView txtViewEcType;
		public TextView txtViewCent;
		public TextView txtViewMed;
		public TextView txtViewMoy;
		public TextView txtViewPond;

	}

	private static final int ITEM_VIEW_TYPE_LIST_ITEM = 0;
	private static final int ITEM_VIEW_TYPE_SEPARATOR = 1;
	private static final int ITEM_VIEW_TYPE_COUNT = 2;
	final String inflater = Context.LAYOUT_INFLATER_SERVICE;
	NumberFormat nf_frCA;
	NumberFormat nf_enUS;
	private final CourseEvaluation courseEvaluation;
	private double total;
	private final LayoutInflater li;
	private final Context ctx;
	private ViewHolder holder = null;

	public MyCourseDetailAdapter(final Context context,
			final CourseEvaluation courseEvaluation) {
		super();
		this.courseEvaluation = courseEvaluation;
		nf_frCA = new DecimalFormat("##,#", new DecimalFormatSymbols(
				Locale.CANADA_FRENCH));
		nf_enUS = new DecimalFormat("##.#");

		// parse exams results
		for (final EvaluationElement evaluationElement : courseEvaluation
				.getEvaluationElements()) {
			if (evaluationElement.getNote().length() > 1) {
				try {

					final String pond = evaluationElement.getPonderation();
					final double value = nf_frCA.parse(pond).doubleValue();

					total += value;

				} catch (final ParseException e) {
				}
			}
		}
		ctx = context;
		li = (LayoutInflater) ctx.getSystemService(inflater);
	}

	@Override
	public EvaluationElement getItem(final int position) {
		EvaluationElement evaluationElement = null;

		if (position > 7) {
			// offset for static rows
			evaluationElement = courseEvaluation.getEvaluationElements().get(
					position - 8);
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

		final int type = getItemViewType(position);
		if (convertView == null) {
			holder = new ViewHolder();
			// inflate from xml
			convertView = li
					.inflate(
							type == MyCourseDetailAdapter.ITEM_VIEW_TYPE_LIST_ITEM ? R.layout.list_item_value
									: R.layout.list_separator, null);
			// init objs
			holder.txtViewSeparator = (TextView) convertView
					.findViewById(R.id.textViewSeparator);
			holder.txtView = (TextView) convertView.findViewById(R.id.textView);
			holder.txtViewValue = (TextView) convertView
					.findViewById(R.id.value);
			holder.txtViewMoy = (TextView) convertView
					.findViewById(R.id.item_value_moy);
			holder.txtViewMed = (TextView) convertView
					.findViewById(R.id.item_value_med);
			holder.txtViewCent = (TextView) convertView
					.findViewById(R.id.item_value_centile);
			holder.txtViewEcType = (TextView) convertView
					.findViewById(R.id.item_value_ec_type);
			holder.txtViewPond = (TextView) convertView
					.findViewById(R.id.item_value_pond);
			// set tag
			convertView.setTag(holder);
		} else {
			// get tag
			holder = (ViewHolder) convertView.getTag();
		}

		// ui display of inflated xml
		if (type == MyCourseDetailAdapter.ITEM_VIEW_TYPE_SEPARATOR) {
			if (position == 0) {
				holder.txtViewSeparator.setText(R.string.sommaire);
			} else {
				holder.txtViewSeparator.setText(R.string.mesNotes);
			}
		} else {
			holder.txtViewMoy.setVisibility(View.GONE);
			holder.txtViewMed.setVisibility(View.GONE);
			holder.txtViewCent.setVisibility(View.GONE);
			holder.txtViewEcType.setVisibility(View.GONE);
			holder.txtViewPond.setVisibility(View.GONE);
			switch (position) {
			case 1:// COURS EVAL
				holder.txtView.setText(R.string.cote);
				holder.txtViewValue.setText(courseEvaluation.getCote());
				break;
			case 2:// NOTE � CE JOUR
				holder.txtView.setText(R.string.noteACejour);
				final String note = courseEvaluation.getScoreFinalSur100();
				holder.txtViewValue.setText(courseEvaluation.getNoteACeJour()
						+ "/" + total + " (" + note + "%)");
				break;
			case 3:// MOYENNE CLASSE
				holder.txtView.setText(R.string.moyenne);
				final String m = courseEvaluation.getMoyenneClasse();
				try {
					holder.txtViewValue
							.setText(m
									+ "/"
									+ total
									+ " ("
									+ nf_enUS.format(+(nf_frCA.parse(m)
											.doubleValue() / total) * 100)
									+ "%)");
				} catch (final ParseException e1) {
					e1.printStackTrace();
				}
				break;
			case 4:// �CART TYPE
				holder.txtView.setText(R.string.ecartType);
				holder.txtViewValue.setText(courseEvaluation
						.getEcartTypeClasse());
				break;
			case 5:// M�DIANE
				holder.txtView.setText(R.string.mediane);
				final String n = courseEvaluation.getMedianeClasse();
				try {
					holder.txtViewValue.setText(nf_enUS.format((nf_frCA
							.parse(n).doubleValue() / total) * 100) + "%");
				} catch (final ParseException e1) {
					e1.printStackTrace();
				}
				break;
			case 6:// RAND CENTILLE
				holder.txtView.setText(R.string.rangCentille);
				holder.txtViewValue.setText(courseEvaluation
						.getRangCentileClasse());
				break;
			default:// ELSE
				final EvaluationElement element = getItem(position);

				if (element != null) {
					holder.txtView.setText(element.getNom());
					try {
						final String notee = element.getNote();
						final String sur = element.getCorrigeSur();
						double sur100 = 0;
						if (!notee.equals("") && !sur.equals("")) {
							sur100 = ((nf_frCA.parse(notee).doubleValue()) / nf_frCA
									.parse(sur).doubleValue()) * 100;

							final String tmp = nf_enUS.format(sur100);
							holder.txtViewValue.setText(element.getNote() + "/"
									+ element.getCorrigeSur() + " (" + tmp
									+ "%)");

							holder.txtViewMoy.setVisibility(View.VISIBLE);
							holder.txtViewMed.setVisibility(View.VISIBLE);
							holder.txtViewCent.setVisibility(View.VISIBLE);
							holder.txtViewEcType.setVisibility(View.VISIBLE);
							holder.txtViewPond.setVisibility(View.VISIBLE);

							holder.txtViewMoy.setText("Moyenne: "
									+ " "
									+ nf_enUS.format(nf_frCA.parse(
											element.getMoyenne()).doubleValue()
											/ nf_frCA.parse(sur).doubleValue()
											* 100) + "%");

							holder.txtViewMed.setText("M�diane: "
									+ nf_enUS.format(nf_frCA.parse(
											element.getMediane()).doubleValue()
											/ nf_frCA.parse(sur).doubleValue()
											* 100) + "%");

							holder.txtViewCent.setText("Rang centile: "
									+ element.getRangCentile());

							holder.txtViewEcType.setText("�cart-type: "
									+ element.getEcartType());

							holder.txtViewPond.setText("Pond�ration: "
									+ element.getPonderation() + "%");
						} else {
							holder.txtViewValue.setText("/" + sur);
						}

					} catch (final ParseException e) {
						e.printStackTrace();
					}
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

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getCount() {
		return 8 + courseEvaluation.getEvaluationElements().size();
		// return 7 + courseEvaluation.getEvaluationElements().size();
	}
}