package com.chelz.foodorganizer.screens.recipeDetails.presentation.navigation

import com.chelz.foodorganizer.screens.foodDetails.FoodDetailsIndex
import com.chelz.foodorganizer.utils.navigators.GlobalNavigator

class RecipeDetailsNavigatorDefault(private val navigator: GlobalNavigator) : RecipeDetailsNavigator {

	override fun navigateBack() {
		navigator.exit()
	}

	override fun navigateToFoodDetails(foodId: Int) {
		navigator.open(FoodDetailsIndex(foodId))
	}

}