package com.chelz.foodorganizer.screens.profile.presentation.navigation

import com.chelz.foodorganizer.screens.settings.SettingsIndex
import com.chelz.foodorganizer.utils.navigators.GlobalNavigator

class ProfileNavigatorDefault(private val navigator: GlobalNavigator):ProfileNavigator {

	override fun navigateToSettingsFragment() {
		navigator.open(SettingsIndex())
	}
}