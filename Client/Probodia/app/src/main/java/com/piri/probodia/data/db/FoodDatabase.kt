package com.piri.probodia.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.piri.probodia.data.db.dao.FoodDao
import com.piri.probodia.data.db.entity.FoodEntity

@Database(entities = [FoodEntity::class], version = 1)
abstract class FoodDatabase : RoomDatabase() {

    abstract fun foodDao() : FoodDao

    companion object {
        @Volatile
        private var INSTANCE : FoodDatabase? = null

        fun getDatabase(
            context : Context
        ) : FoodDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FoodDatabase::class.java,
                    "food_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}