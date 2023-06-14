package com.kinandcarta.create.proxytoggle.repository.userprefs

import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {

    val isNightMode: Flow<Boolean>

    suspend fun toggleTheme()
}
