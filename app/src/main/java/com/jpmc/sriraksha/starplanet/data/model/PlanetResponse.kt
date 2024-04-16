package com.jpmc.sriraksha.starplanet.data.model

import com.google.gson.annotations.SerializedName

data class PlanetResponse(
    val count: Long,
    val next: String?,
    val previous: Any?,
    val results: List<PlanetDTO>,
)

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


