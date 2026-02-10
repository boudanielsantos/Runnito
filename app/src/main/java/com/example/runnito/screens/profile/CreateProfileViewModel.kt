package com.example.runnito.screens.profile

import androidx.compose.animation.core.copy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.runnito.model.user.User
import com.example.runnito.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.concurrent.atomics.update

@HiltViewModel
class CreateProfileViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {


    private val _uiState = MutableStateFlow(CreateProfileUiState())
    val uiState = _uiState.asStateFlow()

    fun onNameChange(newName: String) {
        _uiState.update { it.copy(name = newName) }
    }

    fun onEmailChange(newEmail: String) {
        _uiState.update { it.copy(email = newEmail) }
    }

    fun onProfilePictureChange(uri: String) {
        _uiState.update { it.copy(profilePictureUri = uri) }
    }

    fun saveUserProfile() {
        if (_uiState.value.name.isBlank() || _uiState.value.email.isBlank()) {
            // Optional: Add error handling for blank fields
            return
        }

        viewModelScope.launch {
            val newUser = User(
                name = _uiState.value.name.trim(),
                email = _uiState.value.email.trim(),
                profilePicture = _uiState.value.profilePictureUri.trim()
            )
            userRepository.insertUser(newUser)

        }
    }
}

data class CreateProfileUiState(
    val name: String = "",
    val email: String = "",
    val profilePictureUri: String = ""
)
