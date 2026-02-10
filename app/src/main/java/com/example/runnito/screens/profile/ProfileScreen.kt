package com.example.runnito.screens.profile

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.runnito.components.ProfileImage
import com.example.runnito.components.RunningManLoader


@Composable
fun ProfileScreen(profileViewModel: ProfileViewModel) {
    val totalDistance by profileViewModel.totalDistanceRan.collectAsStateWithLifecycle()
    val totalEventsThisYear by profileViewModel.totalEventsThisYear.collectAsStateWithLifecycle()
    val totalEventsOverall by profileViewModel.totalEventsOverall.collectAsStateWithLifecycle()
    val currentUserState by profileViewModel.currentUser.collectAsStateWithLifecycle()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (currentUserState.loading || currentUserState.data == null) {
            RunningManLoader()
        } else {
            ProfileImage(imageUri = currentUserState.data!!.profilePicture)
            Log.i(
                "TAG", "PROFILE PIC = ${currentUserState.data!!.profilePicture}"
            )
            Spacer(modifier = Modifier.height(16.dp))


            Text(
                text = currentUserState.data!!.name,
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Displaying the statistics
            StatisticRow(
                label = "Total Distance Ran",
                value = String.format("%.1f km", totalDistance)
            )
            StatisticRow(label = "Events Joined (This Year)", value = "$totalEventsThisYear")
            StatisticRow(label = "Events Joined (Overall)", value = "$totalEventsOverall")
        }
    }

}

@Composable
fun StatisticRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
        Text(text = value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
    }
}