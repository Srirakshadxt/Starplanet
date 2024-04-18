package com.jpmc.sriraksha.starplanet.data.remote

import com.jpmc.sriraksha.starplanet.data.model.PlanetResponse

interface PlanetRemoteDataSource {
    suspend fun getPlanets(): PlanetResponse
    suspend fun getNextPage(nextPageUrl: String): PlanetResponse
}