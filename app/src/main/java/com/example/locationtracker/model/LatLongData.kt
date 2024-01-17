package com.example.locationtracker.model

import android.os.Parcel
import android.os.Parcelable

data class LatLongData(val latitude:Double,val longitude:Double):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readDouble(),
        parcel.readDouble()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LatLongData> {
        override fun createFromParcel(parcel: Parcel): LatLongData {
            return LatLongData(parcel)
        }

        override fun newArray(size: Int): Array<LatLongData?> {
            return arrayOfNulls(size)
        }
    }
}