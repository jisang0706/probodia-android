package com.piri.probodia.data.remote.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class MedicineDto(
    override val type : String,

    val record : Record
) : RecordDatasBase {

    @Parcelize
    data class Record(
        val timeTag : String,

        val recordId : Int,

        val recordDate : String,

        val medicineDetails : List<MedicineDetail>
    ) : Parcelable {

        @Parcelize
        data class MedicineDetail(
            val medicineDetailId : Int,

            val medicineCnt : Int,

            val medicineName : String,

            val medicineId : String,
        ) : Parcelable
    }
}
