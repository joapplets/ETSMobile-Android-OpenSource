package ca.etsmtl.applets.etsmobile;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.etsmt.applets.etsmobile.views.MyMapMarker;
import com.etsmt.applets.etsmobile.views.NavBar;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class SecurityActivity extends MapActivity {

	// http://stackoverflow.com/questions/10167819/android-geopoint-from-location-locality
	public static Address searchLocationByName(final Context context,
			final String locationName) {
		final Geocoder geoCoder = new Geocoder(context, Locale.getDefault());
		Address ad = null;
		try {
			final List<Address> addresses = geoCoder.getFromLocationName(
					locationName, 1);
			for (final Address address : addresses) {
				new GeoPoint((int) (address.getLatitude() * 1E6),
						(int) (address.getLongitude() * 1E6));
				address.getAddressLine(1);
				ad = address;
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return ad;
	}

	private NavBar navBar;
	private ListView listView;

	private MapView mapView;

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	protected void onCreate(final android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.security);

		navBar = (NavBar) findViewById(R.id.navBar1);
		navBar.hideRightButton();
		
		navBar.setHomeAction(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		listView = (ListView) findViewById(android.R.id.list);

		final ViewGroup viewGroup = (ViewGroup) getLayoutInflater().inflate(
				R.layout.secu_list_header,
				(ViewGroup) findViewById(R.id.secu_list_header_layout));
		listView.addHeaderView(viewGroup, null, false);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(final AdapterView<?> arg0, final View arg1,
					final int arg2, final long arg3) {
				Intent intent = new Intent(getApplicationContext(),
						UrgenceActivity.class);
				intent.putExtra("id", arg2);
				startActivity(intent);

			}
		});
		listView.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, getResources()
						.getStringArray(R.array.secu_urgence)));

		viewGroup.findViewById(R.id.secu_list_header_phone).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {

					}
				});

		mapView = (MapView) findViewById(R.id.mapview);

		final Drawable drawable = getResources().getDrawable(R.drawable.icon);
		final MyMapMarker markers = new MyMapMarker(drawable, this);

		final MapController controller = mapView.getController();

		final Address adress = SecurityActivity.searchLocationByName(this,
				"École de Technologie Supérieure");
		if (adress != null) {

			final GeoPoint geo = new GeoPoint(
					(int) (adress.getLatitude() * 1E6),
					(int) (adress.getLongitude() * 1E6));

			final OverlayItem items = new OverlayItem(geo,
					"École de Technologie SUpérieur", "");
			markers.addOverlay(items);

			mapView.getOverlays().add(markers);

			controller.setCenter(geo);
			controller.setZoom(18);
			controller.animateTo(geo);
		}
	}
}
