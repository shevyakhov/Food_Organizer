package com.chelz.foodorganizer.utils.navigators

import com.chelz.foodorganizer.utils.FragmentIndex
import kotlinx.coroutines.flow.StateFlow

interface MainNavigator {

	val indexStateFlow: StateFlow<FragmentIndex>

	fun open(fragmentIndex: FragmentIndex)

	fun exit()
}