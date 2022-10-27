package com.piri.probodia.data.remote.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TodayRecord(
    @SerializedName("data")
    val temp : MutableList<AllData>
) {

    private var datas : MutableList<RecordDatasBase>? = null

    fun getDatas() : MutableList<RecordDatasBase> {
        if (datas == null || datas?.size != temp.size) {
            datas = mutableListOf()
            initDatas()
        }

        return datas!!
    }

    private fun initDatas() {
        for(it in temp) {
            when(it.type) {
                "SUGAR" -> datas!!.add(GlucoseDto(it.type, it.record.getGlucoseRecord()))
                "PRESSURE" -> datas!!.add(PressureDto(it.type, it.record.getPressureRecord()))
                "MEDICINE" -> datas!!.add(MedicineDto(it.type, it.record.getMedicineRecord()))
                "MEAL" -> datas!!.add(MealDto(it.type, it.record.getMealRecord()))
            }
        }
    }

    data class AllData (
        override val type : String,

        val record : AllRecord
            ) : RecordDatasBase {
    }

    data class AllRecord(
        val timeTag : String,

        val recordId : Int,

        val recordDate : String,

        @SerializedName("bloodSugar")
        @Expose
        val glucose : Int?,

        @SerializedName("maxBloodPressure")
        @Expose
        val maxPressure : Int?,

        @SerializedName("minBloodPressure")
        @Expose
        val minPressure : Int?,

        @SerializedName("heartBeat")
        @Expose
        val heartRate : Int?,

        @Expose
        val medicineDetails : List<MedicineDto.Record.MedicineDetail>?,

        @Expose
        val mealDetails : List<MealDto.MealDetail>?,
    ) {
        fun getGlucoseRecord() : GlucoseDto.Record {
            return GlucoseDto.Record(
                timeTag,
                glucose!!,
                recordId,
                recordDate
            )
        }

        fun getPressureRecord() : PressureDto.Record {
            return PressureDto.Record(
                timeTag,
                maxPressure!!,
                minPressure!!,
                heartRate!!,
                recordId,
                recordDate
            )
        }

        fun getMedicineRecord() : MedicineDto.Record {
            return MedicineDto.Record(
                timeTag,
                recordId,
                recordDate,
                medicineDetails!!
            )
        }

        fun getMealRecord() : MealDto.Record {
            return MealDto.Record(
                timeTag,
                mealDetails!!,
                recordId,
                recordDate
            )
        }
    }

}
