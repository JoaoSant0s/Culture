package com.culture.santos.module;

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

    public States currentState;

    private StateManager stateManager;

    public State(MapsActivity initContext){
        stateManager = new StateManager(initContext, this);
        setInitState();
    }

    public States getState(){
        return currentState;
    }

    public void setAddEventState(){
        synchronized (this) {currentState = States.AddEvent;}
        notifyChanged();
    }

    public void setInitState(){
        synchronized (this) {currentState = States.InitState;}
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
