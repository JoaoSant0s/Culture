package com.culture.santos.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.culture.santos.culture.MapsActivity;
import com.culture.santos.culture.R;
import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ricar on 13/08/2016.
 */
public class GoogleMapAdapter {

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


        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                try {
                    createMaker(latLng);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //saveMaker(latLng);
            }
        });

        /*googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(marker.getTitle());
                builder.setMessage(marker.getSnippet());
                builder.setPositiveButton(R.string.action_remove, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        removeMarker(marker.getPosition());
                        marker.remove();
                    }
                });
                builder.setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
                builder.show();
                return true;
            }
        });*/

    }

    private void createMaker(LatLng latLng) throws IOException {
        Geocoder gc = new Geocoder(this.context.getBaseContext(), Locale.getDefault());
        List<android.location.Address> fromLocation = gc.getFromLocation(latLng.latitude, latLng.longitude, 1);
        if (!fromLocation.isEmpty()) {
            android.location.Address en = fromLocation.get(0);
            //googleMap.addMarker(new MarkerOptions().position(latLng).title(tupla.name).snippet(en.getAddressLine(0)).icon(BitmapDescriptorFactory.defaultMarker(tupla.getColorAviso())));
        }
    }

    public boolean isEmpty(){
        return this.mMap == null;
    }

    private void setAlertDialogGPS() {
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) return;

        // Build the alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this.context);
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
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) return;
        currentLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(new Criteria(), false));

        LatLng state = (currentLocation == null) ? (new LatLng(0, 50)) : (new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(state));
        if((currentLocation != null)) mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        checkPermission();
    }

    private void checkPermission(){
        if (ActivityCompat.checkSelfPermission(this.context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    private void setMapActions() {

    }







}
