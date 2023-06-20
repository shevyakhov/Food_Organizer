package com.chelz.foodorganizer.screens.onboarding

import androidx.fragment.app.Fragment
import com.chelz.foodorganizer.screens.onboarding.ui.OnBoardingFragment
import com.chelz.foodorganizer.utils.FragmentIndex

class OnBoardingIndex : FragmentIndex {

	override fun createInstance(): Fragment = OnBoardingFragment.newInstance()
}