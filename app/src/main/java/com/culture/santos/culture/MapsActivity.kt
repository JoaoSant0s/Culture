package com.culture.santos.culture

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView

import com.culture.santos.adapter.EventAdapter
import com.culture.santos.adapter.FirebaseAdapter
import com.culture.santos.adapter.GoogleMapAdapter
import com.culture.santos.adapter.GoogleSignInAdapter
import com.culture.santos.adapter.TutorialAdapter
import com.culture.santos.module.State
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

import java.util.Timer
import java.util.TimerTask

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {

    val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    val PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 2
    private var drawerLayout: DrawerLayout? = null
    private var navegationViewHeader: View? = null
    private var navigationView: NavigationView? = null
    private var actionBar: ActionBar? = null

    private val handler = Handler()
    private var timer: Timer? = null
    private var timerTask: TimerTask? = null

    private var mMap: GoogleMapAdapter? = null
    private var fireBase: FirebaseAdapter? = null
    private var googleSign: GoogleSignInAdapter? = null
    var eventsAdapter: EventAdapter? = null
        private set
    var tutorialAdapter: TutorialAdapter? = null
        private set

    var state: State? = null
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        state = State(this)

        fireBase = FirebaseAdapter(this)
        googleSign = GoogleSignInAdapter(this)
        eventsAdapter = EventAdapter()
        tutorialAdapter = TutorialAdapter(this)

        setMapFragmentEnvironment()
    }

    private fun setMapFragmentEnvironment() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onStart() {
        super.onStart()
        defineNavigationBar()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        googleSign!!.handleResult(requestCode, data)

        mMap?.handleResult(requestCode, resultCode, data)
        if (!googleSign!!.isSuccess) return
        defineUserData()
        fireBase?.defineFireBase()
    }

    override fun onResume() {
        super.onResume()
        if (mMap == null) return
        if (!mMap?.isEmpty!!) return
        startTimer()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
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
                state?.setAddEventTutorial()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = GoogleMapAdapter(googleMap, this, Context.LOCATION_SERVICE)
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.d("onConnectionFailed", "True")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mMap?.setUpMap()
                } else {
                    Log.d("MUST ACCEPT", "MUST ACCEPT")
                }
                return
            }
            PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mMap?.setUpMap()
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
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeButtonEnabled(true)

        drawerLayout = findViewById<View>(R.id.navigation_drawer_layout) as DrawerLayout

        navigationView = findViewById<View>(R.id.navigation_view) as NavigationView
        navegationViewHeader = navigationView!!.getHeaderView(0)

        setupNavigationDrawerContent(navigationView!!)
    }

    private fun setupNavigationDrawerContent(navigationView: NavigationView) {
        navigationView.setNavigationItemSelectedListener(NavigationView.OnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.list_events -> {
                    drawerLayout?.closeDrawer(GravityCompat.START)
                    state?.setListEventState()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.add_event -> {
                    drawerLayout?.closeDrawer(GravityCompat.START)
                    state?.setAddEventState()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.remove_event -> {
                    drawerLayout?.closeDrawer(GravityCompat.START)
                    state?.setRemoveEventState()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.edit_event -> {
                    drawerLayout?.closeDrawer(GravityCompat.START)
                    state?.setEditEventState()
                    return@OnNavigationItemSelectedListener true
                }
            }
            true
        })
    }

    fun alertDialogGPS(intent: Intent) {
        startActivity(intent)
    }

    private fun setMapActions() {}

    private fun startTimer() {
        timer = Timer()
        initializeTimerTask()
        timer!!.schedule(timerTask, 1000, 2000) //
    }

    private fun initializeTimerTask() {
        timerTask = object : TimerTask() {
            override fun run() {
                //use a handler to run a toast that shows the current timestamp
                handler.post {
                    mMap!!.setUpMap()
                    timer!!.cancel()
                    timerTask!!.cancel()
                }
            }
        }
    }


}
