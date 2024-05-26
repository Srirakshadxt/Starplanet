package com.sriraksha.starplanet.data.remote

import com.sriraksha.starplanet.data.model.PlanetDTO
import com.sriraksha.starplanet.data.model.PlanetResponse
import com.sriraksha.starplanet.utils.NoNetworkException
import com.sriraksha.starplanet.utils.RemoteDataSourceException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.io.IOException

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PlanetRemoteDataSourceTest {
    @Mock
    private lateinit var planetApi: PlanetApi

    private lateinit var planetRemoteDataSource: PlanetRemoteDataSourceImpl

    @Before
    fun setup() {
        planetRemoteDataSource = PlanetRemoteDataSourceImpl(planetApi)
    }

    /**
     * Scenario: Get planets from the remote data source when the API returns a valid response.
     * Covers the positive case of retrieving planets and verifying the correct PlanetResponse is returned.
     */
    @Test
    fun `get planets should return the correct PlanetResponse`() = runBlocking {
        // Arrange
        val planetResponse = PlanetResponse(
            count = 2,
            next = null,
            previous = null,
            results = listOf(
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
        )

        `when`(planetApi.getPlanets()).thenReturn(planetResponse)

        // Act
        val result = planetRemoteDataSource.getPlanets()

        // Assert
        assertEquals(planetResponse, result)
    }

    /**
     * Scenario: Get planets from the remote data source when the API returns an empty list.
     * Covers the edge case of retrieving planets when no data is available and verifying an empty PlanetResponse is returned.
     */
    @Test
    fun `get planets should return an empty PlanetResponse when the API returns an empty list`() =
        runBlocking {
            // Arrange
            val emptyPlanetResponse = PlanetResponse(
                count = 0,
                next = null,
                previous = null,
                results = emptyList()
            )

            `when`(planetApi.getPlanets()).thenReturn(emptyPlanetResponse)

            // Act
            val result = planetRemoteDataSource.getPlanets()

            // Assert
            assertEquals(emptyPlanetResponse, result)
        }

    /**
     * Scenario: Get the next page of planets from the remote data source when the API returns a valid response.
     * Covers the positive case of retrieving the next page of planets and verifying the correct PlanetResponse is returned.
     */
    @Test
    fun `get next page should return the correct PlanetResponse`() = runBlocking {
        // Arrange
        val nextPageUrl = "https://swapi.dev/api/planets/?page=2"
        val planetResponse = PlanetResponse(
            count = 2,
            next = null,
            previous = null,
            results = listOf(
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
        )

        `when`(planetApi.getNextPage(nextPageUrl)).thenReturn(planetResponse)

        // Act
        val result = planetRemoteDataSource.getNextPage(nextPageUrl)

        // Assert
        assertEquals(planetResponse, result)
    }

    /**
     * Scenario: Get the next page of planets from the remote data source when the API returns an empty list.
     * Covers the edge case of retrieving the next page of planets when no data is available and verifying an empty PlanetResponse is returned.
     */
    @Test
    fun `get next page should return an empty PlanetResponse when the API returns an empty list`() =
        runBlocking {
            // Arrange
            val nextPageUrl = "https://swapi.dev/api/planets/?page=2"
            val emptyPlanetResponse = PlanetResponse(
                count = 0,
                next = null,
                previous = null,
                results = emptyList()
            )

            `when`(planetApi.getNextPage(nextPageUrl)).thenReturn(emptyPlanetResponse)

            // Act
            val result = planetRemoteDataSource.getNextPage(nextPageUrl)

            // Assert
            assertEquals(emptyPlanetResponse, result)
        }

    /**
     * Scenario: Get the next page of planets from the remote data source when an IOException occurs.
     * Covers the negative case of handling an IOException and verifying that a NoNetworkException is thrown with the correct message.
     */
    @Test
    fun `get next page should throw NoNetworkException on IOException`() = runBlocking {
        // Arrange
        val nextPageUrl = "https://swapi.dev/api/planets/?page=2"
        `when`(planetApi.getNextPage(nextPageUrl)).thenAnswer {
            throw IOException()
        }

        // Act
        val exception = assertThrows(NoNetworkException::class.java) {
            runBlocking { planetRemoteDataSource.getNextPage(nextPageUrl) }
        }

        assertTrue(exception is NoNetworkException)
        assertEquals("No network connection available", exception.message)
    }

    /**
     * Scenario: Get planets from the remote data source when an unknown exception occurs.
     * Covers the negative case of handling an unknown exception and verifying that a RemoteDataSourceException is thrown with the correct message.
     */
    @Test
    fun `get planets should throw RemoteDataSourceException on other exceptions`() = runBlocking {
        // Arrange
        `when`(planetApi.getPlanets()).thenThrow(RuntimeException("Unknown error"))

        val exception = assertThrows(RemoteDataSourceException::class.java) {
            runBlocking { planetRemoteDataSource.getPlanets() }
        }

        assertTrue(exception is RemoteDataSourceException)
        assertEquals("Unknown error", exception.message)

    }

    /**
     * Scenario: Get the next page of planets from the remote data source when an unknown exception occurs.
     * Covers the negative case of handling an unknown exception and verifying that a RemoteDataSourceException is thrown with the correct message.
     */
    @Test
    fun `get next page should throw RemoteDataSourceException on other exceptions`() = runBlocking {
        // Arrange
        val nextPageUrl = "https://swapi.dev/api/planets/?page=2"
        `when`(planetApi.getNextPage(nextPageUrl)).thenThrow(RuntimeException("Unknown error"))

        val exception = assertThrows(RemoteDataSourceException::class.java) {
            runBlocking { planetRemoteDataSource.getNextPage(nextPageUrl) }
        }
        // Act
        assertTrue(exception is RemoteDataSourceException)
        assertEquals("Unknown error", exception.message)
    }
}