package com.culture.santos.adapter;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Debug;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.culture.santos.culture.CreateEventActivity;
import com.culture.santos.culture.MapsActivity;
import com.culture.santos.culture.R;
import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ricar on 13/08/2016.
 */
public class GoogleMapAdapter {

    private static final int REQUEST_CODE_CREATE_MARKER = 0x9345;
    private GoogleMap mMap;
    private LocationManager locationManager;
    private Location currentLocation;
    private MapsActivity context;

    public GoogleMapAdapter(GoogleMap googleMap, MapsActivity context, String locationService){
        this.mMap = googleMap;
        this.context = context;
        this.locationManager = (LocationManager) this.context.getSystemService(locationService);

        setAlertDialogGPS();
        setUpMap();
        setMapActions();


        this.mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.d("Click", "Event");
                markerAddEvent(latLng);
            }
        });

        this.mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                Log.d("Click", "Marker");
                markerClickEvent(marker);
                return true;
            }
        });
    }
    public void handleResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_CODE_CREATE_MARKER) {
            if(resultCode == Activity.RESULT_OK) {
                Toast.makeText(this.context, "Result: OK", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this.context, "Result: FAIL", Toast.LENGTH_LONG).show();
            }
        }
    }


    private void markerAddEvent(LatLng latLng) {
        if(!this.context.getState().isAddEventState()) return;

        try {
            createMarker(latLng);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void markerClickEvent(final Marker marker){
        if(!this.context.getState().isRemoveEventState()) return;

        Builder builder = new Builder(this.context);
        builder.setTitle(marker.getTitle());
        builder.setMessage(marker.getSnippet());
        builder.setPositiveButton(R.string.remove, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                marker.remove();
                context.getState().setInitState();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                context.getState().setInitState();
            }
        });
        builder.show();
    }

    private void createMarker(LatLng latLng) throws IOException {
        Geocoder gc = new Geocoder(this.context.getBaseContext(), Locale.getDefault());
        List<android.location.Address> fromLocation = gc.getFromLocation(latLng.latitude, latLng.longitude, 1);
        if (!fromLocation.isEmpty()) {
            this.context.getTutorialAdapter().hideAddEventTutorial();
            android.location.Address en = fromLocation.get(0);
            Marker currentMarker = this.mMap.addMarker(new MarkerOptions().position(latLng).title("Teste").snippet(en.getAddressLine(0)));

            Intent intent = new Intent(this.context, CreateEventActivity.class);
            this.context.getEventsAdapter().setMarker(currentMarker);

            this.context.startActivityForResult(intent, REQUEST_CODE_CREATE_MARKER);

            context.getState().setInitState();
        }
    }

    public boolean isEmpty(){
        return this.mMap == null;
    }

    private void setAlertDialogGPS() {
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) return;

        // Build the alert dialog
        Builder builder = new Builder(this.context);
        builder.setTitle("Location Services Not Active");
        builder.setMessage("Please enable Location Services and GPS");
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                // Show location settings when the user acknowledges the alert dialog
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.alertDialogGPS(intent);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {}
        });

        Dialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public void setUpMap() {
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) return;
        if(!checkPermission()) return;
        String provider = locationManager.getBestProvider(new Criteria(), true);

        currentLocation = locationManager.getLastKnownLocation(provider);

        LatLng state = (currentLocation == null) ? (new LatLng(0, 50)) : (new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(state));
        if((currentLocation != null)) mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setMyLocationEnabled(true);
    }

    private boolean checkPermission(){
        if (ActivityCompat.checkSelfPermission(this.context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.context, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, this.context.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            return false;
        }
        return true;

    }

    private void setMapActions() {

    }







}
