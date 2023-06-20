package com.chelz.foodorganizer.screens.profile

import androidx.fragment.app.Fragment
import com.chelz.foodorganizer.screens.profile.ui.ProfileFragment
import com.chelz.foodorganizer.utils.FragmentIndex

object ProfileIndex : FragmentIndex {

	override fun createInstance(): Fragment = ProfileFragment.newInstance()
}