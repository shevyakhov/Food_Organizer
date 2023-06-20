package com.chelz.foodorganizer.screens.toBuyList.presentation.navigation

import com.chelz.foodorganizer.screens.addToBuyList.AddToBuyListIndex
import com.chelz.foodorganizer.utils.navigators.GlobalNavigator

class ToBuyListNavigatorDefault(val navigator: GlobalNavigator) : ToBuyListNavigator {

	override fun navigateToToBuyListFragment() {
		navigator.open(AddToBuyListIndex())
	}
}