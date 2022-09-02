package com.example.probodia.data.remote.model

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
                ) : ApiItemName
    }
}