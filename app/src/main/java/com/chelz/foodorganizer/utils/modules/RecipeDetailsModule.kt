package com.chelz.foodorganizer.utils.modules

import com.chelz.foodorganizer.screens.recipeDetails.presentation.RecipeDetailsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val RecipeDetailsModule = module {
	viewModel { (recipeId: Int) ->
		RecipeDetailsViewModel(
			recipeId = recipeId,
			navigator = get(),
			recipesRepository = get(),
			foodRepository = get()
		)
	}
}