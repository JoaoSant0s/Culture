package com.culture.santos.module

import android.os.Parcel
import android.os.Parcelable

class Event() : Parcelable {

    var name: String = ""
    var startDate: Long = 0
    var endDate: Long = 0
    var location: String = ""
    var description : String = ""
    var externalLink : String = ""
    var latitude : Double = 0.0
    var longitude : Double = 0.0

    constructor(parcel: Parcel) : this() {
        name = parcel.readString()
        startDate = parcel.readLong()
        endDate = parcel.readLong()
        location = parcel.readString()
        description = parcel.readString()
        externalLink = parcel.readString()
        latitude  = parcel.readDouble()
        longitude  = parcel.readDouble()
    }

    constructor(newName : String, newStartDate : Long, newEndDate : Long, newLocation : String, newDescription : String, newExternalLink : String, newLatitude : Double, newLongitude : Double) : this() {
        this.name = newName
        this.startDate = newStartDate
        this.endDate = newEndDate
        this.location = newLocation
        this.description = newDescription
        this.externalLink = newExternalLink
        this.latitude  = newLatitude
        this.longitude  = newLongitude
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeLong(startDate)
        parcel.writeLong(endDate)
        parcel.writeString(location)
        parcel.writeString(description)
        parcel.writeString(externalLink)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Event> {
        override fun createFromParcel(parcel: Parcel): Event {
            return Event(parcel)
        }

        override fun newArray(size: Int): Array<Event?> {
            return arrayOfNulls(size)
        }
    }
}