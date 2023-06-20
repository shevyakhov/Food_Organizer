package com.chelz.foodorganizer.screens.splash.presentation.navigation

import com.chelz.foodorganizer.application.mainFragment.presentation.MainIndex
import com.chelz.foodorganizer.screens.onboarding.OnBoardingIndex
import com.chelz.foodorganizer.utils.navigators.GlobalNavigator

class SplashFragmentNavigatorDefault(
	private val navigator: GlobalNavigator,
) : SplashFragmentNavigator {

	override fun goToOnBoarding() {
		navigator.replace(OnBoardingIndex())
	}

	override fun goToMain() {
		navigator.replace(MainIndex)
	}
}