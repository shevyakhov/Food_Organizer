package com.chelz.foodorganizer.screens.recipes.presentation.navigation

interface RecipesNavigator {

	fun goToRecipeDetails(id: Int)

	fun goToAddRecipe()

	fun goBack()
}