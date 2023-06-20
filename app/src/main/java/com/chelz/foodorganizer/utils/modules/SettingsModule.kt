package com.chelz.foodorganizer.utils.modules

import com.chelz.foodorganizer.screens.settings.presentation.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val SettingsModule = module {
	viewModel {
		SettingsViewModel(
			navigator = get(),
			notificationManager = get(),
			app = get(),
			repository = get()
		)
	}
}