package com.chelz.foodorganizer.screens.foodList.presentation.navigation

import com.chelz.foodorganizer.screens.addFood.AddFoodIndex
import com.chelz.foodorganizer.screens.foodDetails.FoodDetailsIndex
import com.chelz.foodorganizer.utils.navigators.GlobalNavigator

class FoodListFragmentNavigatorDefault(
	private val navigator: GlobalNavigator,
) : FoodListFragmentNavigator {

	override fun goToFoodDetails(id: Int) {
		navigator.open(FoodDetailsIndex)
	}

	override fun goToAddFood() {
		navigator.open(AddFoodIndex)
	}
}