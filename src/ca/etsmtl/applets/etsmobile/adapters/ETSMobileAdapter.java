package ca.etsmtl.applets.etsmobile.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import ca.etsmtl.applets.etsmobile.R;

public class ETSMobileAdapter extends BaseAdapter {

    class Holder {
	ImageView image;
	TextView text;
    }

    private final Context context;

    private final Integer[] icons = { R.drawable.icon_profile, R.drawable.icon_news,
	    R.drawable.icon_emergency, R.drawable.icon_schedule, R.drawable.icon_addressbook,
	    R.drawable.icon_courses, R.drawable.icon_biblio, R.drawable.icon_comments };
    private final String[] iconsLabels;

    public ETSMobileAdapter(final Context context) {
	this.context = context;
	iconsLabels = context.getResources().getStringArray(R.array.main_labels);
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
    public View getView(final int position, View convertedView, final ViewGroup arg2) {

	/**
	 * Petit truc pour loader plus vite les items. Voir le site suivant pour
	 * mieux comprendre :
	 * http://www.google.com/intl/fr-FR/events/io/2009/sessions
	 * /TurboChargeUiAndroidFast.html
	 */
	Holder holder;
	if (convertedView == null) {

	    convertedView = LayoutInflater.from(context).inflate(R.layout.gridview_item, null);

	    holder = new Holder();

	    holder.image = (ImageView) convertedView.findViewById(R.id.grid_item_image);
	    holder.text = (TextView) convertedView.findViewById(R.id.grid_item_title);

	    convertedView.setTag(holder);

	} else {
	    holder = (Holder) convertedView.getTag();
	}

	holder.image.setImageResource(icons[position]);
	holder.text.setText(iconsLabels[position]);

	return convertedView;
    }
}