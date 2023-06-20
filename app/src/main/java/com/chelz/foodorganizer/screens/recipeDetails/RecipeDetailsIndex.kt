package com.chelz.foodorganizer.screens.recipeDetails

import androidx.fragment.app.Fragment
import com.chelz.foodorganizer.screens.recipeDetails.ui.RecipeDetailsFragment
import com.chelz.foodorganizer.utils.FragmentIndex

class RecipeDetailsIndex(val id: Int) : FragmentIndex {

	override fun createInstance(): Fragment = RecipeDetailsFragment.newInstance(id)
}