package com.culture.santos.adapter;

import android.content.Intent;
import android.util.Log;

import com.culture.santos.culture.MapsActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by Ricar on 13/08/2016.
 */
public class GoogleSignInAdapter {

    private final int RC_SIGN_IN = 10;
    private GoogleSignInAccount currentAccount;
    private GoogleApiClient mGoogleApiClient;
    private MapsActivity context;

    public GoogleSignInAdapter(MapsActivity context){
        this.context = context;
        setGoogleEnviroment();
    }

    public boolean isSuccess(){
        return currentAccount != null;
    }

    public GoogleSignInAccount getCurrentAccount(){
        return currentAccount;
    }

    private void setGoogleEnviroment(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this.context)
                .enableAutoManage(this.context, this.context /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signIn();
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        context.signIn(signInIntent, RC_SIGN_IN);
    }

    public void handleResult(int requestCode, Intent data) {
        Log.d("requestCode ", "" + RC_SIGN_IN);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            currentAccount = result.getSignInAccount();
        }else{
            currentAccount = null;
            Log.d("Error", "Erro in login");
        }
    }
}
