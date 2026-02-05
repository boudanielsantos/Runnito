package com.example.runnito

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.runnito.components.RunnitoAppBar
import com.example.runnito.navigation.AppNavigation
import com.example.runnito.navigation.ScreenRoutes
import com.example.runnito.ui.theme.RunnitoTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


private val screens = listOf(
    ScreenRoutes.Events,
    ScreenRoutes.RegisteredEvents,
    ScreenRoutes.Profile
)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen()
        }
    }
}

@Composable
@Preview
@OptIn(ExperimentalMaterial3Api::class)
fun MainScreen() {
    RunnitoTheme {
        val navController = rememberNavController()
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val scope = rememberCoroutineScope()

        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val drawerGestureEnabled = currentRoute != ScreenRoutes.EventDetails.route
        val destinationsWithDrawer = currentRoute in listOf(
            ScreenRoutes.Events.route,
            ScreenRoutes.RegisteredEvents.route,
            ScreenRoutes.Profile.route
        )

        val showBackButton = !destinationsWithDrawer && currentRoute != null

        RunnitoDrawer(
            scope = scope,
            navController = navController,
            currentRoute = currentRoute,
            drawerState = drawerState,
            drawerGestureEnabled = drawerGestureEnabled
        ) {
            Scaffold(
                topBar = {
                    RunnitoAppBar(
                        "Runnito",
                        showBackButton = showBackButton,
                        onBackButtonClicked = { navController.popBackStack() },
                        scope = scope,
                        drawerState = drawerState
                    )
                }
            ) { paddingValues ->
                AppNavigation(navController = navController, paddingValues = paddingValues)
            }
        }


    }
}


@Composable
fun RunnitoDrawer(
    scope: CoroutineScope,
    navController: NavController,
    currentRoute: String?,
    drawerState: DrawerState,
    drawerGestureEnabled: Boolean,
    content: @Composable () -> Unit
) {

    ModalNavigationDrawer(
        gesturesEnabled = drawerGestureEnabled,
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Runnito", modifier = Modifier.padding(16.dp))
                screens.forEach { screen ->
                    NavigationDrawerItem(
                        label = { Text(text = screen.title) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            navController.navigate(screen.route)
                            scope.launch {
                                drawerState.close()
                            }
                        }
                    )
                }
            }
        }
    ) {
        content()
    }
}