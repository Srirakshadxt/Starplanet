package com.jpmc.sriraksha.starplanet.data.remote

import com.jpmc.sriraksha.starplanet.data.model.PlanetResponse
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * Defines the API endpoints for retrieving planet data.
 */
interface PlanetApi {

    /**
     * Retrieves the list of planets.
     * @return The response containing the list of planets.
     */
    @GET("planets")
    suspend fun getPlanets(): PlanetResponse

    /**
     * Retrieves the next page of planet data.
     * @param nextPageUrl The URL of the next page.
     * @return The response containing the next page of planets.
     */
    @GET
    suspend fun getNextPage(@Url nextPageUrl: String): PlanetResponse
}