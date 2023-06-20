package com.chelz.foodorganizer.utils.navigators

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.chelz.foodorganizer.R
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.github.terrakok.cicerone.androidx.FragmentScreen

class FoodAppNavigator constructor(
	activity: FragmentActivity,
	containerId: Int,
	fragmentManager: FragmentManager,
) : AppNavigator(
	activity, containerId, fragmentManager
) {

	override fun setupFragmentTransaction(
		screen: FragmentScreen,
		fragmentTransaction: FragmentTransaction,
		currentFragment: Fragment?,
		nextFragment: Fragment,
	) {
		super.setupFragmentTransaction(
			screen,
			fragmentTransaction.setCustomAnimations(
				R.anim.anim_in_right,
				R.anim.anim_out_right,
				R.anim.anim_in_right,
				R.anim.anim_out_right,
			),
			currentFragment,
			nextFragment
		)
	}
}