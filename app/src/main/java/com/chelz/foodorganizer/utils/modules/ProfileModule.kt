package com.chelz.foodorganizer.utils.modules

import com.chelz.foodorganizer.screens.profile.presentation.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ProfileModule = module {
	viewModel {
		ProfileViewModel(
			navigator = get(),
			repository = get()
		)
	}
}