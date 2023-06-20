package com.chelz.foodorganizer.screens.addFood

import androidx.fragment.app.Fragment
import com.chelz.foodorganizer.screens.addFood.ui.AddFoodFragment
import com.chelz.foodorganizer.utils.FragmentIndex

object AddFoodIndex : FragmentIndex {

	override fun createInstance(): Fragment = AddFoodFragment.newInstance()
}