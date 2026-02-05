package edu.ucne.steven_javier_ap2_p1.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edu.ucne.steven_javier_ap2_p1.presentation.entidad.edit.BorrameEditScreen
import edu.ucne.steven_javier_ap2_p1.presentation.entidad.list.BorrameListScreen

sealed class Pantalla(val ruta: String) {
    data object Lista : Pantalla("lista_borrame")
    data object Editar : Pantalla("editar_borrame")
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Pantalla.Lista.ruta
    ) {
        composable(Pantalla.Lista.ruta) {
            BorrameListScreen(
                onIrAEditar = { navController.navigate(Pantalla.Editar.ruta) }
            )
        }

        composable(Pantalla.Editar.ruta) {
            BorrameEditScreen(
                onVolver = { navController.popBackStack() }
            )
        }
    }
}