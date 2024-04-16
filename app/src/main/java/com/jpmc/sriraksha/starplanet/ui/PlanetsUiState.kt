package com.jpmc.sriraksha.starplanet.ui

import com.jpmc.sriraksha.starplanet.data.model.Planet

sealed class PlanetsUiState {
    data object Loading : PlanetsUiState()
    data class Success(val planets: List<Planet>) : PlanetsUiState()
    data class Error(val errorMessage: String) : PlanetsUiState()
}