package com.jpmc.sriraksha.starplanet.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jpmc.sriraksha.starplanet.data.model.Planet


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanetDetailsScreen(
    planet: Planet,
    onDismiss: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(planet.name) },
                navigationIcon = {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Rotation Period: ${planet.rotationPeriod}")
            Text("Orbital Period: ${planet.orbitalPeriod}")
            Text("Diameter: ${planet.diameter}")
            Text("Climate: ${planet.climate}")
            Text("Gravity: ${planet.gravity}")
            Text("Terrain: ${planet.terrain}")
            Text("Surface Water: ${planet.surfaceWater}")
            Text("Population: ${planet.population}")
        }
    }
}