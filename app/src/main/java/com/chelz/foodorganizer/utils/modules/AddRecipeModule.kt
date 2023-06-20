package com.chelz.foodorganizer.utils.modules

import com.chelz.foodorganizer.screens.addRecipe.presentation.AddRecipeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val AddRecipeModule = module {
	viewModel {
		AddRecipeViewModel(
			navigator = get(),
			repository = get()
		)
	}
}