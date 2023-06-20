package com.chelz.foodorganizer.screens.recipeDetails.presentation.navigation

interface RecipeDetailsNavigator {

	fun navigateBack()
	fun navigateToFoodDetails(foodId: Int)
}