package com.piri.probodia.data.remote.body

data class PutMedicineBody(
    var recordId : Int,
    var timeTag : String,
    var recordDate : String,
    var medicineDetails : List<MedicineDetail>
) {
    data class MedicineDetail(
        var medicineCnt : Int,
        var medicineName : String,
        var medicineId : String
    )
}
