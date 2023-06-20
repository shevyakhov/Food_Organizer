package com.chelz.foodorganizer.utils.modules

import com.chelz.foodorganizer.screens.foodList.presentation.FoodListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val FoodListModule = module {
	viewModel {
		FoodListViewModel(
			navigator = get(),
			app = get(),
			repository = get()
		)
	}
}