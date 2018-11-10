package com.culture.santos.adapter


import android.app.Activity
import android.app.AlertDialog.Builder
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.os.Debug
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.widget.Toast

import com.culture.santos.culture.MapsActivity
import com.culture.santos.culture.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

import java.io.IOException
import java.util.Locale


/**
 * Created by Ricar on 13/08/2016.
 */
class GoogleMapAdapter(private val mMap: GoogleMap?, private val context: MapsActivity) {
    private var currentLocation: Location? = null

    val isEmpty: Boolean
        get() = this.mMap == null

    init {
        setUpMap()
        setMapEvents()
    }

    fun setMapEvents(){
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
        try {
            createMarker(latLng)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Throws(IOException::class)
    private fun createMarker(latLng: LatLng) {
        val gc = Geocoder(this.context.baseContext, Locale.getDefault())
        val fromLocation = gc.getFromLocation(latLng.latitude, latLng.longitude, 1)
        if (!fromLocation.isEmpty()) {
            val en = fromLocation[0]
            val currentMarker = this.mMap?.addMarker(MarkerOptions().position(latLng).title("Teste").snippet(en.getAddressLine(0)))
        }
    }

    private fun markerClickEvent(marker: Marker) {
        val builder = Builder(this.context)
        builder.setTitle(marker.title)
        builder.setMessage(marker.snippet)
        builder.setPositiveButton(R.string.remove) { dialog, id ->
            marker.remove()
        }
        builder.show()
    }

    fun setAlertDialogGPS() {
        if (this.context.haveGPSandNETWORK()) return

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
        if (!checkPermission()) return
        val provider = this.context.locationManager!!.getBestProvider(Criteria(), true)

        currentLocation = this.context.locationManager!!.getLastKnownLocation(provider)

        this.context.locationManager!!.requestLocationUpdates(provider, 0, 0f, object : LocationListener{
            override fun onLocationChanged(p0: Location?) {
                moveMapCamera(p0)
            }
            override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onProviderEnabled(p0: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onProviderDisabled(p0: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

        if (currentLocation != null){
            mMap?.moveCamera(CameraUpdateFactory.newLatLng(LatLng(currentLocation?.latitude!!, currentLocation?.longitude!!)))
        }

        mMap?.animateCamera(CameraUpdateFactory.zoomTo(5f))
        mMap?.uiSettings?.isZoomControlsEnabled = true
        mMap?.uiSettings?.isMyLocationButtonEnabled = true
        mMap?.isMyLocationEnabled = true
    }

    private fun moveMapCamera(location: Location?){
        var latLng = LatLng(location!!.getLatitude(), location!!.getLongitude())
        Log.d("State_LAT", latLng?.toString())
        mMap?.moveCamera(CameraUpdateFactory.newLatLng(latLng))
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

    companion object {
        private val REQUEST_CODE_CREATE_MARKER = 0x9345
    }
}
