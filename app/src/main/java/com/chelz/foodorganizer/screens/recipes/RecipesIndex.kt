package com.chelz.foodorganizer.screens.recipes

import androidx.fragment.app.Fragment
import com.chelz.foodorganizer.screens.recipes.ui.RecipesFragment
import com.chelz.foodorganizer.utils.FragmentIndex

object RecipesIndex : FragmentIndex {

	override fun createInstance(): Fragment = RecipesFragment.newInstance()
}