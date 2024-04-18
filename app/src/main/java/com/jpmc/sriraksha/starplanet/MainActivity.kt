package com.jpmc.sriraksha.starplanet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.jpmc.sriraksha.starplanet.ui.screens.PlanetsListScreen
import com.jpmc.sriraksha.starplanet.ui.theme.StarPlanetTheme
import com.jpmc.sriraksha.starplanet.ui.viewmodel.PlanetsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val planetsViewModel: PlanetsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StarPlanetTheme {
                PlanetsListScreen(planetsViewModel)
            }
        }
    }
}