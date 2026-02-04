package com.example.runnito.screens.eventdetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.runnito.components.ImageBanner
import com.example.runnito.components.RunningManLoader

@Composable
fun EventDetailsScreen(eventId: String?, eventDetailsViewModel: EventDetailsViewModel) {
    LaunchedEffect(Unit) {
        eventDetailsViewModel.loadEvent(eventId?.toIntOrNull())
    }
    val eventState = eventDetailsViewModel.event.collectAsState().value

    if (eventState.loading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            RunningManLoader()
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                modifier = Modifier.padding(12.dp),
                text = "${eventState.data?.title}",
                overflow = TextOverflow.Visible,
                style = MaterialTheme.typography.headlineMedium
            )
            ImageBanner(modifier = Modifier.padding(12.dp), url = eventState.data?.bannerUrl)


            Text(text = "Distances Available", style = MaterialTheme.typography.headlineMedium)
            Row() {
                eventState.data?.distanceAvailable?.forEach { distance ->
                    Text(distance.displayName)
                }
            }
Text("SUBTITLE ${eventState.data?.subtitle}")
            Text(
                buildAnnotatedString {
                    withLink(
                        LinkAnnotation.Url(
                            url = eventState.data?.registrationLink ?: "",
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


}