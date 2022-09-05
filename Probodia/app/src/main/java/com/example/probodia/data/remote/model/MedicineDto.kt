package com.example.probodia.data.remote.model

data class MedicineDto(
    override val type : String,

    val record : Record
) : RecordDatasBase {
    data class Record(
        val timeTag : String,

        val recordId : Int,

        val recordDate : String,

        val medicineDetails : List<MedicineDetail>
    ) {
        data class MedicineDetail(
            val medicineCnt : Int,

            val medicineName : String,

            val medicineId : String
        )
    }
}
