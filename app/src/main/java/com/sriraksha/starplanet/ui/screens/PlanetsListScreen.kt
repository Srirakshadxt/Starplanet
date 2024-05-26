package com.sriraksha.starplanet.ui.screens

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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sriraksha.starplanet.data.model.Planet
import com.sriraksha.starplanet.ui.PlanetsUiState
import com.sriraksha.starplanet.ui.components.ErrorMessage
import com.sriraksha.starplanet.ui.components.LoadingIndicator
import com.sriraksha.starplanet.ui.loadmore.strategy.DefaultLoadMoreStrategy
import com.sriraksha.starplanet.ui.loadmore.strategy.LoadMoreStrategy
import com.sriraksha.starplanet.ui.viewmodel.PlanetsViewModel
import kotlinx.coroutines.flow.collectLatest

/**
 * Displays a list of Star Wars planets with a top app bar and a loading indicator, error message, or planet details screen when applicable.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanetsListScreen(
    viewModel: PlanetsViewModel = hiltViewModel(),
    loadMoreStrategy: LoadMoreStrategy = DefaultLoadMoreStrategy()
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
                    isLoadingMore = isLoadingMore,
                    loadMoreStrategy
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

/**
 * Displays a lazy column of planet items with loading more functionality.
 */
@Composable
fun PlanetsList(
    planets: List<Planet>,
    onPlanetSelected: (Planet) -> Unit,
    onLoadMore: () -> Unit,
    isLoadingMore: Boolean,
    loadMoreStrategy: LoadMoreStrategy = DefaultLoadMoreStrategy()
) {

    // Remembers the lazy list state to track the scroll position and load more items when needed
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

//    val shouldLoadMore by derivedStateOf {
//        loadMoreStrategy.shouldLoadMore(listState.layoutInfo)
//    }
//    LaunchedEffect(shouldLoadMore) {
//        if (shouldLoadMore) {
//            onLoadMore()
//        }
//    }
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo }
            .collectLatest { layoutInfo ->
                val shouldLoadMore = loadMoreStrategy.shouldLoadMore(layoutInfo)
                if (shouldLoadMore) {
                    onLoadMore()
                }
            }
    }
}

/**
 * Displays a clickable card with a planet's name
 */
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