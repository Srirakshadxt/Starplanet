package com.jpmc.sriraksha.starplanet.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jpmc.sriraksha.starplanet.data.model.Planet

/**
 * Abstract Room database class that manages Planet entities with a PlanetDao for CRUD operations.
 */
@Database(entities = [Planet::class], version = 1)
abstract class PlanetDatabase : RoomDatabase() {
    /**
     * Returns an instance of the PlanetDao interface for performing CRUD operations on Planet entities.
     *
     * @return an instance of the PlanetDao interface
     */
    abstract fun planetDao(): PlanetDao
}