package com.chelz.foodorganizer.screens.onboarding.presentation.navigation

import com.chelz.foodorganizer.screens.registration.RegistrationIndex
import com.chelz.foodorganizer.utils.navigators.GlobalNavigator

class OnBoardingFragmentNavigatorDefault(
	private val navigator: GlobalNavigator,
) : OnBoardingFragmentNavigator {

	override fun goToRegistration() {
		navigator.newRoot(RegistrationIndex)
	}
}