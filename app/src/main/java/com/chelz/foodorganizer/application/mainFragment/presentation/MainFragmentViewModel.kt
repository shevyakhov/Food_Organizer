package com.chelz.foodorganizer.application.mainFragment.presentation

import androidx.lifecycle.ViewModel
import com.chelz.foodorganizer.application.mainFragment.MainScreenValue
import com.chelz.foodorganizer.application.mainFragment.presentation.navigation.MainFragmentNavigator
import kotlinx.coroutines.flow.Flow

class MainFragmentViewModel(
	private val navigator: MainFragmentNavigator,
) : ViewModel() {

	val mainScreenValueFlow: Flow<MainScreenValue> = navigator.screenFlow

	init {
		navigator.navigateToFoodList()
	}

	fun navigateBack() {
		navigator.goBack()
	}

	fun navigateToFoodList() {
		navigator.navigateToFoodList()
	}

	fun navigateToRecipes() {
		navigator.navigateToRecipes()
	}

	fun navigateToToBuyList() {
		navigator.navigateToBuyList()
	}

	fun navigateToProfile() {
		navigator.navigateToProfile()
	}
}