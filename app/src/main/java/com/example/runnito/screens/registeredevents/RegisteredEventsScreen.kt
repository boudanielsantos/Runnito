package com.example.runnito.screens.registeredevents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.runnito.model.Distance
import com.example.runnito.model.event.EventModel
import com.example.runnito.viewmodel.RegisteredEventsViewModel

@Composable
fun RegisteredEventsScreen(
    viewModel: RegisteredEventsViewModel,
    onEventClick: (Int) -> Unit
) {
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
