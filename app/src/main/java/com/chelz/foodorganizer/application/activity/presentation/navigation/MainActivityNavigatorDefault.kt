package com.chelz.foodorganizer.application.activity.presentation.navigation

import com.chelz.foodorganizer.screens.splash.SplashIndex
import com.chelz.foodorganizer.utils.navigators.GlobalNavigator

class MainActivityNavigatorDefault(
	private val navigator: GlobalNavigator
) : MainActivityNavigator {

	override fun newRootScreen() {
		navigator.newRoot(SplashIndex())
	}

}