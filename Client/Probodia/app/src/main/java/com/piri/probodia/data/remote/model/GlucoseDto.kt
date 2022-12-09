package com.piri.probodia.data.remote.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class GlucoseDto(
    override val type : String,

    val record : Record
) : RecordDatasBase {
    data class Record(
        val timeTag : String,

        @SerializedName("bloodSugar")
        val glucose : Int,

        val recordId : Int,

        val recordDate : String
    ) : Parcelable {
        constructor(parcel : Parcel) : this(
            parcel.readString()!!,
            parcel.readInt()!!,
            parcel.readInt()!!,
            parcel.readString()!!
        )

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel?, flags: Int) {
            dest?.writeString(timeTag)
            dest?.writeInt(glucose)
            dest?.writeInt(recordId)
            dest?.writeString(recordDate)
        }

        companion object CREATOR : Parcelable.Creator<Record> {
            override fun createFromParcel(source: Parcel): Record {
                return Record(source)
            }

            override fun newArray(size: Int): Array<Record?> {
                return arrayOfNulls(size)
            }

        }
    }
}
