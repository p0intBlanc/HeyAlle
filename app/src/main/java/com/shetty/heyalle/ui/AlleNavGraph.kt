package com.shetty.heyalle.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.shetty.heyalle.ui.gallery.GalleryScreen
import com.shetty.heyalle.ui.gallery.InfoScreen
import com.shetty.heyalle.ui.viewmodel.ImagesViewModel

@Composable
fun AlleNavGraph(
    navController: NavHostController,
    startDestination: String = AppDestinations.HOME_ROUTE,
    viewModel: ImagesViewModel
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(route = AppDestinations.HOME_ROUTE) {
            GalleryScreen(viewModel)
        }
        composable(route = AppDestinations.INFO_ROUTE) {
            InfoScreen(viewModel)
        }
    }
}


object AppDestinations {
    const val HOME_ROUTE = "home"
    const val INFO_ROUTE = "info"
}

sealed class Screen(val route: String) {
    object Home : Screen(AppDestinations.HOME_ROUTE)
    object Search : Screen(AppDestinations.INFO_ROUTE)


}
