package com.chelz.foodorganizer.application.activity.presentation

import androidx.lifecycle.ViewModel
import com.chelz.foodorganizer.application.activity.presentation.navigation.MainActivityNavigator

class MainActivityViewModel(
	private val navigator: MainActivityNavigator,
) : ViewModel() {

	fun openMainRoot() {
		navigator.newRootScreen()
	}
}
