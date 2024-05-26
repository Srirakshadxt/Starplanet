package com.sriraksha.starplanet.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class that represents a planet entity with various attributes such as name, rotation period, orbital period, diameter, climate, gravity, terrain, surface water, population, created, and edited.
 */
@Entity(tableName = "planets")
data class Planet(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val rotationPeriod: String,
    val orbitalPeriod: String,
    val diameter: String,
    val climate: String,
    val gravity: String,
    val terrain: String,
    val surfaceWater: String,
    val population: String,
    val created: String,
    val edited: String,
)