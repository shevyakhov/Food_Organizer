package com.chelz.foodorganizer.screens.recipes.presentation.navigation

import com.chelz.foodorganizer.screens.addRecipe.AddRecipeIndex
import com.chelz.foodorganizer.screens.recipeDetails.RecipeDetailsIndex
import com.chelz.foodorganizer.utils.navigators.GlobalNavigator

class RecipesNavigatorDefault(
	private val navigator: GlobalNavigator,
) : RecipesNavigator {

	override fun goToRecipeDetails(id: Int) {
		navigator.open(RecipeDetailsIndex(id))
	}

	override fun goToAddRecipe() {
		navigator.open(AddRecipeIndex())
	}

	override fun goBack() {
		navigator.finish()
	}
}