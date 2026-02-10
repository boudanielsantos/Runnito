package com.example.runnito.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.example.runnito.navigation.ScreenRoutes
import com.example.runnito.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class MainViewModel @Inject constructor(userRepository: UserRepository) :
    ViewModel() {
    val startDestination = userRepository.getFirstUser().map { user ->
        if (user == null) ScreenRoutes.CreateProfile.route else ScreenRoutes.Events.route
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null as String?
    )
}