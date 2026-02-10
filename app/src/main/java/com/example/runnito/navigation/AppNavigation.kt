package com.example.runnito.navigation

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
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
import com.example.runnito.screens.main.MainViewModel
import com.example.runnito.screens.profile.CreateProfileScreen
import com.example.runnito.screens.profile.CreateProfileViewModel
import com.example.runnito.screens.profile.ProfileScreen
import com.example.runnito.screens.profile.ProfileViewModel
import com.example.runnito.screens.registeredevents.RegisteredEventsScreen
import com.example.runnito.viewmodel.RegisteredEventsViewModel

@Composable
fun AppNavigation(navController: NavHostController, paddingValues: PaddingValues) {
    val mainViewModel = hiltViewModel<MainViewModel>()
    val startDestination by mainViewModel.startDestination.collectAsState()
    if (startDestination == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Log.i("TAG","startDestination: $startDestination")
    NavHost(
        navController = navController,
        startDestination = startDestination!!,
        modifier = Modifier.padding(paddingValues)
    ) {
        composable(ScreenRoutes.CreateProfile.route) {
            val createProfileViewModel = hiltViewModel<CreateProfileViewModel>()
            CreateProfileScreen(createProfileViewModel)
        }
        composable(ScreenRoutes.Events.route) {
            val eventsViewModel = hiltViewModel<EventsViewModel>()

            EventsScreen(eventsViewModel, onNavigateToEventDetails = { eventId ->
                navController.navigate(ScreenRoutes.EventDetails.route + "/$eventId")
            })
        }
        composable(ScreenRoutes.RegisteredEvents.route) {
            val registeredEventsViewModel = hiltViewModel<RegisteredEventsViewModel>()

            RegisteredEventsScreen(registeredEventsViewModel) {
                navController.navigate(ScreenRoutes.EventDetails.route + "/$it")
            }
        }
        composable(ScreenRoutes.Profile.route) {
            val profileViewModel = hiltViewModel<ProfileViewModel>()
            ProfileScreen(profileViewModel)

        }

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
