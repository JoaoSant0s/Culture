package com.culture.santos.module


import android.widget.Toast

import com.culture.santos.culture.MapsActivity

import java.util.Observable
import java.util.Observer

/**
 * Created by Ricar on 02/09/2016.
 */
class StateManager(var mapsContext: MapsActivity?, stateContext: State?) : Observer {

    init {
        if (stateContext != null) {
            addObserve(stateContext)
        }
    }

    private fun addObserve(stateContext: State) {
        stateContext.addObserver(this)
    }

    override fun update(observable: Observable?, data: Any?) {
        if(observable == null) return;

        val currentState = (observable as State).state
        val tutorialState = observable.stateTutorial
        val toastMessage = currentState.toString() + " " + tutorialState!!.toString()
        if(this.mapsContext != null){
            val toast = Toast.makeText(this.mapsContext, toastMessage, Toast.LENGTH_SHORT)
            toast.show()
        }
    }
}
