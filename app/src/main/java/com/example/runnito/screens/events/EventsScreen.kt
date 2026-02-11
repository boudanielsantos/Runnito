package com.example.runnito.screens.events

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.runnito.components.RunningManLoader
import com.example.runnito.model.event.EventModel
import com.example.runnito.ui.theme.april
import com.example.runnito.ui.theme.august
import com.example.runnito.ui.theme.december
import com.example.runnito.ui.theme.february
import com.example.runnito.ui.theme.january
import com.example.runnito.ui.theme.july
import com.example.runnito.ui.theme.june
import com.example.runnito.ui.theme.march
import com.example.runnito.ui.theme.may
import com.example.runnito.ui.theme.november
import com.example.runnito.ui.theme.october
import com.example.runnito.ui.theme.september
import java.util.Locale


@Composable
fun EventsScreen(viewModel: EventsViewModel, onNavigateToEventDetails: (String) -> Unit) {

    val eventsState = viewModel.events.collectAsStateWithLifecycle().value
    var selectedMonth by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    val months = eventsState.data?.map { it.month }?.distinct()
    val filteredEvents = eventsState.data?.filter { event ->
        val monthMatches = selectedMonth == null || event.month == selectedMonth
        val searchMatches =
            searchQuery.isEmpty() || event.title.contains(searchQuery, ignoreCase = true)
        monthMatches && searchMatches
    }



    if (eventsState.loading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            RunningManLoader()
        }

    } else if (filteredEvents == null || filteredEvents.isEmpty()) {
        EventFilters(
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            months = months,
            selectedMonth = selectedMonth,
            onMonthSelected = { selectedMonth = it }
        )
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "No matching events found.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    } else {
        Column {
            EventFilters(
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                months = months,
                selectedMonth = selectedMonth,
                onMonthSelected = { selectedMonth = it }
            )
            EventContent(filteredEvents, onNavigateToEventDetails = onNavigateToEventDetails)
        }
    }

}

@Composable
fun EventFilters(
    modifier: Modifier = Modifier,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    months: List<String>?, selectedMonth: String?,
    onMonthSelected: (String?) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(modifier = modifier) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            label = { Text("Search by title") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    keyboardController?.hide()
                }
            )
        )
        MonthFilter(
            months = months,
            selectedMonth = selectedMonth,
            onMonthSelected = onMonthSelected
        )
    }

}

@Composable
fun MonthFilter(
    months: List<String>?,
    selectedMonth: String?,
    onMonthSelected: (String?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = selectedMonth ?: "All Months",
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }
                .background(Color.LightGray)
                .padding(16.dp)
        )
        DropdownMenu(

            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            DropdownMenuItem(
                text = { Text("All Months") },
                onClick = {
                    onMonthSelected(null)
                    expanded = false
                }
            )
            months?.forEach { month ->
                DropdownMenuItem(
                    text = { Text(month) },
                    onClick = {
                        onMonthSelected(month)
                        expanded = false
                    }
                )
            }
        }
    }
}


@Composable
fun EventContent(eventsList: List<EventModel>, onNavigateToEventDetails: (String) -> Unit) {
    EventBanner(eventsList, onNavigateToEventDetails)
}

@Composable
fun EventBanner(eventsList: List<EventModel>, onNavigateToEventDetails: (String) -> Unit) {
    LazyColumn() {
        items(items = eventsList) { event ->
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .clickable { onNavigateToEventDetails(event.id.toString()) }

            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(color = getMonthColor(event.month))
                ) {

                    Text(
                        text = "${event.month} ${event.day}",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp)
                    )
                    Text(
                        text = event.title,
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(16.dp)
                    )
                }
            }
        }
    }


}


@Composable
private fun getMonthColor(month: String): Color {
    return when (month.lowercase(Locale.ROOT)) {
        "january" -> MaterialTheme.colorScheme.january
        "february" -> MaterialTheme.colorScheme.february
        "march" -> MaterialTheme.colorScheme.march
        "april" -> MaterialTheme.colorScheme.april
        "may" -> MaterialTheme.colorScheme.may
        "june" -> MaterialTheme.colorScheme.june
        "july" -> MaterialTheme.colorScheme.july
        "august" -> MaterialTheme.colorScheme.august
        "september" -> MaterialTheme.colorScheme.september
        "october" -> MaterialTheme.colorScheme.october
        "november" -> MaterialTheme.colorScheme.november
        "december" -> MaterialTheme.colorScheme.december
        else -> MaterialTheme.colorScheme.surfaceVariant
    }
}

