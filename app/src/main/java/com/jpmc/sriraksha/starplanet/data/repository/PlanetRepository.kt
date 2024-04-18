package com.jpmc.sriraksha.starplanet.data.repository

import com.jpmc.sriraksha.starplanet.data.model.PlanetResponse

/**
 * Defines the contract for accessing planet data from a repository.
 * This interface is separated from its implementation to facilitate mocking and unit testing.
 */
interface PlanetRepository {
    /**
     * Retrieves the list of planets.
     * @return The result containing the planet response or an error.
     */
    suspend fun getPlanets(): Result<PlanetResponse>

    /**
     * Retrieves the next page of planet data.
     * @param nextPageUrl The URL of the next page.
     * @return The result containing the planet response or an error.
     */
    suspend fun getNextPage(nextPageUrl: String): Result<PlanetResponse>
}
