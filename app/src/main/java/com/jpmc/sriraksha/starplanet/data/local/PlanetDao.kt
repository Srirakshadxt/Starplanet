package com.jpmc.sriraksha.starplanet.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jpmc.sriraksha.starplanet.data.model.Planet

@Dao
interface PlanetDao {

    /**
     * @param planets
     *
     * Insert Planets to Room DB
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlanets(planets: List<Planet>)

    /**
     * @return List<Planets>
     *
     * Retrieves List of planets from Room DB
     */
    @Query("SELECT * FROM planets")
    fun getAllPlanets(): List<Planet>

    /**
     * @return long
     *
     * Gets the count of planers saved in Room DB
     */
    @Query("SELECT COUNT(*) FROM planets")
    fun getPlanetsCount(): Long
}


