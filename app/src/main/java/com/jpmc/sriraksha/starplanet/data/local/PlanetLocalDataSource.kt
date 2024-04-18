package com.jpmc.sriraksha.starplanet.data.local

import com.jpmc.sriraksha.starplanet.data.model.Planet

interface PlanetLocalDataSource {
    suspend fun savePlanets(planets: List<Planet>)

    suspend fun getPlanets(): List<Planet>

    suspend fun getPlanetsCount(): Long
}
