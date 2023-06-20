package com.chelz.foodorganizer.screens.addToBuyList.presentation.navigation

import com.chelz.foodorganizer.utils.navigators.GlobalNavigator

class AddToBuyListNavigatorDefault(private val navigator: GlobalNavigator) : AddToBuyListNavigator {

	override fun navigateBack() {
		navigator.exit()
	}
}