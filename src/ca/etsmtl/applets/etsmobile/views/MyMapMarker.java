package ca.etsmtl.applets.etsmobile.views;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class MyMapMarker extends ItemizedOverlay {

	private final ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();

	public MyMapMarker(final Drawable defaultMarker) {
		super(ItemizedOverlay.boundCenterBottom(defaultMarker));
	}

	public MyMapMarker(final Drawable defaultMarker, final Context context) {
		this(defaultMarker);
	}

	public void addOverlay(final OverlayItem item) {
		mOverlays.add(item);
		populate();

	}

	@Override
	protected OverlayItem createItem(final int i) {
		return mOverlays.get(i);
	}

	@Override
	protected boolean onTap(final int index) {
		// OverlayItem item = mOverlays.get(index);
		// AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		// dialog.setTitle(item.getTitle());
		// dialog.setMessage(item.getSnippet());
		// dialog.setPositiveButton("Yes", new OnClickListener() {
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// dialog.dismiss();
		// }
		// });
		// dialog.show();
		return true;
	}

	@Override
	public int size() {
		return mOverlays.size();
	}
}
