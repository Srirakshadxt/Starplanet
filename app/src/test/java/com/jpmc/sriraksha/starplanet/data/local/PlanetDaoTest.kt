package com.jpmc.sriraksha.starplanet.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jpmc.sriraksha.starplanet.data.model.Planet
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PlanetDaoTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var planetDao: PlanetDao


    @Test
    fun `insert planets should save to database`() = runBlocking {
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
        planetDao.insertPlanets(planets)
        `when`(planetDao.getAllPlanets()).thenReturn(planets)
        // Assert
        val allPlanets = planetDao.getAllPlanets()

        assertEquals(2, allPlanets.size)
        assertEquals("Earth", allPlanets[0].name)
        assertEquals("Mars", allPlanets[1].name)
    }


    @Test
    fun `get all planets should return an empty list when the database is empty`() =
        runBlocking {
            // Arrange
            `when`(planetDao.getAllPlanets()).thenReturn(emptyList())

            // Act
            val allPlanets = planetDao.getAllPlanets()

            // Assert
            assertTrue(allPlanets.isEmpty())
        }

    @Test
    fun `get planets count should return 0 when the database is empty`() = runBlocking {
        // Arrange
        `when`(planetDao.getPlanetsCount()).thenReturn(0L)

        // Act
        val planetsCount = planetDao.getPlanetsCount()

        // Assert
        assertEquals(0L, planetsCount)
    }
}
