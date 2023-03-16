package com.chelz.foodorganizer.application.mainFragment.presentation.navigation

import com.chelz.foodorganizer.application.mainFragment.MainScreenValue
import kotlinx.coroutines.flow.Flow

interface MainFragmentNavigator {

	val screenFlow: Flow<MainScreenValue>

	fun navigateToFoodList()

	fun navigateToRecipes()

	fun navigateToBuyList()

	fun navigateToProfile()

	fun goBack()
}