package com.jpmc.sriraksha.starplanet.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jpmc.sriraksha.starplanet.data.model.Planet
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

    @Test
    fun `save planets should insert the planets into the local database`() = runBlocking {
        // Arrange
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

        // Act
        planetLocalDataSource.savePlanets(planets)

        // Assert
        verify(planetDao).insertPlanets(planets)
    }

    @Test
    fun `get planets should return an empty list when the local database is empty`() = runBlocking {
        // Arrange
        `when`(planetDao.getAllPlanets()).thenReturn(emptyList())

        // Act
        val allPlanets = planetLocalDataSource.getPlanets()

        // Assert
        assertTrue(allPlanets.isEmpty())
    }

    @Test
    fun `get planets count should return 0 when the local database is empty`() = runBlocking {
        // Arrange
        `when`(planetDao.getPlanetsCount()).thenReturn(0L)

        // Act
        val planetsCount = planetLocalDataSource.getPlanetsCount()

        // Assert
        assertEquals(0L, planetsCount)
    }
}