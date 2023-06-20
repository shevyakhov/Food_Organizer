package com.chelz.foodorganizer.screens.addRecipe

import androidx.fragment.app.Fragment
import com.chelz.foodorganizer.screens.addRecipe.ui.AddRecipeFragment
import com.chelz.foodorganizer.utils.FragmentIndex

class AddRecipeIndex:FragmentIndex {

	override fun createInstance(): Fragment = AddRecipeFragment.newInstance()
}