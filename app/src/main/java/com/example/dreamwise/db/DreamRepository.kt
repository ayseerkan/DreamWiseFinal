package com.example.dreamwise

import androidx.lifecycle.LiveData

class DreamRepository(private val dreamDao: DreamDao) {

    val happyDreams: LiveData<List<Dream>> = dreamDao.getHappyDreams()
    val nightmares: LiveData<List<Dream>> = dreamDao.getNightmares()

    suspend fun insert(dream: Dream) {
        dreamDao.insert(dream)
    }
}
