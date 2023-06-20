package com.chelz.foodorganizer.utils.navigators

import com.chelz.foodorganizer.utils.FragmentIndex

interface GlobalNavigator {

	fun open(fragmentIndex: FragmentIndex)

	fun replace(fragmentIndex: FragmentIndex)

	fun newRoot(fragmentIndex: FragmentIndex)

	fun popToRoot()

	fun exit()

	fun finish()

	fun popTo(fragmentIndexClass: Class<FragmentIndex>)
}