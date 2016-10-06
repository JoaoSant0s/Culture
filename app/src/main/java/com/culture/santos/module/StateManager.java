package com.culture.santos.module;

import android.util.Log;
import android.widget.Toast;

import com.culture.santos.culture.MapsActivity;

import java.util.Observable;
import java.util.Observer;
import com.culture.santos.module.State.States;
import com.culture.santos.module.State.StatesTutorial;

/**
 * Created by Ricar on 02/09/2016.
 */
public class StateManager implements Observer {

    MapsActivity mapsContext;

    public StateManager (MapsActivity context, State stateContext){
        this.mapsContext = context;
        addObserve(stateContext);
    }

    private void addObserve(State stateContext){
        stateContext.addObserver(this);
    }

    @Override
    public void update(Observable observable, Object data) {
        States currentState = ((State) observable).getState();
        StatesTutorial tutorialState = ((State) observable).getStateTutorial();
        String toastMessage = currentState.toString() + " " +  tutorialState.toString();

        Toast toast = Toast.makeText(this.mapsContext, toastMessage, Toast.LENGTH_SHORT);
        toast.show();
    }
}
