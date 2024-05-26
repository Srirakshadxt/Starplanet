package com.sriraksha.starplanet.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sriraksha.starplanet.data.model.Planet
import com.sriraksha.starplanet.data.repository.PlanetRepository
import com.sriraksha.starplanet.data.repository.PlanetRepositoryImpl.Companion.toPlanet
import com.sriraksha.starplanet.ui.PlanetsUiState
import com.sriraksha.starplanet.utils.NoMorePagesException
import com.sriraksha.starplanet.utils.NoNetworkException
import com.sriraksha.starplanet.utils.RemoteDataSourceException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * View model for the Planets screen that manages the UI state, selected planet, and loading state.
 * It fetches planets from the repository, handles loading more planets, and updates the UI accordingly.
 */
@HiltViewModel
class PlanetsViewModel @Inject constructor(
    private val planetRepository: PlanetRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<PlanetsUiState>(PlanetsUiState.Loading)
    val uiState: StateFlow<PlanetsUiState> = _uiState.asStateFlow()

    private val _selectedPlanet = MutableStateFlow<Planet?>(null)
    val selectedPlanet: StateFlow<Planet?> = _selectedPlanet.asStateFlow()

    private val _isLoadingMore = MutableStateFlow(false)
    val isLoadingMore: StateFlow<Boolean> = _isLoadingMore.asStateFlow()

    var nextPageUrl: String? = null

    fun fetchPlanets() {
        viewModelScope.launch {
            _uiState.value = PlanetsUiState.Loading
            val result = planetRepository.getPlanets()
            result.onSuccess { response ->
                _uiState.value = PlanetsUiState.Success(response.results.map { it.toPlanet() })
                nextPageUrl = response.next
            }.onFailure { error ->
                handleError(error)
            }
        }
    }


    fun loadMorePlanets() {
        if (!_isLoadingMore.value && nextPageUrl != null) {
            _isLoadingMore.value = true
            viewModelScope.launch {
                val result = planetRepository.getNextPage(nextPageUrl!!)
                result.onSuccess { response ->
                    val planets = response.results.map { it.toPlanet() }
                    val currentPlanets =
                        (_uiState.value as? PlanetsUiState.Success)?.planets.orEmpty()
                    _uiState.value = PlanetsUiState.Success(currentPlanets + planets)
                    nextPageUrl = response.next
                }.onFailure { error ->
                    handleError(error)
                }
                _isLoadingMore.value = false
            }
        }
    }

    fun onPlanetSelected(planet: Planet) {
        _selectedPlanet.value = planet
    }

    fun onPlanetDetailsDismissed() {
        viewModelScope.launch {
            _selectedPlanet.emit(null)
        }
    }

    private fun handleError(error: Throwable) {
        _uiState.value = when (error) {
            is NoNetworkException -> PlanetsUiState.Error(
                error.message ?: "No network connection available"
            )

            is NoMorePagesException -> PlanetsUiState.Error("No more pages available")
            is RemoteDataSourceException -> PlanetsUiState.Error(error.message ?: "Unknown error")
            else -> PlanetsUiState.Error("Unknown error")
        }
    }
}