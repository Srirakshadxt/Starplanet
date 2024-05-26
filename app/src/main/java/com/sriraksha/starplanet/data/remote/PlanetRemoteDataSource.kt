package com.sriraksha.starplanet.data.remote

import com.sriraksha.starplanet.data.model.PlanetResponse

/**
 * Defines the contract for retrieving planet data from planet API.
 */
interface PlanetRemoteDataSource {
    /**
     * Retrieves the list of planets.
     * @return The response containing the list of planets.
     */
    suspend fun getPlanets(): PlanetResponse

    /**
     * Retrieves the next page of planet data.
     * @param nextPageUrl The URL of the next page.
     * @return The response containing the next page of planets.
     */
    suspend fun getNextPage(nextPageUrl: String): PlanetResponse
}