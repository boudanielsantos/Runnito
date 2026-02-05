package com.example.runnito.navigation

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.runnito.screens.eventdetails.EventDetailsScreen
import com.example.runnito.screens.eventdetails.EventDetailsViewModel
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

            EventsScreen(eventsViewModel, onNavigateToEventDetails = { eventId ->
                navController.navigate(ScreenRoutes.EventDetails.route + "/$eventId")
            })
        }
        composable(ScreenRoutes.RegisteredEvents.route) { RegisteredEventsScreen() }
        composable(ScreenRoutes.Profile.route) { ProfileScreen() }

        composable(
            route = ScreenRoutes.EventDetails.route + "/{eventId}",
            arguments = listOf(navArgument("eventId") { type = NavType.StringType })
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getString("eventId")
            val eventsDetailsViewModel = hiltViewModel<EventDetailsViewModel>()

            EventDetailsScreen(
                eventId = eventId,
                eventDetailsViewModel = eventsDetailsViewModel,
                paddingValues = paddingValues
            )
        }
    }
}
