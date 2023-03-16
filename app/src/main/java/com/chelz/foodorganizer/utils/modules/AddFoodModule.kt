package com.chelz.foodorganizer.utils.modules

import com.chelz.foodorganizer.screens.addFood.presentation.AddFoodViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val AddFoodModule = module {
	viewModel {
		AddFoodViewModel(
			navigator = get()
		)
	}
}