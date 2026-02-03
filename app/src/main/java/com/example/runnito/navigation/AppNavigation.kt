package com.example.runnito.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.runnito.screens.events.EventsScreen
import com.example.runnito.screens.events.EventsViewModel
import com.example.runnito.screens.profile.ProfileScreen
import com.example.runnito.screens.registeredevents.RegisteredEventsScreen

@Composable
fun AppNavigation(navController: NavHostController, paddingValues: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = ScreenRoutes.Events.route,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable(ScreenRoutes.Events.route) {
            val eventsViewModel = hiltViewModel<EventsViewModel>()
            EventsScreen(eventsViewModel)
        }
        composable(ScreenRoutes.RegisteredEvents.route) { RegisteredEventsScreen() }
        composable(ScreenRoutes.Profile.route) { ProfileScreen() }
    }
}
