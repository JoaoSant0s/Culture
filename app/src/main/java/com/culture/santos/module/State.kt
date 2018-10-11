package com.culture.santos.module

import android.util.Log
import com.culture.santos.culture.MapsActivity
import java.util.Observable

/**
 * Created by Ricar on 02/09/2016.
 */
class State(private val cxt: MapsActivity?) : Observable() {

    var state: States? = null
        private set
    var stateTutorial: StatesTutorial? = null
        private set

    private val stateManager: StateManager

    val isAddEventStateTutorial: Boolean
        get() = stateTutorial == StatesTutorial.AddEvent

    val isDisableEventStateTutorial: Boolean
        get() = stateTutorial == StatesTutorial.Disable

    val isAddEventState: Boolean
        get() = state == States.AddEvent

    val isRemoveEventState: Boolean
        get() = state == States.RemoveState

    enum class States {
        InitState,
        AddEvent,
        RemoveState,
        ListState,
        EditState
    }

    enum class StatesTutorial {
        Disable,
        AddEvent
    }

    init {
        stateManager = StateManager(cxt, this)
        setInitState()
    }

    fun setAddEventState() {
        if (isAddEventStateTutorial) cxt?.tutorialAdapter!!.showAddEventTutorial()
        synchronized(this) {
            state = States.AddEvent
        }
        notifyChanged()
    }

    fun setInitState() {
        synchronized(this) {
            state = States.InitState
            stateTutorial = StatesTutorial.Disable
        }
        notifyChanged()
    }

    fun setRemoveEventState() {
        synchronized(this) {
            state = States.RemoveState
        }
        notifyChanged()
    }

    fun setEditEventState() {
        synchronized(this) {
            state = States.EditState
        }
        notifyChanged()
    }

    fun setListEventState() {
        synchronized(this) {
            state = States.ListState
        }
        notifyChanged()
    }

    fun setDisableTutorial() {
        synchronized(this) {
            stateTutorial = StatesTutorial.Disable
        }
        notifyChanged()
    }

    fun setAddEventTutorial() {
        synchronized(this) {
            Log.d("BEFORE", stateTutorial!!.toString())
            if (isDisableEventStateTutorial) {
                stateTutorial = StatesTutorial.AddEvent
            } else if (isAddEventStateTutorial) {
                stateTutorial = StatesTutorial.Disable
            }
            Log.d("AFTER", stateTutorial!!.toString())
        }
        notifyChanged()
    }

    private fun notifyChanged() {
        setChanged()
        notifyObservers()
    }
}
