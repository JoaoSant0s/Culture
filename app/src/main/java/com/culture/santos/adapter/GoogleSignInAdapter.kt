package com.culture.santos.adapter

import android.content.Intent
import android.util.Log

import com.culture.santos.culture.MapsActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.api.GoogleApiClient

/**
 * Created by Ricar on 13/08/2016.
 */
class GoogleSignInAdapter(private val context: MapsActivity) {

    private val RC_SIGN_IN = 10
    var currentAccount: GoogleSignInAccount? = null
        private set
    private var mGoogleApiClient: GoogleApiClient? = null

    val isSuccess: Boolean
        get() = currentAccount != null

    init {
        setGoogleEnviroment()
    }

    private fun setGoogleEnviroment() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        mGoogleApiClient = GoogleApiClient.Builder(this.context)
                .enableAutoManage(this.context, this.context /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()

        signIn()
    }

    private fun signIn() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        context.signIn(signInIntent, RC_SIGN_IN)
    }

    fun handleResult(requestCode: Int, data: Intent?) {
        Log.d("requestCode ", "" + RC_SIGN_IN)
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            handleSignInResult(result)
        }
    }

    private fun handleSignInResult(result: GoogleSignInResult) {
        if (result.isSuccess) {
            currentAccount = result.signInAccount
        } else {
            currentAccount = null
            Log.d("Error", "Erro in login")
        }
    }
}
