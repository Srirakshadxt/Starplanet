package com.jpmc.sriraksha.starplanet.data.local

import com.jpmc.sriraksha.starplanet.data.model.Planet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Concrete implementation of the PlanetLocalDataSource interface that uses a PlanetDao to perform CRUD operations on Planet entities.
 */
class PlanetLocalDataSourceImpl @Inject constructor(
    private val planetDao: PlanetDao
) : PlanetLocalDataSource {

    override suspend fun savePlanets(planets: List<Planet>) {
        withContext(Dispatchers.IO) {
            planetDao.insertPlanets(planets)
        }
    }

    override suspend fun getPlanets(): List<Planet> {
        return withContext(Dispatchers.IO) {
            planetDao.getAllPlanets()
        }
    }

    override suspend fun getPlanetsCount(): Long {
        return withContext(Dispatchers.IO) {
            planetDao.getPlanetsCount()
        }
    }
}