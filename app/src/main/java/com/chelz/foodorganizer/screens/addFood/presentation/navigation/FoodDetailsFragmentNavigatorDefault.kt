package com.chelz.foodorganizer.screens.addFood.presentation.navigation

import com.chelz.foodorganizer.utils.navigators.GlobalNavigator

class AddFoodFragmentNavigatorDefault(
	private val navigator: GlobalNavigator,
) : AddFoodFragmentNavigator {

	override fun navigateBack() {
		navigator.exit()
	}
}