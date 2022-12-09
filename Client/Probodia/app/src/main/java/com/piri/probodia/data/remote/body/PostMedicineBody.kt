package com.piri.probodia.data.remote.body

data class PostMedicineBody (
    var timeTag : String,
    var recordDate : String,
    var medicineDetails : List<PostMedicineItem>
        ) {
    data class PostMedicineItem (
        var medicineId : String,
        var medicineName : String,
        var medicineCnt : Int,
            )
}