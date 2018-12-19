package com.culture.santos.culture

import android.app.Activity
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.support.design.internal.BottomNavigationItemView
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.*
import com.culture.santos.adapter.FirebaseAdapter

import com.culture.santos.adapter.GoogleMapAdapter
import com.culture.santos.adapter.GoogleSignInAdapter
import com.culture.santos.module.Event
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

import java.util.Timer
import java.util.TimerTask

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, BottomNavigationView.OnNavigationItemReselectedListener, BottomNavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    val PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 2

    val ACTIVITY_CREATE_EVENT = 1

    private var drawerLayout: DrawerLayout? = null
    private var navegationViewHeader: View? = null
    private var navigationView: NavigationView? = null
    private var actionBar: ActionBar? = null

    private val handler = Handler()
    private var timer: Timer? = null
    private var timerTask: TimerTask? = null

    private var mMap: GoogleMapAdapter? = null
    private var googleSign: GoogleSignInAdapter? = null

    var locationManager: LocationManager? = null
    var googleApiClient: GoogleApiClient? = null

    private var bottomView : BottomNavigationView? = null
    private var calendarFrameLayout : FrameLayout? = null
    private var searchFrameLayout : FrameLayout? = null
    private var locationFrameLayout : FrameLayout? = null

    var blockScreen: FrameLayout? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        FirebaseAdapter.start(this)

        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        googleSign = GoogleSignInAdapter(this)

        googleApiClient = GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build()

        setMapFragmentEnvironment()
    }

    fun createMarkets(events: List<Event>){
        for (event in events){
            mMap!!.createFinalMarker(event)
        }
    }

    fun haveGPSandNETWORK(): Boolean {
        return locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER) && locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun setMapFragmentEnvironment() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onStart() {
        super.onStart()
        defineNavigationBar()
        setFrameFragmentEvents()
        setSelectEventLocation()
    }

    private fun setSelectEventLocation(){
        locationFrameLayout = findViewById<View>(R.id.location_panel) as FrameLayout

        var buttonCreateEvent = findViewById<Button>(R.id.button_create_event)
        var buttonCancelEvent = findViewById<Button>(R.id.button_cancel_event)

        buttonCreateEvent.setOnClickListener (){
            if(mMap!!.hasSavedLocation()){
                bottomView!!.visibility = View.VISIBLE
                createEventActivity()
            }else{
                Toast.makeText(this, "Selecione um local", Toast.LENGTH_LONG).show()
            }
        }

        buttonCancelEvent.setOnClickListener (){
            disableLocationScreen();
        }
    }

    private fun disableLocationScreen(){
        this.mMap!!.setIdleLocationState()
        mMap!!.removeSavedLocation()
        locationFrameLayout!!.visibility = View.INVISIBLE
        bottomView!!.visibility = View.VISIBLE
    }

    private fun setFrameFragmentEvents(){
        bottomView = findViewById<View>(R.id.bottom_navigation_menu) as BottomNavigationView
        calendarFrameLayout = findViewById<View>(R.id.menu_calendar_screen) as FrameLayout
        searchFrameLayout = findViewById<View>(R.id.menu_search_screen) as FrameLayout

        bottomView!!.setOnNavigationItemSelectedListener (this)
        bottomView!!.setOnNavigationItemReselectedListener (this)

        setCalendarClickEvents();
        setSearchClickEvents();

        calendarFrameLayout = findViewById<View>(R.id.menu_calendar_screen) as FrameLayout

        blockScreen = findViewById<View>(R.id.block_screen) as FrameLayout

        blockScreen!!.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                return true;
            }
        })
    }

    private fun setCalendarClickEvents(){

        var button = findViewById<Button>(R.id.btn_create_event)

        button.setOnClickListener (){
            this.mMap!!.setMapLocationState()
            bottomView!!.visibility = View.INVISIBLE
            locationFrameLayout!!.visibility = View.VISIBLE
            var item = findViewById<View>(R.id.menu_map_access) as BottomNavigationItemView
            item.setChecked(true)
            changeCalendarNavegationItemReselected()
        }
    }

    private fun setSearchClickEvents(){

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_map_access -> {
                calendarFrameLayout!!.visibility = View.INVISIBLE
                searchFrameLayout!!.visibility = View.INVISIBLE
            }
            R.id.menu_search_access -> {
                calendarFrameLayout!!.visibility = View.INVISIBLE
                searchFrameLayout!!.visibility = View.VISIBLE
            }
            R.id.menu_calendar_access -> {
                calendarFrameLayout!!.visibility = View.VISIBLE
                searchFrameLayout!!.visibility = View.INVISIBLE
            }
        }
        return true
    }

    override fun onNavigationItemReselected(item: MenuItem) {
        when (item.itemId) {
            R.id.menu_map_access -> {}
            R.id.menu_search_access -> {
                changeSearchNavegationItemReselected()
            }
            R.id.menu_calendar_access -> {
                changeCalendarNavegationItemReselected()
            }
        }
    }

    private fun changeSearchNavegationItemReselected(){
        if(searchFrameLayout!!.visibility == View.VISIBLE){
            searchFrameLayout!!.visibility = View.INVISIBLE
        }else{
            searchFrameLayout!!.visibility = View.VISIBLE
        }
    }

    private fun changeCalendarNavegationItemReselected(){
        if(calendarFrameLayout!!.visibility == View.VISIBLE){
            calendarFrameLayout!!.visibility = View.INVISIBLE
        }else{
            calendarFrameLayout!!.visibility = View.VISIBLE
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        googleSign!!.handleResult(requestCode, data)

        Log.d("RESULT_TAG", requestCode.toString())

        mMap?.handleResult(requestCode, resultCode, data)

        if (googleSign!!.isSuccess) {
            FirebaseAdapter.marketsCallbacks(::createMarkets)
        }

        if (!googleSign!!.isSuccess) return
        defineUserData()

        if(requestCode == ACTIVITY_CREATE_EVENT){
            if (resultCode == Activity.RESULT_OK) {
                if(data == null) return

                var event = data.getParcelableExtra<Event>("EXTRA_REPLY")
                disableLocationScreen()
                //mMap!!.createFinalMarker(event!!)

                Toast.makeText(this, "Evento Criado", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this, "Processo cancelado", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        //menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id) {
            R.id.home -> {
                drawerLayout!!.openDrawer(GravityCompat.START)
                return true
            }
            R.id.action_tutorial -> {
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = GoogleMapAdapter(googleMap, this)
    }

    override fun onConnected(p0: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnectionSuspended(p0: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mMap?.setUpMap()
                    //mMap?.setAlertDialogGPS()
                } else {
                    Log.d("MUST ACCEPT", "MUST ACCEPT")
                }
                return
            }
            PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mMap?.setUpMap()
                    //mMap?.setAlertDialogGPS()
                } else {
                    Log.d("MUST ACCEPT", "MUST ACCEPT")
                }
                return
            }
        }
    }

    fun signIn(signInIntent: Intent, rcSign: Int) {
        startActivityForResult(signInIntent, rcSign)
    }

    private fun defineUserData() {
        val currentAccount = googleSign?.currentAccount
        updateUserData("email", currentAccount!!.email)
        updateUserData("name", currentAccount!!.displayName)
    }

    private fun updateUserData(key: String, model: String?) {

        val id: Int
        when (key) {
            "email" -> id = R.id.user_email_text
            "name" -> id = R.id.user_name_text
            else -> id = 0
        }
        val text = navegationViewHeader!!.findViewById<View>(id) as TextView
        text.text = model
    }

    private fun defineNavigationBar() {
        actionBar = supportActionBar
        actionBar?.setHomeAsUpIndicator(R.drawable.hamburguer)
        actionBar?.setDisplayHomeAsUpEnabled(false)
        drawerLayout = findViewById<View>(R.id.navigation_drawer_layout) as DrawerLayout

        navigationView = findViewById<View>(R.id.navigation_view) as NavigationView
        navegationViewHeader = navigationView!!.getHeaderView(0)

        var button = findViewById<ImageButton>(R.id.button)

        button.setOnClickListener (){
            drawerLayout?.openDrawer(GravityCompat.START)
        }
    }

    fun createEventActivity(){
        val intent = Intent(this, EventActivity::class.java)
        intent.putExtra("EXTRA_MESSAGE", this.mMap!!.savedLocationMarker!!.snippet)
        //intent.putExtra("EXTRA_MESSAGE_1",)
        intent.putExtra("EXTRA_MESSAGE_2", this.mMap!!.savedLocationMarker!!.position)
        startActivityForResult(intent, ACTIVITY_CREATE_EVENT);
    }

    fun alertDialogGPS(intent: Intent) {
        startActivity(intent)
    }
}
