package com.jpmc.sriraksha.starplanet.ui.viewmodel

import com.jpmc.sriraksha.starplanet.data.model.Planet
import com.jpmc.sriraksha.starplanet.data.model.PlanetDTO
import com.jpmc.sriraksha.starplanet.data.model.PlanetResponse
import com.jpmc.sriraksha.starplanet.data.repository.PlanetRepository
import com.jpmc.sriraksha.starplanet.ui.PlanetsUiState
import com.jpmc.sriraksha.starplanet.utils.NoNetworkException
import com.jpmc.sriraksha.starplanet.utils.RemoteDataSourceException
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.times

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class PlanetsViewModelTest {

    @Mock
    private lateinit var planetRepository: PlanetRepository

    private lateinit var planetsViewModel: PlanetsViewModel

    private val testDispatcher = TestCoroutineDispatcher()
    private val testCoroutineScope = TestCoroutineScope(testDispatcher)

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        planetsViewModel = PlanetsViewModel(planetRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * Scenario: Fetch planets successfully from the repository.
     * Covers the positive case of loading planets and verifying the UI state is updated with the fetched planets.
     */
    @Test
    fun `fetchPlanets should load planets successfully`() = testCoroutineScope.runBlockingTest {
        val planetResponse = createPlanetResponse(
            listOf(
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
                    residents = emptyList(),
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
                    residents = emptyList(),
                    films = emptyList(),
                    created = "2014-12-10T11:35:48.479000Z",
                    edited = "2014-12-20T20:58:18.420000Z",
                    url = "https://swapi.dev/api/planets/2/"
                )
            )
        )
        `when`(planetRepository.getPlanets()).thenReturn(Result.success(planetResponse))
        assertEquals(PlanetsUiState.Loading, planetsViewModel.uiState.value)

        planetsViewModel.fetchPlanets()

        assertTrue(planetsViewModel.uiState.value is PlanetsUiState.Success)
        assertEquals(2, (planetsViewModel.uiState.value as PlanetsUiState.Success).planets.size)
        assertEquals(
            "Earth",
            (planetsViewModel.uiState.value as PlanetsUiState.Success).planets[0].name
        )
        assertEquals(
            "Mars",
            (planetsViewModel.uiState.value as PlanetsUiState.Success).planets[1].name
        )
    }

    /**
     * Scenario: Handle NoNetworkException when fetching planets from the repository.
     * Covers the negative case of handling network failure and verifying the UI state is updated with the error message.
     */
    @Test
    fun `fetchPlanets should handle NoNetworkException`() = testCoroutineScope.runBlockingTest {
        val exception = NoNetworkException("No network connection available")
        `when`(planetRepository.getPlanets()).thenReturn(Result.failure(exception))

        assertEquals(PlanetsUiState.Loading, planetsViewModel.uiState.value)

        planetsViewModel.fetchPlanets()

        assertTrue(planetsViewModel.uiState.value is PlanetsUiState.Error)
        assertEquals(
            "No network connection available",
            (planetsViewModel.uiState.value as PlanetsUiState.Error).errorMessage
        )
        verify(planetRepository, times(2)).getPlanets()
    }

    /**
     * Scenario: Handle RemoteDataSourceException when fetching planets from the repository.
     * Covers the negative case of handling remote data source failure and verifying the UI state is updated with the error message.
     */
    @Test
    fun `fetchPlanets should handle RemoteDataSourceException`() =
        testCoroutineScope.runBlockingTest {
            val exception = RemoteDataSourceException("Unknown error")
            `when`(planetRepository.getPlanets()).thenReturn(Result.failure(exception))

            assertEquals(PlanetsUiState.Loading, planetsViewModel.uiState.value)
            planetsViewModel.fetchPlanets()

            assertTrue(planetsViewModel.uiState.value is PlanetsUiState.Error)
            assertEquals(
                "Unknown error",
                (planetsViewModel.uiState.value as PlanetsUiState.Error).errorMessage
            )
        }

    /**
     * Scenario: Load more planets successfully from the repository.
     * Covers the positive case of loading additional planets and verifying the UI state is updated with the combined list of planets.
     */
    @Test
    fun `loadMorePlanets should load more planets successfully`() =
        testCoroutineScope.runBlockingTest {
            val initialPlanetResponse = createPlanetResponse(
                listOf(
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
                        residents = emptyList(),
                        films = emptyList(),
                        created = "2014-12-10T11:35:48.479000Z",
                        edited = "2014-12-20T20:58:18.420000Z",
                        url = "https://swapi.dev/api/planets/1/"
                    )
                )
            )
            val morePlanetResponse = createPlanetResponse(
                listOf(
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
                        residents = emptyList(),
                        films = emptyList(),
                        created = "2014-12-10T11:35:48.479000Z",
                        edited = "2014-12-20T20:58:18.420000Z",
                        url = "https://swapi.dev/api/planets/2/"
                    )
                )
            )
            `when`(planetRepository.getPlanets()).thenReturn(Result.success(initialPlanetResponse))
            `when`(planetRepository.getNextPage("https://swapi.dev/api/planets/?page=2")).thenReturn(
                Result.success(
                    morePlanetResponse
                )
            )

            assertEquals(PlanetsUiState.Loading, planetsViewModel.uiState.value)

            planetsViewModel.fetchPlanets()
            planetsViewModel.loadMorePlanets()

            assertTrue(planetsViewModel.uiState.value is PlanetsUiState.Success)
            assertEquals(2, (planetsViewModel.uiState.value as PlanetsUiState.Success).planets.size)
            assertEquals(
                "Earth",
                (planetsViewModel.uiState.value as PlanetsUiState.Success).planets[0].name
            )
            assertEquals(
                "Mars",
                (planetsViewModel.uiState.value as PlanetsUiState.Success).planets[1].name
            )
            assertFalse(planetsViewModel.isLoadingMore.value)
        }

    /**
     * Scenario: Handle NoNetworkException when loading more planets from the repository.
     * Covers the negative case of handling network failure during pagination and verifying the UI state is updated with the error message.
     */
    @Test
    fun `loadMorePlanets should handle NoNetworkException`() = testCoroutineScope.runBlockingTest {
        val exception = NoNetworkException("No network connection available")

        `when`(planetRepository.getNextPage(anyString())).thenReturn(Result.failure(exception))

        planetsViewModel.nextPageUrl = "https://swapi.dev/api/planets/?page=2"
        planetsViewModel.loadMorePlanets()

        assertFalse(planetsViewModel.isLoadingMore.value)
        assertTrue(planetsViewModel.uiState.value is PlanetsUiState.Error)
        assertEquals(
            "No network connection available",
            (planetsViewModel.uiState.value as PlanetsUiState.Error).errorMessage
        )
    }

    /**
     * Scenario: Handle RemoteDataSourceException when loading more planets from the repository.
     * Covers the negative case of handling remote data source failure during pagination and verifying the UI state is updated with the error message.
     */
    @Test
    fun `loadMorePlanets should handle RemoteDataSourceException`() =
        testCoroutineScope.runBlockingTest {
            val exception = RemoteDataSourceException("Unknown error")

            `when`(planetRepository.getNextPage(anyString())).thenReturn(Result.failure(exception))

            planetsViewModel.nextPageUrl = "https://swapi.dev/api/planets/?page=2"

            planetsViewModel.loadMorePlanets()

            assertFalse(planetsViewModel.isLoadingMore.value)
            assertTrue(planetsViewModel.uiState.value is PlanetsUiState.Error)
            assertEquals(
                "Unknown error",
                (planetsViewModel.uiState.value as PlanetsUiState.Error).errorMessage
            )
        }

    /**
     * Scenario: Update the selected planet when a planet is selected.
     * Covers the positive case of selecting a planet and verifying the selectedPlanet property is updated.
     */
    @Test
    fun `onPlanetSelected should update the selectedPlanet`() = testCoroutineScope.runBlockingTest {
        val planet = Planet(
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

        planetsViewModel.onPlanetSelected(planet)

        assertEquals(planet, planetsViewModel.selectedPlanet.value)
    }

    /**
     * Scenario: Reset the selected planet to null when the planet details are dismissed.
     * Covers the positive case of dismissing the planet details and verifying the selectedPlanet property is set to null.
     */
    @Test
    fun `onPlanetDetailsDismissed should set selectedPlanet to null`() =
        testCoroutineScope.runBlockingTest {
            // Arrange
            val planet = Planet(
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
            planetsViewModel.onPlanetSelected(planet)

            planetsViewModel.onPlanetDetailsDismissed()

            assertNull(planetsViewModel.selectedPlanet.value)
        }

    private fun createPlanetResponse(
        results: List<PlanetDTO>
    ): PlanetResponse {
        return PlanetResponse(
            count = results.size.toLong(),
            next = "https://swapi.dev/api/planets/?page=2",
            previous = null,
            results = results
        )
    }
}