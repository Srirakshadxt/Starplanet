package com.jpmc.sriraksha.starplanet.data.model

import com.google.gson.annotations.SerializedName

/**
 * Data class that represents the response from a planet API, containing the total count, next/previous page links, and a list of planet data transfer objects.
 */
data class PlanetResponse(
    val count: Long,
    val next: String?,
    val previous: Any?,
    val results: List<PlanetDTO>,
)

/**
 * Data class that represents a planet data transfer object, containing various attributes such as name, rotation period, orbital period, diameter, climate, gravity, terrain, surface water, population, residents, films, created, edited, and URL.
 * The PlanetDTO class is a data transfer object used to encapsulate and transfer planet data between the API response and the application's internal data model, providing a clear separation of concerns.
 */
data class PlanetDTO(
    val name: String,
    @SerializedName("rotation_period")
    val rotationPeriod: String,
    @SerializedName("orbital_period")
    val orbitalPeriod: String,
    val diameter: String,
    val climate: String,
    val gravity: String,
    val terrain: String,
    @SerializedName("surface_water")
    val surfaceWater: String,
    val population: String,
    val residents: List<String>?,
    val films: List<String>,
    val created: String,
    val edited: String,
    val url: String,
)


