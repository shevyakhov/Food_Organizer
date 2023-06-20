package com.chelz.foodorganizer.utils.modules

import com.chelz.foodorganizer.screens.registration.presentation.RegistrationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val RegistrationModule = module {
	viewModel {
		RegistrationViewModel(
			navigator = get(),
			app = get()
		)
	}
}