package com.example.probodia.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel

class RecordViewModel : ViewModel() {

    fun recordGlucose() {
        Log.e("RECORD", "Glucose")
    }

    fun recordPressure() {
        Log.e("RECORD", "Pressure")
    }

    fun recordMedicine() {
        Log.e("RECORD", "Medicine")
    }

    fun recordFood() {
        Log.e("RECORD", "Food")
    }
}