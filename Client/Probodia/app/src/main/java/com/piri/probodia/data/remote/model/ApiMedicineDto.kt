package com.piri.probodia.data.remote.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ApiMedicineDto (
    @SerializedName("body")
    val body : Body
        ) {
    data class Body(
        @SerializedName("items")
        val items : List<MedicineItem>
    ) {
        data class MedicineItem (
            @SerializedName("ITEM_SEQ")
            val item_seq : String,

            @SerializedName("ITEM_NAME")
            override val itemName : String,

            @SerializedName("ENTP_SEQ")
            val entp_seq : String,

            @SerializedName("ENTP_NAME")
            val entp_name : String,

            @SerializedName("CHART")
            val chart : String,

            @SerializedName("ITEM_IMAGE")
            val item_image : String
        ) : Parcelable, ApiItemName {

            var unit : Int = 1

            constructor(parcel : Parcel) : this(
                parcel.readString()!!,
                parcel.readString()!!,
                parcel.readString()!!,
                parcel.readString()!!,
                parcel.readString()!!,
                parcel.readString()!!
                    )

            override fun describeContents(): Int = 0

            override fun writeToParcel(dest: Parcel?, flags: Int) {
                dest?.writeString(item_seq)
                dest?.writeString(itemName)
                dest?.writeString(entp_seq)
                dest?.writeString(entp_name)
                dest?.writeString(chart)
                dest?.writeString(item_image)
            }

            companion object CREATOR : Parcelable.Creator<MedicineItem> {
                override fun createFromParcel(source: Parcel): MedicineItem {
                    return MedicineItem(source)
                }

                override fun newArray(size: Int): Array<MedicineItem?> {
                    return arrayOfNulls(size)
                }

            }
        }
    }
}