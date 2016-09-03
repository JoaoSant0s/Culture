package com.culture.santos.adapter;

import com.culture.santos.module.Event;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ricar on 03/09/2016.
 */
public class EventAdapter {
    private List<Event> events;

    private Marker auxMarker;

    public EventAdapter(){
        this.events = new ArrayList<>();
    }

    public Marker getMarker(){
        return auxMarker;
    }

    public void setMarker(Marker currentMarker){
        auxMarker = currentMarker;
    }

    public void addEvent(Event e){
        events.add(e);
    }

    public boolean cointainsEvent(Event e){
        return events.contains(e);
    }

    public Event getEvent(int position){
        return events.get(position);
    }

}
