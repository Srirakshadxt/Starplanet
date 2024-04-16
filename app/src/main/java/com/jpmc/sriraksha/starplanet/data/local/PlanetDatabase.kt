package com.jpmc.sriraksha.starplanet.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jpmc.sriraksha.starplanet.data.model.Planet

@Database(entities = [Planet::class], version = 1)
abstract class PlanetDatabase : RoomDatabase() {
    abstract fun planetDao(): PlanetDao
}