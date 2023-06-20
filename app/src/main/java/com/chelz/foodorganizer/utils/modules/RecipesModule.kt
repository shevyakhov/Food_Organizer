package com.chelz.foodorganizer.utils.modules

import com.chelz.foodorganizer.screens.recipes.presentation.RecipesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val RecipesModule = module {
	viewModel {
		RecipesViewModel(
			navigator = get(),
			app = get(),
			repository = get()
		)
	}
}