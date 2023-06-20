package com.chelz.foodorganizer.utils.navigators

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.chelz.foodorganizer.utils.FragmentIndex
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.Screen
import com.github.terrakok.cicerone.androidx.FragmentScreen

class GlobalNavigatorDefault(
	private val navigator: Router,
) : GlobalNavigator {

	override fun open(fragmentIndex: FragmentIndex) {
		navigator.navigateTo(CustomFragmentScreen(fragmentIndex))
	}

	override fun replace(fragmentIndex: FragmentIndex) {
		navigator.replaceScreen(CustomFragmentScreen(fragmentIndex))
	}

	override fun newRoot(fragmentIndex: FragmentIndex) {
		navigator.newRootScreen(CustomFragmentScreen(fragmentIndex))
	}

	override fun popToRoot() {
		navigator.backTo(null)
	}

	override fun exit() {
		navigator.exit()
	}

	override fun finish() {
		navigator.finishChain()
	}

	override fun popTo(fragmentIndexClass: Class<FragmentIndex>) {
		navigator.backTo(ScreenDefault(fragmentIndexClass.name))
	}
}

private class CustomFragmentScreen(private val fragmentIndex: FragmentIndex) : FragmentScreen {

	override val screenKey: String = fragmentIndex::class.java.name

	override fun createFragment(factory: FragmentFactory): Fragment =
		fragmentIndex.createInstance()
}

private class ScreenDefault(override val screenKey: String) : Screen