package com.jpmc.sriraksha.starplanet.data.repository

import com.google.gson.Gson
import com.jpmc.sriraksha.starplanet.data.local.PlanetLocalDataSource
import com.jpmc.sriraksha.starplanet.data.model.Planet
import com.jpmc.sriraksha.starplanet.data.model.PlanetDTO
import com.jpmc.sriraksha.starplanet.data.model.PlanetResponse
import com.jpmc.sriraksha.starplanet.data.remote.PlanetRemoteDataSource
import com.jpmc.sriraksha.starplanet.utils.NoNetworkException
import com.jpmc.sriraksha.starplanet.utils.RemoteDataSourceException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PlanetRepositoryImplTest {
    @Mock
    private lateinit var planetRemoteDataSource: PlanetRemoteDataSource

    @Mock
    private lateinit var planetLocalDataSource: PlanetLocalDataSource

    private lateinit var planetRepository: PlanetRepositoryImpl

    @Before
    fun setup() {
        planetRepository = PlanetRepositoryImpl(planetRemoteDataSource, planetLocalDataSource)
    }

    /**
     * Scenario: Get planets from the repository when the remote data source returns a valid response.
     * Covers the positive case of retrieving planets and verifying the correct PlanetResponse is returned.
     */
    @Test
    fun `get planets should return the correct PlanetResponse`() = runTest {
        // Arrange
        val planetResponse = createPlanetResponse()
        `when`(planetRemoteDataSource.getPlanets()).thenReturn(planetResponse)
//        `when`(planetLocalDataSource.savePlanets(any())).thenReturn(Unit)

        // Act
        val result = planetRepository.getPlanets()

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(planetResponse, result.getOrNull())
        verify(planetRemoteDataSource).getPlanets()
        // verify(planetLocalDataSource).savePlanets(any())
    }

    /**
     * Scenario: Get planets from the repository when the remote data source fails with NoNetworkException.
     * Covers the edge case of retrieving cached planets from the local data source when the remote fails.
     */
    @Test
    fun `get planets should return cached response when remote fails with NoNetworkException`() =
        runTest {
            // Arrange
            val cachedPlanets = listOf(createPlanet(), createPlanet())
            `when`(planetRemoteDataSource.getPlanets()).thenAnswer {
                throw NoNetworkException("someError")
            }

            `when`(planetLocalDataSource.getPlanets()).thenReturn(cachedPlanets)
            `when`(planetLocalDataSource.getPlanetsCount()).thenReturn(cachedPlanets.size.toLong())

            // Act
            val result = planetRepository.getPlanets()

            // Assert
            assertTrue(result.isSuccess)
            assertEquals(
                getExpectedResp(), result.getOrNull()
            )
            verify(planetRemoteDataSource).getPlanets()
            verify(planetLocalDataSource).getPlanets()
            verify(planetLocalDataSource).getPlanetsCount()
        }

    /**
     * Scenario: Get planets from the repository when both remote and local data sources fail.
     * Covers the negative case of handling failures from both remote and local data sources.
     */
    @Test
    fun `get planets should return failure when remote and local fail`() = runTest {
        // Arrange
        `when`(planetRemoteDataSource.getPlanets()).thenAnswer {
            throw RemoteDataSourceException("Unknown error")
        }

        // Act
        val result = planetRepository.getPlanets()

        // Assert
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is RemoteDataSourceException)
        verify(planetRemoteDataSource).getPlanets()
    }

    /**
     * Scenario: Get the next page of planets from the repository when the remote data source returns a valid response.
     * Covers the positive case of retrieving the next page of planets and verifying the correct PlanetResponse is returned.
     */
    @Test
    fun `get next page should return the correct PlanetResponse`() = runTest {
        // Arrange
        val nextPageUrl = "https://swapi.dev/api/planets/?page=2"
        val planetResponse = createPlanetResponse()
        `when`(planetRemoteDataSource.getNextPage(nextPageUrl)).thenReturn(planetResponse)


        // Act
        val result = planetRepository.getNextPage(nextPageUrl)

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(planetResponse, result.getOrNull())
        verify(planetRemoteDataSource).getNextPage(nextPageUrl)
    }

    /**
     * Scenario: Get the next page of planets from the repository when the remote data source fails with NoNetworkException.
     * Covers the negative case of handling NoNetworkException when retrieving the next page of planets.
     */
    @Test
    fun `get next page should return failure when remote fails with NoNetworkException`() =
        runTest {

            val nextPageUrl = "https://swapi.dev/api/planets/?page=2"
            `when`(planetRemoteDataSource.getNextPage(nextPageUrl)).thenAnswer {
                throw NoNetworkException("someError")
            }

            val result = planetRepository.getNextPage(nextPageUrl)

            // Assert
            assertTrue(result.isFailure)
            assertTrue(result.exceptionOrNull() is NoNetworkException)
            verify(planetRemoteDataSource).getNextPage(nextPageUrl)
        }

    /**
     * Scenario: Get the next page of planets from the repository when the remote data source fails with RemoteDataSourceException.
     * Covers the negative case of handling RemoteDataSourceException when retrieving the next page of planets.
     */
    @Test
    fun `get next page should return failure when remote fails with RemoteDataSourceException`() =
        runTest {
            // Arrange
            val nextPageUrl = "https://swapi.dev/api/planets/?page=2"

            `when`(planetRemoteDataSource.getNextPage(nextPageUrl)).thenAnswer {
                throw RemoteDataSourceException("someError")
            }

            // Act
            val result = planetRepository.getNextPage(nextPageUrl)

            // Assert
            assertTrue(result.isFailure)
            assertTrue(result.exceptionOrNull() is RemoteDataSourceException)
            verify(planetRemoteDataSource).getNextPage(nextPageUrl)
        }

    private fun getExpectedResp(): PlanetResponse {
        val expectedRespString =
            "{\"count\":2,\"results\":[{\"name\":\"Earth\",\"rotation_period\":\"23.44\",\"orbital_period\":\"365.25\",\"diameter\":\"12742\",\"climate\":\"temperate\",\"gravity\":\"1 standard\",\"terrain\":\"mostly water\",\"surface_water\":\"71\",\"population\":\"7.53 billion\",\"films\":[],\"created\":\"2014-12-10T11:35:48.479000Z\",\"edited\":\"2014-12-20T20:58:18.420000Z\",\"url\":\"\"},{\"name\":\"Earth\",\"rotation_period\":\"23.44\",\"orbital_period\":\"365.25\",\"diameter\":\"12742\",\"climate\":\"temperate\",\"gravity\":\"1 standard\",\"terrain\":\"mostly water\",\"surface_water\":\"71\",\"population\":\"7.53 billion\",\"films\":[],\"created\":\"2014-12-10T11:35:48.479000Z\",\"edited\":\"2014-12-20T20:58:18.420000Z\",\"url\":\"\"}]}"
        return Gson().fromJson(expectedRespString, PlanetResponse::class.java)
    }

    private fun createPlanetResponse(
        results: List<PlanetDTO> = listOf(
            PlanetDTO(
                name = "Earth",
                rotationPeriod = "23.44",
                orbitalPeriod = "365.25",
                diameter = "12742",
                climate = "temperate",
                gravity = "1 standard",
                terrain = "mostly water",
                surfaceWater = "71",
                population = "7.53 billion",
                residents = null,
                films = emptyList(),
                created = "2014-12-10T11:35:48.479000Z",
                edited = "2014-12-20T20:58:18.420000Z",
                url = "https://swapi.dev/api/planets/1/"
            ),
            PlanetDTO(
                name = "Mars",
                rotationPeriod = "24.62",
                orbitalPeriod = "687.0",
                diameter = "6779",
                climate = "cold",
                gravity = "0.38 standard",
                terrain = "polar ice caps, desert, mountains, volcanoes",
                surfaceWater = "0",
                population = "unknown",
                residents = null,
                films = emptyList(),
                created = "2014-12-10T11:35:48.479000Z",
                edited = "2014-12-20T20:58:18.420000Z",
                url = "https://swapi.dev/api/planets/2/"
            )
        )
    ): PlanetResponse {
        return PlanetResponse(
            count = results.size.toLong(),
            next = "https://swapi.dev/api/planets/?page=2",
            previous = null,
            results = results
        )
    }

    private fun createPlanet(): Planet {
        return Planet(
            name = "Earth",
            rotationPeriod = "23.44",
            orbitalPeriod = "365.25",
            diameter = "12742",
            climate = "temperate",
            gravity = "1 standard",
            terrain = "mostly water",
            surfaceWater = "71",
            population = "7.53 billion",
            created = "2014-12-10T11:35:48.479000Z",
            edited = "2014-12-20T20:58:18.420000Z"
        )
    }
}