package com.example.runnito.navigation
sealed class ScreenRoutes(val route: String, val title: String) {
    object RegisteredEvents : ScreenRoutes("registered_events", "Registered Events")
    object Events : ScreenRoutes("events", "Events")
    object Profile : ScreenRoutes("profile", "Profile")
}
