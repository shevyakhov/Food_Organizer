package com.chelz.foodorganizer.utils.navigators

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.chelz.foodorganizer.screens.foodList.FoodListIndex
import com.chelz.foodorganizer.utils.FragmentIndex
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.FragmentScreen
import kotlinx.coroutines.flow.MutableStateFlow

class MainNavigatorDefault(private val navigator: Router) : MainNavigator {

	override val indexStateFlow = MutableStateFlow<FragmentIndex>(FoodListIndex)

	private val backStack = mutableListOf<FragmentIndex>()

	override fun open(fragmentIndex: FragmentIndex) {
		backStack.remove(fragmentIndex)
		backStack.add(fragmentIndex)
		indexStateFlow.value = fragmentIndex
		navigator.navigateTo(FoodFragmentScreen(fragmentIndex))
	}

	override fun exit() {
		backStack.removeLast()
		if (backStack.isNotEmpty()) {
			open(backStack.last())
		} else {
			navigator.finishChain()
		}
	}
}

private class FoodFragmentScreen(private val fragmentIndex: FragmentIndex) : FragmentScreen {

	override fun createFragment(factory: FragmentFactory): Fragment = fragmentIndex.createInstance()
}