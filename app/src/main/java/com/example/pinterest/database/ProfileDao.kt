package com.example.pinterest.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.pinterest.database.model.Profile
@Dao
interface ProfileDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveProduct(saved: Profile)

    @Query("SELECT * FROM Saved")
    fun getSaved(): List<Profile>
}
