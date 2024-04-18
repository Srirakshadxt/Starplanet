package com.jpmc.sriraksha.starplanet.ui.screens

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jpmc.sriraksha.starplanet.MainActivity
import com.jpmc.sriraksha.starplanet.data.model.Planet
import com.jpmc.sriraksha.starplanet.ui.PlanetsUiState
import com.jpmc.sriraksha.starplanet.ui.viewmodel.PlanetsViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented tests for the PlanetsListScreen and PlanetDetailsScreen composable.
 * Verifies the behavior of the screen under different scenarios, including data loading, error handling, and user interactions.
 */
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class PlanetsListScreenTest {

    @get:Rule(order = 1)
    var hiltTestRule = HiltAndroidRule(this)

    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltTestRule.inject()
    }

    /**
     * Scenario: Display a single planet in the list when data is available.
     * Verifies that the PlanetsListScreen correctly displays the planet name, terrain, and population.
     */
    @Test
    fun displayPlanetsListWhenDataIsAvailable() {
        val planets = listOf(
            Planet(
                name = "Tatooine",
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
        )
        val viewModel: PlanetsViewModel = mockk()
        val uiStateFlow = MutableStateFlow<PlanetsUiState>(PlanetsUiState.Success(planets))
        val isLoadingMoreFlow = MutableStateFlow(false)
        val selectedPlanetFlow = MutableStateFlow<Planet?>(null)

        every { viewModel.uiState } returns uiStateFlow
        every { viewModel.isLoadingMore } returns isLoadingMoreFlow
        every { viewModel.selectedPlanet } returns selectedPlanetFlow
        every { viewModel.loadMorePlanets() }

        composeTestRule.activity.setContent {
            PlanetsListScreen(viewModel)
        }

        composeTestRule.waitUntil(10_000L) {
            (composeTestRule.activity.planetsViewModel.uiState.value as? PlanetsUiState.Success)?.planets?.isNotEmpty() == true
        }

        composeTestRule.onNodeWithText("Star Wars Planet Viewer").assertIsDisplayed()
        composeTestRule.onNodeWithText("Tatooine").assertIsDisplayed()
        composeTestRule.onNodeWithText("Terrain: mostly water").assertIsDisplayed()
        composeTestRule.onNodeWithText("Population: 7.53 billion").assertIsDisplayed()
    }

    /**
     * Scenario: Display multiple planets in the list when data is available.
     * Verifies that the PlanetsListScreen correctly displays the planet names, terrains, and populations.
     */
    @Test
    fun displayMultiplePlanetsListWhenDataIsAvailable() {
        val planets = listOf(
            Planet(
                name = "Tatooine",
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
                name = "Alderaan",
                rotationPeriod = "24.0",
                orbitalPeriod = "364.0",
                diameter = "12500",
                climate = "temperate",
                gravity = "1 standard",
                terrain = "grasslands, mountains",
                surfaceWater = "40",
                population = "2 billion",
                created = "2014-12-10T11:35:48.479000Z",
                edited = "2014-12-20T20:58:18.420000Z"
            )
        )

        val viewModel: PlanetsViewModel = mockk()
        val uiStateFlow = MutableStateFlow<PlanetsUiState>(PlanetsUiState.Success(planets))
        val isLoadingMoreFlow = MutableStateFlow(false)
        val selectedPlanetFlow = MutableStateFlow<Planet?>(null)

        every { viewModel.uiState } returns uiStateFlow
        every { viewModel.isLoadingMore } returns isLoadingMoreFlow
        every { viewModel.selectedPlanet } returns selectedPlanetFlow
        every { viewModel.loadMorePlanets() }

        composeTestRule.activity.setContent {
            PlanetsListScreen(viewModel)
        }

        composeTestRule.waitUntil(10_000L) {
            (composeTestRule.activity.planetsViewModel.uiState.value as? PlanetsUiState.Success)?.planets?.isNotEmpty() == true
        }

        composeTestRule.onNodeWithText("Star Wars Planet Viewer").assertIsDisplayed()

        composeTestRule.onNodeWithText("Tatooine").assertIsDisplayed()
        composeTestRule.onNodeWithText("Terrain: mostly water").assertIsDisplayed()
        composeTestRule.onNodeWithText("Population: 7.53 billion").assertIsDisplayed()

        composeTestRule.onNodeWithText("Alderaan").assertIsDisplayed()
        composeTestRule.onNodeWithText("Terrain: grasslands, mountains").assertIsDisplayed()
        composeTestRule.onNodeWithText("Population: 2 billion").assertIsDisplayed()
    }

    /**
     * Scenario: Navigate to the planet details screen when a planet is clicked.
     * Verifies that clicking on a planet item triggers the navigation and displays the correct planet details.
     */
    @Test
    fun navigateToPlanetDetailsScreenWhenPlanetClicked() {
        val planets = listOf(
            Planet(
                name = "Tatooine",
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
                name = "Alderaan",
                rotationPeriod = "24.0",
                orbitalPeriod = "364.0",
                diameter = "12500",
                climate = "temperate",
                gravity = "1 standard",
                terrain = "grasslands, mountains",
                surfaceWater = "40",
                population = "2 billion",
                created = "2014-12-10T11:35:48.479000Z",
                edited = "2014-12-20T20:58:18.420000Z"
            )
        )
        val viewModel: PlanetsViewModel = mockk()
        val uiStateFlow = MutableStateFlow<PlanetsUiState>(PlanetsUiState.Success(planets))
        val isLoadingMoreFlow = MutableStateFlow(false)
        val selectedPlanetFlow = MutableStateFlow<Planet?>(null)

        every { viewModel.uiState } returns uiStateFlow
        every { viewModel.isLoadingMore } returns isLoadingMoreFlow
        every { viewModel.selectedPlanet } returns selectedPlanetFlow
        every { viewModel.onPlanetSelected(any()) } answers {
            selectedPlanetFlow.value = arg(0) as Planet
        }

        composeTestRule.activity.setContent {
            PlanetsListScreen(viewModel)
        }

        composeTestRule.waitUntil(5_000L) {
            (composeTestRule.activity.planetsViewModel.uiState.value as? PlanetsUiState.Success)?.planets?.isNotEmpty() == true
        }

        composeTestRule.onNodeWithText("Tatooine").performClick()

        composeTestRule.onNodeWithText("Rotation Period: 23.44").assertIsDisplayed()
        composeTestRule.onNodeWithText("Orbital Period: 365.25").assertIsDisplayed()
        composeTestRule.onNodeWithText("Diameter: 12742").assertIsDisplayed()
        composeTestRule.onNodeWithText("Climate: temperate").assertIsDisplayed()
        composeTestRule.onNodeWithText("Gravity: 1 standard").assertIsDisplayed()
        composeTestRule.onNodeWithText("Surface Water: 71").assertIsDisplayed()
    }

    /**
     * Scenario: Display an error message when fetching planets fails.
     * Verifies that the PlanetsListScreen displays the error message and a "Retry" button, while hiding the loading indicator.
     */
    @Test
    fun displayErrorMessageWhenFetchingPlanetsFails() {
        val errorMessage = "Failed to fetch planets"
        val viewModel: PlanetsViewModel = mockk()
        val uiStateFlow = MutableStateFlow<PlanetsUiState>(PlanetsUiState.Loading)
        val errorUiState = PlanetsUiState.Error(errorMessage)

        coEvery { viewModel.uiState } coAnswers {
            uiStateFlow.value = PlanetsUiState.Loading
            delay(100)
            uiStateFlow.value = errorUiState
            uiStateFlow
        }
        every { viewModel.isLoadingMore } returns MutableStateFlow(false)
        every { viewModel.selectedPlanet } returns MutableStateFlow(null)
        every { viewModel.fetchPlanets() }

        composeTestRule.activity.setContent {
            PlanetsListScreen(viewModel)
        }

        composeTestRule.waitUntil(5_000L) {
            composeTestRule.onNodeWithText(errorMessage).isDisplayed()
        }

        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
        composeTestRule.onNodeWithText("Retry").assertIsDisplayed()
        composeTestRule.onNode(hasTestTag("loading-indicator")).assertDoesNotExist()

    }

    /**
     * Scenario: Retry fetching planets when the "Retry" button is clicked after an error.
     * Verifies that clicking the "Retry" button invokes the fetchPlanets() method of the view model.
     */
    @Test
    fun retryFetchingPlanetsWhenErrorMessageClicked() {
        val errorMessage = "Failed to fetch planets"
        val viewModel: PlanetsViewModel = mockk()
        val uiStateFlow = MutableStateFlow<PlanetsUiState>(PlanetsUiState.Loading)
        val errorUiState = PlanetsUiState.Error(errorMessage)

        coEvery { viewModel.uiState } coAnswers {
            uiStateFlow.value = PlanetsUiState.Loading
            delay(100)
            uiStateFlow.value = errorUiState
            uiStateFlow
        }
        every { viewModel.isLoadingMore } returns MutableStateFlow(false)
        every { viewModel.selectedPlanet } returns MutableStateFlow(null)

        coEvery { viewModel.fetchPlanets() } answers {
            uiStateFlow.value = PlanetsUiState.Error(errorMessage)
        }

        composeTestRule.activity.setContent {
            PlanetsListScreen(viewModel)
        }

        composeTestRule.waitUntil(5_000L) {
            composeTestRule.onNodeWithText(errorMessage).isDisplayed()
        }

        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
        composeTestRule.onNodeWithText("Retry").assertIsDisplayed()
        composeTestRule.onNode(hasTestTag("loading-indicator")).assertDoesNotExist()

        composeTestRule.onNodeWithText("Retry").performClick()
        coVerify { viewModel.fetchPlanets() }
    }
}


