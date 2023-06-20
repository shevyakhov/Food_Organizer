package com.chelz.foodorganizer.utils.modules

import com.chelz.foodorganizer.screens.foodDetails.presentation.FoodDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val FoodDetailsModule = module {
	viewModel { (foodId: Int?) ->
		FoodDetailsViewModel(
			foodId = foodId ?: 0,
			repository = get(),
			navigator = get(),
			notificationManager = get()
		)
	}
}