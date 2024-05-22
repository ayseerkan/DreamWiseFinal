package com.example.dreamwise

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DreamDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(dream: Dream)

    @Query("SELECT * FROM dreams WHERE isNightmare = 0")
    fun getHappyDreams(): LiveData<List<Dream>>

    @Query("SELECT * FROM dreams WHERE isNightmare = 1")
    fun getNightmares(): LiveData<List<Dream>>
}
