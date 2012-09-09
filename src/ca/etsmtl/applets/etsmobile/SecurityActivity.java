package ca.etsmtl.applets.etsmobile;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.etsmt.applets.etsmobile.views.NavBar;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class SecurityActivity extends MapActivity {

	private NavBar navBar;
	private ListView listView;
	private MapView mapView;

	@Override
	protected void onCreate(final android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.security);

		navBar = (NavBar) findViewById(R.id.navBar1);
		navBar.hideRightButton();

		listView = (ListView) findViewById(R.id.listView1);
		listView.setAdapter(new ArrayAdapter<String>(this,
				R.layout.simple_list_item_1, getResources().getStringArray(
						R.array.secu_urgence)));
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				Intent intent = new Intent(getApplicationContext(),
						UrgenceActivity.class);
				startActivity(intent);
			}
		});

		mapView = (MapView) findViewById(R.id.mapview);
		LayoutParams params = new LayoutParams(100, 300);
		mapView.setLayoutParams(params);
		
		final MapController controller = mapView.getController();
		
		Address adress = searchLocationByName(this,
				"École de Technologie Supérieure");
		GeoPoint geo = new GeoPoint((int) (adress.getLatitude() * 1E6),
				(int) (adress.getLongitude() * 1E6));
		 controller.setCenter(geo);
		controller.setZoom(12);
		controller.animateTo(geo);
	}

	// http://stackoverflow.com/questions/10167819/android-geopoint-from-location-locality
	public static Address searchLocationByName(Context context,
			String locationName) {
		Geocoder geoCoder = new Geocoder(context, Locale.getDefault());
		GeoPoint gp = null;
		Address ad = null;
		try {
			List<Address> addresses = geoCoder.getFromLocationName(
					locationName, 1);
			for (Address address : addresses) {
				gp = new GeoPoint((int) (address.getLatitude() * 1E6),
						(int) (address.getLongitude() * 1E6));
				address.getAddressLine(1);
				ad = address;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ad;
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
}
