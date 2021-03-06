package com.culture.santos.module;

import android.util.Log;

import com.culture.santos.culture.MapsActivity;

import java.util.Observable;

/**
 * Created by Ricar on 02/09/2016.
 */
public class State extends Observable {

    public enum States{
        InitState,
        AddEvent,
        RemoveState,
        ListState,
        EditState
    }

    public enum StatesTutorial{
        Disable,
        AddEvent
    }

    private States currentState;
    private StatesTutorial currentStateTutorial;

    private StateManager stateManager;
    private MapsActivity cxt;

    public States getState(){
        return currentState;
    }
    public StatesTutorial getStateTutorial(){
        return currentStateTutorial;
    }

    public State(MapsActivity initContext){
        cxt = initContext;
        stateManager = new StateManager(initContext, this);
        setInitState();
    }

    public void setAddEventState(){
        if(isAddEventStateTutorial()) cxt.getTutorialAdapter().showAddEventTutorial();
        synchronized (this) {currentState = States.AddEvent;}
        notifyChanged();
    }

    public void setInitState(){
        synchronized (this) {
            currentState = States.InitState;
            currentStateTutorial = StatesTutorial.Disable;
        }
        notifyChanged();
    }

    public void setRemoveEventState() {
        synchronized (this) {currentState = States.RemoveState;}
        notifyChanged();
    }

    public void setEditEventState() {
        synchronized (this) {currentState = States.EditState;}
        notifyChanged();
    }

    public void setListEventState() {
        synchronized (this) {currentState = States.ListState;}
        notifyChanged();
    }

    public void setDisableTutorial(){
        synchronized (this) {currentStateTutorial = StatesTutorial.Disable;}
        notifyChanged();
    }

    public void setAddEventTutorial(){
        synchronized (this) {
            Log.d("BEFORE", currentStateTutorial.toString());
            if(isDisableEventStateTutorial()){
                currentStateTutorial = StatesTutorial.AddEvent;
            }else if(isAddEventStateTutorial()){
                currentStateTutorial = StatesTutorial.Disable;
            }
            Log.d("AFTER", currentStateTutorial.toString());
        }
        notifyChanged();
    }

    public boolean isAddEventStateTutorial(){return (currentStateTutorial == StatesTutorial.AddEvent);}

    public boolean isDisableEventStateTutorial(){return (currentStateTutorial == StatesTutorial.Disable);}

    public boolean isAddEventState(){
        return (currentState == States.AddEvent);
    }

    public boolean isRemoveEventState(){
        return (currentState == States.RemoveState);
    }

    private void notifyChanged(){
        setChanged();
        notifyObservers();
    }
}
