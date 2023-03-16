package com.chelz.foodorganizer.application.mainFragment.presentation

import androidx.fragment.app.Fragment
import com.chelz.foodorganizer.application.mainFragment.ui.MainFragment
import com.chelz.foodorganizer.utils.FragmentIndex

object MainIndex : FragmentIndex {

	override fun createInstance(): Fragment = MainFragment()
}