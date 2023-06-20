package com.chelz.foodorganizer.screens.splash.presentation

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import com.chelz.foodorganizer.screens.splash.presentation.navigation.SplashFragmentNavigator

class SplashViewModel(val navigator: SplashFragmentNavigator, val app: Application) : AndroidViewModel(app) {

	private fun isUserLogged(): Boolean {

		val sharedPreference = app.getSharedPreferences("USER", Context.MODE_PRIVATE)
		return sharedPreference.getBoolean("isLogged", false)
	}

	fun init() {
		if (!isUserLogged()) {
			navigator.goToOnBoarding()
		} else
			navigator.goToMain()
	}

}