package com.chelz.foodorganizer.screens.addRecipe.presentation.navigation

import com.chelz.foodorganizer.utils.navigators.GlobalNavigator

class AddRecipeNavigatorDefault(private val navigator: GlobalNavigator) : AddRecipeNavigator {

	override fun navigateBack() {
		navigator.exit()
	}
}