package edu.ucne.steven_javier_ap2_p1.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import edu.ucne.steven_javier_ap2_p1.presentation.cerveza.list.CervezaListScreen
import edu.ucne.steven_javier_ap2_p1.presentation.cerveza.edit.CervezaEditScreen
import edu.ucne.steven_javier_ap2_p1.presentation.Screen

@Composable
fun NavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.CervezaList
    ) {
        composable<Screen.CervezaList> {
            CervezaListScreen(
                onNavigateToCreate = {
                    navController.navigate(Screen.CervezaEdit(0))
                },
                onNavigateToEdit = { id ->
                    navController.navigate(Screen.CervezaEdit(id))
                }
            )
        }

        composable<Screen.CervezaEdit> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.CervezaEdit>()
            CervezaEditScreen(
                cervezaId = args.id,
                onNavigateBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}