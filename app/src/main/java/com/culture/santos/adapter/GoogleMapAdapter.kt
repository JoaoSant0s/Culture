package com.culture.santos.adapter

import android.app.Activity
import android.app.AlertDialog.Builder
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.widget.Toast

import com.culture.santos.culture.CreateEventActivity
import com.culture.santos.culture.MapsActivity
import com.culture.santos.culture.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.location.LocationListener

import java.io.IOException
import java.util.Locale
import android.os.Bundle
import com.google.android.gms.location.LocationCallback


/**
 * Created by Ricar on 13/08/2016.
 */
class GoogleMapAdapter(private val mMap: GoogleMap?, private val context: MapsActivity, locationService: String) {

    private var locationManager: LocationManager? = null
    private var currentLocation: Location? = null
    private var locationChangeListener : LocationListener ? = null

    val isEmpty: Boolean
        get() = this.mMap == null

    init {
        locationManager = this.context.getSystemService(locationService) as LocationManager
        setAlertDialogGPS()
        setUpMap()
        setMapActions()

        this.mMap!!.setOnMapClickListener { latLng ->
            Log.d("Click", "Event")
            markerAddEvent(latLng)
        }

        this.mMap.setOnMarkerClickListener { marker ->
            Log.d("Click", "Marker")
            markerClickEvent(marker)
            true
        }
    }

    fun handleResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == REQUEST_CODE_CREATE_MARKER) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this.context, "Result: OK", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this.context, "Result: FAIL", Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun markerAddEvent(latLng: LatLng) {
        if (this.context.state?.isAddEventState!!) return

        try {
            createMarker(latLng)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private fun markerClickEvent(marker: Marker) {
        if (this.context.state?.isRemoveEventState!!) return

        val builder = Builder(this.context)
        builder.setTitle(marker.title)
        builder.setMessage(marker.snippet)
        builder.setPositiveButton(R.string.remove) { dialog, id ->
            marker.remove()
            context.state?.setInitState()
        }
        builder.setNegativeButton(R.string.cancel) { dialog, id -> context.state?.setInitState() }
        builder.show()
    }

    @Throws(IOException::class)
    private fun createMarker(latLng: LatLng) {
        val gc = Geocoder(this.context.baseContext, Locale.getDefault())
        val fromLocation = gc.getFromLocation(latLng.latitude, latLng.longitude, 1)
        if (!fromLocation.isEmpty()) {
            this.context.tutorialAdapter?.hideAddEventTutorial()
            val en = fromLocation[0]
            val currentMarker = this.mMap?.addMarker(MarkerOptions().position(latLng).title("Teste").snippet(en.getAddressLine(0)))

            val intent = Intent(this.context, CreateEventActivity::class.java)
            this.context.eventsAdapter?.marker = currentMarker

            this.context.startActivityForResult(intent, REQUEST_CODE_CREATE_MARKER)

            context.state?.setInitState()
        }
    }

    private fun setAlertDialogGPS() {
        if (locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER) && locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) return

        // Build the alert dialog
        val builder = Builder(this.context)
        builder.setTitle("Location Services Not Active")
        builder.setMessage("Please enable Location Services and GPS")
        builder.setPositiveButton(R.string.ok) { dialogInterface, i ->
            // Show location settings when the user acknowledges the alert dialog
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            context.alertDialogGPS(intent)
        }
        builder.setNegativeButton(R.string.cancel) { dialog, id -> }

        val alertDialog = builder.create()
        alertDialog.setCanceledOnTouchOutside(false)
        alertDialog.show()
    }

    fun setUpMap() {
        if (!locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) return
        if (!checkPermission()) return
        val provider = locationManager!!.getBestProvider(Criteria(), true)

        //locationManager.requestLocationUpdates(provider, 0, 0, this.context);
        currentLocation = locationManager!!.getLastKnownLocation(provider)

        val state = if (currentLocation == null) LatLng(0.0, 50.0) else LatLng(currentLocation?.latitude!!, currentLocation?.longitude!!)

        mMap?.moveCamera(CameraUpdateFactory.newLatLng(state))
        if (currentLocation != null) mMap?.animateCamera(CameraUpdateFactory.zoomTo(15f))
        mMap?.uiSettings?.isZoomControlsEnabled = true
        mMap?.uiSettings?.isMyLocationButtonEnabled = true
        mMap?.isMyLocationEnabled = true
    }

    private fun checkPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(this.context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.context, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), this.context.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
            return false
        }

        if (ActivityCompat.checkSelfPermission(this.context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.context, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION), this.context.PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION)
            return false
        }
        return true

    }

    private fun setMapActions() {

    }

    companion object {

        private val REQUEST_CODE_CREATE_MARKER = 0x9345
    }
}
