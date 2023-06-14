package com.kinandcarta.create.proxytoggle.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kinandcarta.create.proxytoggle.repository.userprefs.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val useDarkTheme = userPreferencesRepository.isNightMode.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = false
    )
}
