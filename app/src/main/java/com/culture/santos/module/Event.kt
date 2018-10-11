package com.culture.santos.module

import android.location.Location
import android.widget.ImageView

import java.util.Calendar
import java.util.HashSet

/**
 * Created by Ricar on 29/07/2016.
 */
class Event(var name: String?, var date: Calendar?, var location: Location?, var reference: String?, var description: String?, var facebookLink: String?) {

    var eventCover: ImageView? = null
    var eventPhotos: Set<ImageView>? = null

    init {

        eventPhotos = HashSet()
    }

}
