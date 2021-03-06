package com.culture.santos.culture;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.culture.santos.adapter.EventAdapter;
import com.culture.santos.adapter.FirebaseAdapter;
import com.culture.santos.adapter.GoogleMapAdapter;
import com.culture.santos.adapter.GoogleSignInAdapter;
import com.culture.santos.adapter.TutorialAdapter;
import com.culture.santos.module.State;
import com.culture.santos.module.StateManager;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.Timer;
import java.util.TimerTask;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {

    public final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    public final int PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 2;
    private DrawerLayout drawerLayout;
    private View navegationViewHeader;
    private NavigationView navigationView;
    private ActionBar actionBar;

    private final Handler handler = new Handler();
    private Timer timer;
    private TimerTask timerTask;

    private GoogleMapAdapter mMap;
    private FirebaseAdapter fireBase;
    private GoogleSignInAdapter googleSign;
    private EventAdapter eventsAdapter;
    private TutorialAdapter tutorialAdapter;

    private State state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        fireBase = new FirebaseAdapter(this);
        googleSign = new GoogleSignInAdapter(this);
        eventsAdapter = new EventAdapter();
        tutorialAdapter = new TutorialAdapter(this);

        state = new State(this);

        setMapFragmentEnvironment();

    }

    public State getState(){
        return state;
    }
    public EventAdapter getEventsAdapter(){return eventsAdapter;}
    public TutorialAdapter getTutorialAdapter(){return tutorialAdapter;}

    private void setMapFragmentEnvironment(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onStart(){
        super.onStart();
        defineNavigationBar();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        googleSign.handleResult(requestCode, data);

        mMap.handleResult(requestCode, resultCode, data);
        if(!googleSign.isSuccess()) return;
        defineUserData();
        fireBase.defineFireBase();
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(mMap == null)return;
        if(mMap.isEmpty()) return;
        startTimer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_tutorial:
                state.setAddEventTutorial();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = new GoogleMapAdapter(googleMap, this, this.LOCATION_SERVICE);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("onConnectionFailed", "True");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mMap.setUpMap();
                } else {
                    Log.d("MUST ACCEPT", "MUST ACCEPT");
                }
                return;
            }
            case PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION:{

            }
        }
    }

    public void signIn(Intent signInIntent, int rcSign) {
        startActivityForResult(signInIntent, rcSign);
    }

    private void defineUserData() {
        GoogleSignInAccount currentAccount = googleSign.getCurrentAccount();
        updateUserData("email", currentAccount.getEmail());
        updateUserData("name", currentAccount.getDisplayName());
    }

    private void updateUserData(String key, String model){

        int id;
        switch (key){
            case "email":
                id = R.id.user_email_text;
                break;
            case "name":
                id = R.id.user_name_text;
                break;
            default:
                id = 0;
                break;
        }
        TextView text = (TextView) navegationViewHeader.findViewById(id);
        text.setText(model);
    }

    private void defineNavigationBar(){
        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.hamburguer);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.navigation_drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navegationViewHeader = navigationView.getHeaderView(0);

        setupNavigationDrawerContent(navigationView);
    }

    private void setupNavigationDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.list_events:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        state.setListEventState();
                        return true;
                    case R.id.add_event:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        state.setAddEventState();
                        return true;
                    case R.id.remove_event:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        state.setRemoveEventState();
                        return true;
                    case R.id.edit_event:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        state.setEditEventState();
                        return true;
                }
                return true;
            }
        });
    }

    public void alertDialogGPS(Intent intent){
        startActivity(intent);
    }

    private void setMapActions(){}

    private void startTimer() {
        timer = new Timer();
        initializeTimerTask();
        timer.schedule(timerTask, 1000, 2000); //
    }

    private void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                //use a handler to run a toast that shows the current timestamp
                handler.post(new Runnable() {
                    public void run() {
                        mMap.setUpMap();
                        timer.cancel();
                        timerTask.cancel();
                    }
                });
            }
        };
    }



}
