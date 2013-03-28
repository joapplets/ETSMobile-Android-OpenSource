package ca.etsmtl.applets.etsmobile;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import ca.etsmtl.applets.etsmobile.views.NavBar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class SecurityActivity extends FragmentActivity {

    private NavBar navBar;
    private ListView listView;

    double lat = 45.494498;
    double lng = -73.563124;

    @Override
    protected void onCreate(final android.os.Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.security);

	navBar = (NavBar) findViewById(R.id.navBar1);
	navBar.setTitle(getString(R.string.secu_title));
	navBar.hideLoading();
	navBar.hideRightButton();
	listView = (ListView) findViewById(android.R.id.list);

	final ViewGroup viewGroup = (ViewGroup) getLayoutInflater().inflate(
		R.layout.secu_list_header, (ViewGroup) findViewById(R.id.secu_list_header_layout));
	listView.addHeaderView(viewGroup, null, false);

	listView.setOnItemClickListener(new OnItemClickListener() {
	    @Override
	    public void onItemClick(final AdapterView<?> arg0, final View arg1, final int arg2,
		    final long arg3) {
		final Intent intent = new Intent(getApplicationContext(), UrgenceActivity.class);
		intent.putExtra("id", arg2);
		startActivity(intent);

	    }
	});
	listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
		getResources().getStringArray(R.array.secu_urgence)));

	viewGroup.findViewById(R.id.secu_list_header_phone).setOnClickListener(
		new OnClickListener() {

		    @Override
		    public void onClick(final View v) {
			final String phoneNumber = ((TextView) v).getText().toString();
			final String uri = "tel:" + phoneNumber.trim();
			final Intent intent = new Intent(Intent.ACTION_DIAL);
			intent.setData(Uri.parse(uri));
			startActivity(intent);
		    }
		});

	final SupportMapFragment map = ((SupportMapFragment) getSupportFragmentManager()
		.findFragmentById(R.id.map));
	final GoogleMap mapView = map.getMap();

	if (mapView != null) {
	    mapView.getUiSettings().setZoomControlsEnabled(false);
	    mapView.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 17));

	    final MarkerOptions etsMarker = new MarkerOptions();
	    etsMarker.position(new LatLng(lat, lng));
	    etsMarker.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon));
	    mapView.addMarker(etsMarker);
	} else {
	    map.getView().setVisibility(View.INVISIBLE);
	}
    }
}
