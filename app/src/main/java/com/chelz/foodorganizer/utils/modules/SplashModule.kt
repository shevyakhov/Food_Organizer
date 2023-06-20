package com.chelz.foodorganizer.utils.modules

import com.chelz.foodorganizer.screens.splash.presentation.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val SplashModule = module {
	viewModel {
		SplashViewModel(
			navigator = get(),
			app = get()
		)
	}
}