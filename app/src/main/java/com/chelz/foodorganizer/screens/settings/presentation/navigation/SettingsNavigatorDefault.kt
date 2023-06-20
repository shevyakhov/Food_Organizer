package com.chelz.foodorganizer.screens.settings.presentation.navigation

import com.chelz.foodorganizer.utils.navigators.GlobalNavigator

class SettingsNavigatorDefault(private val navigator: GlobalNavigator) : SettingsNavigator {

	override fun navigateBack() {
		navigator.exit()
	}
}