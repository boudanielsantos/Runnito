package com.example.runnito.screens.eventdetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.example.runnito.components.ImageBanner
import com.example.runnito.components.RunningManLoader
import com.example.runnito.model.EventModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailsScreen(
    eventId: String?,
    eventDetailsViewModel: EventDetailsViewModel,
    paddingValues: PaddingValues
) {
    LaunchedEffect(Unit) {
        eventDetailsViewModel.loadEvent(eventId?.toIntOrNull())
    }
    val eventState = eventDetailsViewModel.event.collectAsState().value
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
            EventDescription(eventState.data, onAddEvent = {})

        }
    }

}


@Composable
fun EventDescription(event: EventModel?, onAddEvent: (Int?) -> Unit) {
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

        AddEventButton(event?.id) {
            onAddEvent(it)
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