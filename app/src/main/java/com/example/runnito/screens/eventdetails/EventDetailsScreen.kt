package com.example.runnito.screens.eventdetails

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.runnito.components.ImageBanner
import com.example.runnito.components.RunningManLoader
import com.example.runnito.model.Distance
import com.example.runnito.model.event.EventModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailsScreen(
    eventId: String?,
    eventDetailsViewModel: EventDetailsViewModel,
    paddingValues: PaddingValues
) {
    LaunchedEffect(eventId) {
        Log.i("EVENTID", "EVENTSTATE ${eventId}")
        eventDetailsViewModel.loadEvent(eventId?.toIntOrNull())
    }
    val isEventRegistered by eventDetailsViewModel.isEventRegistered.collectAsStateWithLifecycle()
    val eventState = eventDetailsViewModel.event.collectAsStateWithLifecycle().value

    Log.i("TEST", "EVENTSTATE ${eventState.data}")
    if (eventState.loading) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            RunningManLoader()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                modifier = Modifier.padding(12.dp),
                text = "${eventState.data?.title}",
                style = MaterialTheme.typography.headlineMedium
            )
            ImageBanner(modifier = Modifier.padding(12.dp), url = eventState.data?.bannerUrl)
            EventDescription(
                eventState.data, onAddEvent = { eventId, distance ->
                    eventDetailsViewModel.addRegisteredEvent(eventId, distance)
                }, isEventRegistered = isEventRegistered
            )

        }
    }

}


@Composable
fun EventDescription(
    event: EventModel?,
    onAddEvent: (Int?, Distance) -> Unit,
    isEventRegistered: Boolean
) {
    var showDistanceDialog by remember { mutableStateOf(false) }

    if (showDistanceDialog) {
        DistanceSelectionDialog(
            distances = event?.distanceAvailable ?: emptyList(),
            onDismiss = { showDistanceDialog = false },
            onConfirm = { selectedDistance ->
                // Here you can use the selectedDistance
                onAddEvent(event?.id, selectedDistance)
                showDistanceDialog = false
            }
        )
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Text(
                modifier = Modifier
                    .padding(8.dp),
                text = "${event?.description}",
                fontStyle = FontStyle.Italic,
                fontSize = 14.sp,
            )
        }

        EventDistance(event)
        EventRegistrationLink(event)

        //Only Show Add Event Button when there are available distances and the user is not already registered
        if (!event?.distanceAvailable.isNullOrEmpty() && !isEventRegistered) {
            AddEventButton(event.id) {
                showDistanceDialog = true
            }
        }

    }
}


@Composable
fun EventDistance(event: EventModel?) {
    if (event?.distanceAvailable.isNullOrEmpty()) {
        Text(
            text = "No Distances Details Available",
            fontSize = 15.sp,
            fontWeight = FontWeight.ExtraLight
        )
    } else {
        Text(
            modifier = Modifier.padding(8.dp),
            text = "Distances Available",
            style = MaterialTheme.typography.headlineMedium
        )


        Row {
            event.distanceAvailable?.forEachIndexed { index, distance ->
                Text(
                    modifier = Modifier.padding(horizontal = 4.dp),
                    text = distance.displayName
                )
                if (index < (event.distanceAvailable?.size ?: 0) - 1) {
                    Text(text = "|")
                }
            }
        }

    }
}

@Composable
fun EventRegistrationLink(event: EventModel?) {
    if (event?.registrationLink.isNullOrBlank()) {
        Text(
            text = "No Registration Link Available",
            fontSize = 15.sp,
            fontWeight = FontWeight.ExtraLight
        )
    } else {
        Text(
            buildAnnotatedString {
                withLink(
                    LinkAnnotation.Url(
                        url = event.registrationLink ?: "",
                        // Optional: Customize link styling
                        styles = TextLinkStyles(
                            style = SpanStyle(
                                color = Color.Blue,
                                textDecoration = TextDecoration.Underline,
                                fontWeight = FontWeight.Light,
                                fontSize = 15.sp
                            )
                        ),

                        )
                ) {
                    append("Online Registration")
                }

            }

        )

    }
}

@Composable
fun AddEventButton(eventId: Int?, onAddEvent: (Int?) -> Unit) {
    Button(
        onClick = { onAddEvent(eventId) },
        modifier = Modifier.padding(top = 8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add Event Icon",
            modifier = Modifier.size(20.dp)
        )
        Text(text = "Add Event", modifier = Modifier.padding(start = 4.dp))
    }
}

@Composable
fun DistanceSelectionDialog(
    distances: List<Distance>,
    onDismiss: () -> Unit,
    onConfirm: (Distance) -> Unit
) {
    var selectedOption by remember { mutableStateOf(distances.firstOrNull()) }

    if (distances.isEmpty()) {
        return
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select a Distance") },
        text = {
            Column {
                distances.forEach { distance ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedOption = distance }
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (distance == selectedOption),
                            onClick = { selectedOption = distance }
                        )
                        Text(
                            text = distance.displayName,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    selectedOption?.let { onConfirm(it) }
                },
                enabled = selectedOption != null
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
