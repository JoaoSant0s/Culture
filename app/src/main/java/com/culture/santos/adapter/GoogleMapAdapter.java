package com.culture.santos.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

import com.culture.santos.culture.MapsActivity;
import com.culture.santos.culture.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

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
