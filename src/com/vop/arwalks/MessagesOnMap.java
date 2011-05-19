package com.vop.arwalks;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.vop.ar.overlays.ImageOverlay;
import com.vop.tools.DBWrapper;
import com.vop.tools.LocationListener;
import com.vop.tools.VopApplication;
import com.vop.tools.data.Location;
import com.vop.tools.data.Point;
import com.vop.tools.data.Track;

/**
 * show all messages
 * 
 * @author Niels
 * 
 */
public class MessagesOnMap extends MapActivity implements LocationListener {
	
	private MapController mapController;
	private MapView mapView;
	private MyLocationOverlay myLocationOverlay;
	private Location l;
	private VopApplication app;

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	// ////////////////////////////////////
	// INITIALISATIE //
	// ////////////////////////////////////

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		this.app = (VopApplication) getApplicationContext();
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.locatiesmap_layout);
		this.mapView = (MapView) findViewById(R.id.myMapView);
		this.mapController = this.mapView.getController();

		this.mapView.setBuiltInZoomControls(true);
		this.mapView.setSatellite(true);
		this.mapView.setStreetView(true);
		
		int personId = Integer.parseInt(app.getState().get("userid"));;

		initMap();
		ArrayList<Location> locations = DBWrapper.getLocations(personId);
		for(int i=0;i<locations.size();i++){
			drawImage(locations.get(i));
		}

	}

	/**
	 * initialize the map
	 */
	private void initMap() {
		myLocationOverlay = new MyLocationOverlay(this, mapView);
		this.mapView.getOverlays().add(myLocationOverlay);
		this.mapController.setZoom(17);
		this.mapController.animateTo(new GeoPoint((int) (app.getLat() * 1E6), (int) (app.getLng() * 1E6)));
		myLocationOverlay.enableMyLocation();
	}

	// ////////////////////////////////////
	// FUNCTIES //
	// ////////////////////////////////////

	@SuppressWarnings("unchecked")
	// door drawline functie in wandeling_overlay
	private void drawImage(Location l) {
		List<Overlay> overlays = this.mapView.getOverlays();

		overlays.add(new ImageOverlay(l));
	}

	@Override
	public void locationUpdated() {
		double lat = app.getLat();
		double lng = app.getLng();

		GeoPoint punt = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));
		this.mapController.animateTo(punt);
	}
	
	/**
	 * Set listeners
	 */
	@Override
	protected void onResume() {
		super.onResume();
		app.addLocationListener(this);
	}

	/**
	 * Remove listeners
	 */
	@Override
	protected void onStop() {
		super.onStop();
		app.removeLocationListener(this);
	}

}