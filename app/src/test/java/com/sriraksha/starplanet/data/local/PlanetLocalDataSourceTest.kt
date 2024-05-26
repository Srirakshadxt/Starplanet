package com.sriraksha.starplanet.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sriraksha.starplanet.data.model.Planet
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PlanetLocalDataSourceTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var planetDao: PlanetDao

    private lateinit var planetLocalDataSource: PlanetLocalDataSource

    @Before
    fun setup() {
        planetLocalDataSource = PlanetLocalDataSourceImpl(planetDao)
    }

    /**
     * Scenario: Save planets to the local data source and verify they are inserted into the local database.
     * Covers the positive case of saving planets to the local data source and verifying the insertion.
     */
    @Test
    fun `save planets should insert the planets into the local database`() = runBlocking {
        val planets = listOf(
            Planet(
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
            ),
            Planet(
                name = "Mars",
                rotationPeriod = "24.62",
                orbitalPeriod = "687.0",
                diameter = "6779",
                climate = "cold",
                gravity = "0.38 standard",
                terrain = "polar ice caps, desert, mountains, volcanoes",
                surfaceWater = "0",
                population = "unknown",
                created = "2014-12-10T11:35:48.479000Z",
                edited = "2014-12-20T20:58:18.420000Z"
            )
        )

        planetLocalDataSource.savePlanets(planets)

        verify(planetDao).insertPlanets(planets)
    }

    /**
     * Scenario: Get planets from the local data source when the local database is empty.
     * Covers the edge case of retrieving planets from an empty local database and verifying an empty list is returned.
     */
    @Test
    fun `get planets should return an empty list when the local database is empty`() = runBlocking {
        `when`(planetDao.getAllPlanets()).thenReturn(emptyList())

        val allPlanets = planetLocalDataSource.getPlanets()

        assertTrue(allPlanets.isEmpty())
    }

    /**
     * Scenario: Get the count of planets from the local data source when the local database is empty.
     * Covers the edge case of retrieving the count of planets from an empty local database and verifying the count is 0.
     */
    @Test
    fun `get planets count should return 0 when the local database is empty`() = runBlocking {
        `when`(planetDao.getPlanetsCount()).thenReturn(0L)

        val planetsCount = planetLocalDataSource.getPlanetsCount()

        assertEquals(0L, planetsCount)
    }
}
