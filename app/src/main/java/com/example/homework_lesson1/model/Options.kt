package com.example.homework_lesson1.model

import android.os.Parcel
import android.os.Parcelable

data class Options(val count: Int): Parcelable {

    constructor(parcel: Parcel) : this(parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(count)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Options> {
        @JvmStatic val DEFAULT = Options(count = 5)

        override fun createFromParcel(parcel: Parcel): Options {
            return Options(parcel)
        }

        override fun newArray(size: Int): Array<Options?> {
            return arrayOfNulls(size)
        }
    }
}
