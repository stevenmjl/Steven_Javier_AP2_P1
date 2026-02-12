package edu.ucne.steven_javier_ap2_p1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import edu.ucne.steven_javier_ap2_p1.presentation.NavHost
import edu.ucne.steven_javier_ap2_p1.ui.theme.Steven_Javier_AP2_P1Theme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Steven_Javier_AP2_P1Theme {
                val navHostController = rememberNavController()
                NavHost(navHostController)
            }
        }
    }
}