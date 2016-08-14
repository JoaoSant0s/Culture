package com.culture.santos.adapter;

import com.culture.santos.culture.MapsActivity;
import com.firebase.client.Firebase;

/**
 * Created by Ricar on 13/08/2016.
 */
public class FirebaseAdapter {

    private Firebase fireBase;
    private final String BACKGROUND_FIREBASE_URL = "https://culture-7b369.firebaseio.com";

    public FirebaseAdapter(MapsActivity context){
        Firebase.setAndroidContext(context);
    }

    public void defineFireBase(){
        fireBase = new Firebase(BACKGROUND_FIREBASE_URL);
    }
}
