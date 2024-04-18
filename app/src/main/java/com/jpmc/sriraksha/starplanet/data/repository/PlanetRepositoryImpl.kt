package com.jpmc.sriraksha.starplanet.data.repository

import com.jpmc.sriraksha.starplanet.data.local.PlanetLocalDataSource
import com.jpmc.sriraksha.starplanet.data.model.Planet
import com.jpmc.sriraksha.starplanet.data.model.PlanetDTO
import com.jpmc.sriraksha.starplanet.data.model.PlanetResponse
import com.jpmc.sriraksha.starplanet.data.remote.PlanetRemoteDataSource
import com.jpmc.sriraksha.starplanet.utils.NoNetworkException
import com.jpmc.sriraksha.starplanet.utils.RemoteDataSourceException
import javax.inject.Inject

/**
 * Implements the PlanetRepository interface to provide planet data from planet api and local data sources (DB).
 */
class PlanetRepositoryImpl @Inject constructor(
    private val remoteDataSource: PlanetRemoteDataSource,
    private val localDataSource: PlanetLocalDataSource
) : PlanetRepository {
    private var currentPageUrl: String? = null

    override suspend fun getPlanets(): Result<PlanetResponse> {
        return try {
            val response = remoteDataSource.getPlanets()
            currentPageUrl = response.next
            localDataSource.savePlanets(response.results.map { it.toPlanet() })
            Result.success(response)
        } catch (e: NoNetworkException) {
            val cachedPlanets = localDataSource.getPlanets().map { it.toPlanetDTO() }
            when {
                cachedPlanets.isEmpty() -> Result.failure(e)
                else -> {
                    val cachedResponse = PlanetResponse(
                        count = localDataSource.getPlanetsCount(),
                        next = currentPageUrl,
                        previous = null,
                        results = cachedPlanets
                    )
                    Result.success(cachedResponse)
                }
            }
        } catch (e: RemoteDataSourceException) {
            Result.failure(e)
        }
    }

    override suspend fun getNextPage(nextPageUrl: String): Result<PlanetResponse> {
        return try {
            val response = remoteDataSource.getNextPage(nextPageUrl)
            currentPageUrl = response.next
            localDataSource.savePlanets(response.results.map { it.toPlanet() })
            Result.success(response)

        } catch (e: NoNetworkException) {
            Result.failure(e)
        } catch (e: RemoteDataSourceException) {
            Result.failure(e)
        }
    }

    companion object {
        fun PlanetDTO.toPlanet(): Planet {
            return Planet(
                name = name,
                rotationPeriod = rotationPeriod,
                orbitalPeriod = orbitalPeriod,
                diameter = diameter,
                climate = climate,
                gravity = gravity,
                terrain = terrain,
                surfaceWater = surfaceWater,
                population = population,
                created = created,
                edited = edited,
            )
        }

        private fun Planet.toPlanetDTO(): PlanetDTO {
            return PlanetDTO(
                name = name,
                rotationPeriod = rotationPeriod,
                orbitalPeriod = orbitalPeriod,
                diameter = diameter,
                climate = climate,
                gravity = gravity,
                terrain = terrain,
                surfaceWater = surfaceWater,
                population = population,
                residents = null,
                films = emptyList(),
                created = created,
                edited = edited,
                url = ""
            )
        }

    }

}