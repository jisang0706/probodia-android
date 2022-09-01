package com.example.probodia.data.remote.model

import com.google.gson.annotations.SerializedName

data class ApiMedicineDto (
    val body : Body
        ) {
    data class Body(
        val items : MutableList<MedicineItem>
    ) {
        data class MedicineItem (
            @SerializedName("ITEM_SEQ")
            val item_seq : String,

            @SerializedName("ITEM_NAME")
            val item_name : String,

            @SerializedName("ENTP_SEQ")
            val entp_seq : String,

            @SerializedName("ENTP_NAME")
            val entp_name : String,

            @SerializedName("CHART")
            val chart : String,

            @SerializedName("ITEM_IMAGE")
            val item_image : String
                )
    }
}