package com.chelz.foodorganizer.screens.onboarding.presentation

import androidx.lifecycle.ViewModel
import com.chelz.foodorganizer.screens.onboarding.presentation.navigation.OnBoardingFragmentNavigator

class OnBoardingViewModel(val navigator: OnBoardingFragmentNavigator) : ViewModel() {

	fun navigateToRegistration() {
		navigator.goToRegistration()
	}
}