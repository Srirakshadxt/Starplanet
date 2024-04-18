package com.jpmc.sriraksha.starplanet.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jpmc.sriraksha.starplanet.data.model.Planet
import com.jpmc.sriraksha.starplanet.ui.PlanetsUiState
import com.jpmc.sriraksha.starplanet.ui.components.ErrorMessage
import com.jpmc.sriraksha.starplanet.ui.components.LoadingIndicator
import com.jpmc.sriraksha.starplanet.ui.viewmodel.PlanetsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanetsListScreen(
    viewModel: PlanetsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isLoadingMore by viewModel.isLoadingMore.collectAsState()
    val selectedPlanet by viewModel.selectedPlanet.collectAsState(initial = null)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Star Wars Planet Viewer") }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (uiState) {
                is PlanetsUiState.Loading -> LoadingIndicator()
                is PlanetsUiState.Success -> PlanetsList(
                    planets = (uiState as PlanetsUiState.Success).planets,
                    onPlanetSelected = viewModel::onPlanetSelected,
                    onLoadMore = viewModel::loadMorePlanets,
                    isLoadingMore = isLoadingMore
                )

                is PlanetsUiState.Error -> ErrorMessage(
                    message = (uiState as PlanetsUiState.Error).errorMessage,
                    onRetry = viewModel::fetchPlanets
                )
            }
            if (selectedPlanet != null) {
                PlanetDetailsScreen(selectedPlanet!!, viewModel::onPlanetDetailsDismissed)
            }
        }
    }
}

@Composable
fun PlanetsList(
    planets: List<Planet>,
    onPlanetSelected: (Planet) -> Unit,
    onLoadMore: () -> Unit,
    isLoadingMore: Boolean
) {
    val listState = rememberLazyListState()

    LazyColumn(state = listState) {
        items(planets) { planet ->
            PlanetItem(planet = planet, onPlanetSelected = onPlanetSelected)
        }

        if (isLoadingMore) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.Center)
                    )
                }
            }
        }
    }

    val shouldLoadMore = listState.shouldLoadMore()
    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            onLoadMore()
        }
    }
}

@Composable
fun PlanetItem(
    planet: Planet,
    onPlanetSelected: (Planet) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onPlanetSelected(planet) }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = planet.name,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Terrain: ${planet.terrain}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Population: ${planet.population}",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

fun LazyListState.shouldLoadMore(): Boolean {
    val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
    val totalItemCount = layoutInfo.totalItemsCount

    return lastVisibleItem != null && lastVisibleItem.index >= totalItemCount - 5
}