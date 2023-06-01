package com.notesproject.notes

import android.os.Parcel
import android.os.Parcelable

data class Note ( val dataUser: String = "",
                  val dataTitle: String =  "",
                  val dataDesc: String =  "",
                  val dataDate: String  =  "",
                  val dataImage: String?  =  "") : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(dataUser)
        dest.writeString(dataTitle)
        dest.writeString(dataDesc)
        dest.writeString(dataDate)
        dest.writeString(dataImage)
    }

    companion object CREATOR : Parcelable.Creator<Note> {
        override fun createFromParcel(parcel: Parcel): Note {
            return Note(parcel)
        }

        override fun newArray(size: Int): Array<Note?> {
            return arrayOfNulls(size)
        }
    }

}