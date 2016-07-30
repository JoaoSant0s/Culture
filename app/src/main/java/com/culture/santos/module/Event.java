package com.culture.santos.module;

import android.location.Location;
import android.provider.ContactsContract;
import android.widget.ImageView;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Ricar on 29/07/2016.
 */
public class Event {
    private String name;
    private Date date;
    private Location location;

    private String reference;
    private String description;
    private String facebookLink;

    private ImageView eventCover;
    private Set<ImageView> eventPhotos;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFacebookLink() {
        return facebookLink;
    }

    public void setFacebookLink(String facebookLink) {
        this.facebookLink = facebookLink;
    }

    public ImageView getEventCover() {
        return eventCover;
    }

    public void setEventCover(ImageView eventCover) {
        this.eventCover = eventCover;
    }

    public Set<ImageView> getEventPhotos() {
        return eventPhotos;
    }

    public void setEventPhotos(Set<ImageView> eventPhotos) {
        this.eventPhotos = eventPhotos;
    }

    public Event(){
        eventPhotos = new HashSet<>();
    }
}
