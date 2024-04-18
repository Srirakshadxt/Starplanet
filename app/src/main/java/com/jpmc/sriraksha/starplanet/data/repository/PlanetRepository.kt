package com.jpmc.sriraksha.starplanet.data.repository

import com.jpmc.sriraksha.starplanet.data.model.PlanetResponse

interface PlanetRepository {
    suspend fun getPlanets(): Result<PlanetResponse>

    suspend fun getNextPage(nextPageUrl: String): Result<PlanetResponse>
}
