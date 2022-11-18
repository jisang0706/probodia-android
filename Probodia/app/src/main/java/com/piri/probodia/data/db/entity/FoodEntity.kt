package com.piri.probodia.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "food_table")
data class FoodEntity (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id : Int,

    @ColumnInfo(name = "food_name")
    var foodName : String,

    @ColumnInfo(name = "food_id")
    var foodId : String
        )