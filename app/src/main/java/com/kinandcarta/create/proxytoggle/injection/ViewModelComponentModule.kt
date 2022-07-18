package com.kinandcarta.create.proxytoggle.injection

import com.kinandcarta.create.proxytoggle.android.ThemeSwitcherImpl
import com.kinandcarta.create.proxytoggle.core.android.ThemeSwitcher
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface ViewModelComponentModule {

    @Binds
    fun bindThemeSwitcher(
        themeSwitcher: ThemeSwitcherImpl
    ): ThemeSwitcher
}
