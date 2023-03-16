package com.chelz.foodorganizer.screens.foodList

import androidx.fragment.app.Fragment
import com.chelz.foodorganizer.screens.foodList.ui.FoodListFragment
import com.chelz.foodorganizer.screens.foodList.ui.recycler.FoodListItem
import com.chelz.foodorganizer.utils.FragmentIndex

object FoodListIndex : FragmentIndex {

	override fun createInstance(): Fragment = FoodListFragment.newInstance()
}