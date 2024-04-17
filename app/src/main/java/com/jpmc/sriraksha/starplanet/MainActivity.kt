package com.jpmc.sriraksha.starplanet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.jpmc.sriraksha.starplanet.ui.screens.PlanetsListScreen
import com.jpmc.sriraksha.starplanet.ui.theme.StarPlanetTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StarPlanetTheme {
                PlanetsListScreen()
            }
        }
    }
}