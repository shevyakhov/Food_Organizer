package com.chelz.foodorganizer.utils.modules

import com.chelz.foodorganizer.screens.onboarding.presentation.OnBoardingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val OnBoardingModule = module {
	viewModel {
		OnBoardingViewModel(
			navigator = get()
		)
	}
}