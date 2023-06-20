package com.chelz.foodorganizer.screens.settings

import androidx.fragment.app.Fragment
import com.chelz.foodorganizer.screens.settings.ui.SettingsFragment
import com.chelz.foodorganizer.utils.FragmentIndex

class SettingsIndex:FragmentIndex {

	override fun createInstance(): Fragment = SettingsFragment.newInstance()
}