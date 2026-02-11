package com.example.runnito

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import com.example.runnito.components.RunnitoAppBar
import com.example.runnito.navigation.AppNavigation
import com.example.runnito.navigation.ScreenRoutes
import com.example.runnito.screens.main.MainViewModel
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
        val currentScreen = screens.find { it.route == currentRoute } ?: ScreenRoutes.Events

        val drawerGestureEnabled = currentRoute != ScreenRoutes.EventDetails.route
        val destinationsWithDrawer = currentRoute in listOf(
            ScreenRoutes.Events.route,
            ScreenRoutes.RegisteredEvents.route,
            ScreenRoutes.Profile.route
        )
        val viewModel: MainViewModel = hiltViewModel()

        val showBackButton = !destinationsWithDrawer && currentRoute != null
        val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
        Log.i("TAG","USERPROFILE ${userProfile?.profilePicture}")
        if (currentRoute !== ScreenRoutes.CreateProfile.route) {
            RunnitoDrawer(
                scope = scope,
                navController = navController,
                currentRoute = currentRoute,
                drawerState = drawerState,
                userName = userProfile?.name ?: "",
                profilePictureUrl = userProfile?.profilePicture,
                drawerGestureEnabled = drawerGestureEnabled,

                ) {
                Scaffold(
                    topBar = {
                        RunnitoAppBar(
                            currentScreen.title,
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
        } else {
            Scaffold() { paddingValues ->
                AppNavigation(navController = navController, paddingValues = paddingValues)
            }

        }


    }
}


@Composable
fun DrawerHeader(userName: String, profilePictureUrl: String?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(vertical = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(
                model = profilePictureUrl,
                contentDescription = "User profile picture",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.not_available),
                error = painterResource(id = R.drawable.not_available)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = userName,
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleMedium
            )
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
    userName: String,
    profilePictureUrl: String?,
    content: @Composable () -> Unit
) {

    ModalNavigationDrawer(
        gesturesEnabled = drawerGestureEnabled,
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerHeader(userName = userName, profilePictureUrl = profilePictureUrl)
                Spacer(modifier = Modifier.height(16.dp))
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