package com.culture.santos.adapter

import com.culture.santos.culture.MapsActivity
import com.firebase.client.Firebase

/**
 * Created by Ricar on 13/08/2016.
 */
class FirebaseAdapter(context: MapsActivity) {

    private var fireBase: Firebase? = null
    private val BACKGROUND_FIREBASE_URL = "https://culture-7b369.firebaseio.com"

    init {
        Firebase.setAndroidContext(context)
    }

    fun defineFireBase() {
        this.fireBase = Firebase(BACKGROUND_FIREBASE_URL)
    }
}
