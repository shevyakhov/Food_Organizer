package com.chelz.foodorganizer.screens.addToBuyList

import androidx.fragment.app.Fragment
import com.chelz.foodorganizer.screens.addToBuyList.ui.AddToBuyListFragment
import com.chelz.foodorganizer.utils.FragmentIndex

class AddToBuyListIndex:FragmentIndex {

	override fun createInstance(): Fragment = AddToBuyListFragment.newInstance()
}