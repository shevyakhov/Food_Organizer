package com.chelz.foodorganizer.screens.addFood.presentation.navigation

import com.chelz.foodorganizer.screens.foodList.ui.recycler.FoodListItem

interface AddFoodFragmentNavigator {

	fun goBackWithChanges(item:FoodListItem)
}