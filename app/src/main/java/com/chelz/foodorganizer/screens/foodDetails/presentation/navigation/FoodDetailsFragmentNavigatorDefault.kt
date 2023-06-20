package com.chelz.foodorganizer.screens.foodDetails.presentation.navigation

import com.chelz.foodorganizer.utils.navigators.GlobalNavigator

class FoodDetailsFragmentNavigatorDefault(
	private val navigator: GlobalNavigator,
) : FoodDetailsFragmentNavigator {

	override fun navigateBack() {
		navigator.exit()
	}
}