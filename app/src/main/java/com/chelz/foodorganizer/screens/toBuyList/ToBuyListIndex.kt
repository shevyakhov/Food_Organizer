package com.chelz.foodorganizer.screens.toBuyList

import androidx.fragment.app.Fragment
import com.chelz.foodorganizer.screens.toBuyList.ui.ToBuyListFragment
import com.chelz.foodorganizer.utils.FragmentIndex

object ToBuyListIndex : FragmentIndex {

	override fun createInstance(): Fragment = ToBuyListFragment.newInstance()
}