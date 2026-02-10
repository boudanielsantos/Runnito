package com.example.runnito.navigation

sealed class ScreenRoutes(val route: String, val title: String) {
    object RegisteredEvents : ScreenRoutes("registered_events", "Registered Events")
    object Events : ScreenRoutes("events", "Events")

    object CreateProfile : ScreenRoutes("create_profile", "Create Profile")
    object Profile : ScreenRoutes("profile", "Profile")
    object EventDetails : ScreenRoutes("event_details", "Event Details")
}
