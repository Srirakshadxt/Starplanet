package com.jpmc.sriraksha.starplanet.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jpmc.sriraksha.starplanet.data.model.Planet

@Dao
interface PlanetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlanets(planets: List<Planet>)

    @Query("SELECT * FROM planets")
    fun getAllPlanets(): List<Planet>

    @Query("SELECT COUNT(*) FROM planets")
    fun getPlanetsCount(): Long
}