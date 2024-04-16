package com.jpmc.sriraksha.starplanet.data.local

import com.jpmc.sriraksha.starplanet.data.model.Planet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlanetLocalDataSource @Inject constructor(
    private val planetDao: PlanetDao
) {
    suspend fun savePlanets(planets: List<Planet>) {
        withContext(Dispatchers.IO) {
            planetDao.insertPlanets(planets)
        }
    }

    suspend fun getPlanets(): List<Planet> {
        return withContext(Dispatchers.IO) {
            planetDao.getAllPlanets()
        }
    }

    suspend fun getPlanetsCount(): Long {
        return withContext(Dispatchers.IO) {
            planetDao.getPlanetsCount()
        }
    }
}