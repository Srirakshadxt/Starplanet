package com.sriraksha.starplanet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.sriraksha.starplanet.ui.screens.PlanetsListScreen
import com.sriraksha.starplanet.ui.theme.StarPlanetTheme
import com.sriraksha.starplanet.ui.viewmodel.PlanetsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val planetsViewModel: PlanetsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        planetsViewModel.fetchPlanets()
        setContent {
            StarPlanetTheme {
                PlanetsListScreen(planetsViewModel)
            }
        }
    }
}