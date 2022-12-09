package com.piri.probodia.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.piri.probodia.data.db.entity.FoodEntity

@Dao
interface FoodDao {

    @Query("SELECT * FROM food_table ORDER BY id DESC")
    fun getAllFood() : List<FoodEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(food : FoodEntity)

    @Query("DELETE FROM food_table WHERE food_id = :foodId")
    fun deleteByFoodId(foodId : String)

    @Query("DELETE FROM food_table WHERE id NOT IN (SELECT id FROM food_table ORDER BY id DESC LIMIT 20)")
    fun deletePast20()
}