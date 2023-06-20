package com.chelz.foodorganizer.application.mainFragment.presentation.navigation

import com.chelz.foodorganizer.application.mainFragment.MainScreenValue
import com.chelz.foodorganizer.screens.foodList.FoodListIndex
import com.chelz.foodorganizer.screens.profile.ProfileIndex
import com.chelz.foodorganizer.screens.recipes.RecipesIndex
import com.chelz.foodorganizer.screens.toBuyList.ToBuyListIndex
import com.chelz.foodorganizer.utils.navigators.MainNavigator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MainFragmentNavigatorDefault(private val navigator: MainNavigator) : MainFragmentNavigator {

	override val screenFlow: Flow<MainScreenValue> = navigator.indexStateFlow.map {
		when (it) {
			FoodListIndex  -> MainScreenValue.FoodList
			RecipesIndex   -> MainScreenValue.Recipes
			ToBuyListIndex -> MainScreenValue.ToBuyList
			ProfileIndex   -> MainScreenValue.Profile
			else           -> throw Error("cannot handle ${it::class.simpleName}")
		}
	}

	override fun navigateToFoodList() {
		navigator.open(FoodListIndex)
	}

	override fun navigateToRecipes() {
		navigator.open(RecipesIndex)
	}

	override fun navigateToBuyList() {
		navigator.open(ToBuyListIndex)
	}

	override fun navigateToProfile() {
		navigator.open(ProfileIndex)
	}

	override fun goBack() {
		navigator.exit()
	}
}