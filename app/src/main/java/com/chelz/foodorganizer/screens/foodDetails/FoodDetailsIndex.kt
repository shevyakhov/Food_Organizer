package com.chelz.foodorganizer.screens.foodDetails

import androidx.fragment.app.Fragment
import com.chelz.foodorganizer.screens.addFood.ui.AddFoodFragment
import com.chelz.foodorganizer.screens.foodDetails.ui.FoodDetailsFragment
import com.chelz.foodorganizer.utils.FragmentIndex

class FoodDetailsIndex(val id:Int): FragmentIndex {

	override fun createInstance(): Fragment = FoodDetailsFragment.newInstance(id)
}