package com.trinkmobiles.googlemapexample;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;


public class MainActivity extends Activity {

    private static final String TAG  = MainActivity.class.getSimpleName();

    private GoogleMap mMap;
    private Marker mLondonMarker, mMyMarker;
    private Polyline mPolyline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.v(TAG, Keys.getCertificateSHA1Fingerprint(MainActivity.this));

        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.google_map)).getMap();

        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setIndoorLevelPickerEnabled(true);

        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);

        mMap.setMyLocationEnabled(true);

        MapListeners listeners = new MapListeners();
        mMap.setOnMapLongClickListener(listeners);
        mMap.setOnMarkerDragListener(listeners);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gm_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.settings:
                return true;
            case R.id.go_to_london:
                goToLondon();
                return true;
            case R.id.draw_line:
                drawLine();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void goToLondon() {


        if (mLondonMarker == null) {
            LatLng theLondon = new LatLng(51.5286417, -0.1015987);

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(theLondon);
            markerOptions.draggable(false);
            markerOptions.title(getString(R.string.london));
            markerOptions.snippet(getString(R.string.london_snippet));
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(139.0f));
            mLondonMarker = mMap.addMarker(markerOptions);
            mLondonMarker.showInfoWindow();

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(theLondon, 17);
            mMap.animateCamera(cameraUpdate);
        }
        else {
            if (mLondonMarker.isVisible()) {
                mLondonMarker.remove();
                mLondonMarker = null;
            }
        }

    }

    private void drawLine() {

        if (mPolyline == null) {
            PolylineOptions polylineOptions = new PolylineOptions().width(5).color(Color.BLUE).geodesic(true);
            polylineOptions.add(new LatLng(51.500912, -0.126205));
            polylineOptions.add(new LatLng(51.507217, -0.127665));
            polylineOptions.add(new LatLng(51.511467, -0.118996));
            polylineOptions.add(new LatLng(51.504893, -0.113674));
            polylineOptions.add(new LatLng(51.501520, -0.117033));

            mPolyline = mMap.addPolyline(polylineOptions);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(51.5052018, -0.1207146), 14);
            mMap.animateCamera(cameraUpdate);
        }
        else {
            if (mPolyline.isVisible()) {
                mPolyline.remove();
                mPolyline = null;
            }
        }
    }

    private class MapListeners implements GoogleMap.OnMapLongClickListener, GoogleMap.OnMarkerDragListener {

        @Override
        public void onMapLongClick(LatLng latLng) {
            if (mMyMarker != null) {
                mMyMarker.remove();
            }

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.draggable(true);
            markerOptions.title("THIS IS MY MARKER!");
            markerOptions.snippet("I can do what I want");
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.pins9));

            mMyMarker = mMap.addMarker(markerOptions);

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
            mMap.animateCamera(cameraUpdate);
        }

        @Override
        public void onMarkerDragStart(Marker marker) { }

        @Override
        public void onMarkerDrag(Marker marker) { }

        @Override
        public void onMarkerDragEnd(Marker marker) {
            LatLng latLng = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
            mMap.animateCamera(cameraUpdate);
        }
    }

}

