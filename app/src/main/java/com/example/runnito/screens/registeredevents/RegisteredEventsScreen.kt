package com.example.runnito.ui.screen // Or your appropriate UI package

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import androidx.hilt.navigation.compose.hiltViewModel
import com.example.runnito.model.Distance
import com.example.runnito.model.event.EventModel
import com.example.runnito.ui.theme.RunnitoTheme
import com.example.runnito.viewmodel.RegisteredEventsViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun RegisteredEventsScreen(
    viewModel: RegisteredEventsViewModel,
    onEventClick: (Int) -> Unit
) {
    // Collect the state from the ViewModel
    val groupedEvents by viewModel.groupedEvents.collectAsState()

    if (groupedEvents.isEmpty()) {
        EmptyState()
    } else {
        RegisteredEventsList(
            groupedEvents = groupedEvents,
        ) {
            onEventClick(it)
        }
    }

}

@Composable
fun RegisteredEventsList(
    groupedEvents: Map<String, List<EventModel>>,
    modifier: Modifier = Modifier,
    onEventClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { Spacer(modifier = Modifier.height(8.dp)) }


        val sortedGroupedEvents = groupedEvents.toList().sortedBy { (distance, _) ->
            Distance.fromDisplayName(distance)?.intValue ?: Int.MAX_VALUE
        }.toMap(LinkedHashMap())


        sortedGroupedEvents.forEach { (distance, events) ->
            item {
                DistanceHeader(distance)
            }

            items(events, key = { it.id }) { event ->
                EventCard(event) {
                    onEventClick(it)
                }
            }
        }
        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}

@Composable
fun DistanceHeader(distance: String) {
    Text(
        text = distance,
        style = MaterialTheme.typography.headlineSmall.copy(
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )
}

@Composable
fun EventCard(event: EventModel, onEventClick: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onEventClick(event.id)
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = event.title,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Location: ${event.description}",
                style = MaterialTheme.typography.bodyMedium
            )

        }
    }
}

@Composable
fun EmptyState(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "You haven't registered for any events yet.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}
