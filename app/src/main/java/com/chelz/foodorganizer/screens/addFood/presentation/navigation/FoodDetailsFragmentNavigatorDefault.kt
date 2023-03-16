package com.chelz.foodorganizer.screens.addFood.presentation.navigation

import com.chelz.foodorganizer.screens.foodList.FoodListIndex
import com.chelz.foodorganizer.screens.foodList.ui.recycler.FoodListItem
import com.chelz.foodorganizer.utils.navigators.GlobalNavigator

class AddFoodFragmentNavigatorDefault(
	private val navigator: GlobalNavigator
) : AddFoodFragmentNavigator {


	override fun goBackWithChanges(item: FoodListItem) {
		navigator.replace(FoodListIndex)
	}
}