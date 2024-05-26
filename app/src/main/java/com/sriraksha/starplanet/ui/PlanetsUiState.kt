package com.sriraksha.starplanet.ui

import com.sriraksha.starplanet.data.model.Planet

/**
 * Represents the different UI states of the Planets screen.
 * It can be in a loading state, a success state with a list of planets, or an error state with an error message.
 */
sealed class PlanetsUiState {
    data object Loading : PlanetsUiState()
    data class Success(val planets: List<Planet>) : PlanetsUiState()
    data class Error(val errorMessage: String) : PlanetsUiState()
}