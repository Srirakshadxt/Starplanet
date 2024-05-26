package com.sriraksha.starplanet.data.remote

import com.sriraksha.starplanet.data.model.PlanetResponse
import com.sriraksha.starplanet.utils.NoNetworkException
import com.sriraksha.starplanet.utils.RemoteDataSourceException
import java.io.IOException
import javax.inject.Inject

/**
 * Implements the PlanetRemoteDataSource interface to retrieve planet data from the PlanetApi.
 */
class PlanetRemoteDataSourceImpl @Inject constructor(
    private val planetApi: PlanetApi
) : PlanetRemoteDataSource {
    override suspend fun getPlanets(): PlanetResponse {
        try {
            return planetApi.getPlanets()
        } catch (e: Exception) {
            throw getRemoteException(e)
        }
    }

    override suspend fun getNextPage(nextPageUrl: String): PlanetResponse {
        try {
            return planetApi.getNextPage(nextPageUrl)
        } catch (e: Exception) {
            throw getRemoteException(e)
        }
    }

    private fun getRemoteException(e: Exception): Exception {
        return when (e) {
            is IOException -> NoNetworkException()
            else -> RemoteDataSourceException(e.message ?: "Unknown error")
        }
    }
}