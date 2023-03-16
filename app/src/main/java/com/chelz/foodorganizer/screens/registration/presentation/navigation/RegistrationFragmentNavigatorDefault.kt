package com.chelz.foodorganizer.screens.registration.presentation.navigation

import com.chelz.foodorganizer.application.mainFragment.presentation.MainIndex
import com.chelz.foodorganizer.utils.navigators.GlobalNavigator

class RegistrationFragmentNavigatorDefault(
	private val navigator: GlobalNavigator,
) : RegistrationFragmentNavigator {

	override fun goToMain() {
		navigator.newRoot(MainIndex)
	}
}