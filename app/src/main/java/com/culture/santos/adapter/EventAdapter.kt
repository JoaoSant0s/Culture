package com.culture.santos.adapter

import com.culture.santos.module.Event
import com.google.android.gms.maps.model.Marker

import java.util.ArrayList

/**
 * Created by Ricar on 03/09/2016.
 */
class EventAdapter {
    private val events: MutableList<Event>

    var marker: Marker? = null

    init {
        this.events = ArrayList()
    }

    fun addEvent(e: Event) {
        events.add(e)
    }

    fun cointainsEvent(e: Event): Boolean {
        return events.contains(e)
    }

    fun getEvent(position: Int): Event {
        return events[position]
    }

}
