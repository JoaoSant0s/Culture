package com.culture.santos.adapter

import android.app.Activity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import android.content.ContentValues.TAG
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.AuthResult
import com.culture.santos.module.Event
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*

class FirebaseAdapter() {

    companion object {

        private val RC_SIGN_IN = 10
        var mAuth : FirebaseAuth? = null
        var mDatabase : FirebaseDatabase? = null
        var mDbRefEvents : DatabaseReference? = null
        var eventList : MutableList<Event> = mutableListOf()

        var context : Activity? = null
        var firebaseUser : FirebaseUser? = null
        var callbackEvents : ((List<Event>) -> Unit)? = null

        fun start(newContext: Activity){
            mAuth = FirebaseAuth.getInstance()
            mDatabase = FirebaseDatabase.getInstance()
            context = newContext
        }

        fun marketsCallbacks(newCallbackEvents: (List<Event>) -> Unit){
            callbackEvents = newCallbackEvents
            setDatabaseReferences()
        }

        fun setDatabaseReferences(){
            mDbRefEvents = this.mDatabase!!.getReference("events")

            val menuListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    eventList.clear()

                    for (child in dataSnapshot.children){
                        for (event in child.children){
                            var eventValue = event.getValue(Event::class.java)
                            eventList.add(eventValue!!)
                        }
                    }
                    if(callbackEvents != null) callbackEvents!!(eventList)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    println("loadPost:onCancelled ${databaseError.toException()}")
                }
            }
            mDbRefEvents!!.addListenerForSingleValueEvent(menuListener)
        }

        fun firebaseAuthWithGoogle(acct: GoogleSignInAccount?) {
            Log.d(TAG, "firebaseAuthWithGoogle: " + acct!!.id)

            val credential = GoogleAuthProvider.getCredential(acct!!.idToken, null)

            mAuth!!.signInWithCredential(credential).addOnCompleteListener(context!!, object : OnCompleteListener<AuthResult> {
                override fun onComplete(task: Task<AuthResult>) {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithCredential:success")
                        firebaseUser = mAuth!!.getCurrentUser()
                    } else {
                        Log.w(TAG, "signInWithCredential:failure", task.getException())
                    }
                }
            })
        }

        fun saveEventInUser(event : Event, callbacks: (() -> Unit)){
            var key = mDbRefEvents!!.child(firebaseUser!!.uid).push().key
            mDbRefEvents!!.child(firebaseUser!!.uid).child(key!!).setValue(event)
            callbacks()
        }
    }
}