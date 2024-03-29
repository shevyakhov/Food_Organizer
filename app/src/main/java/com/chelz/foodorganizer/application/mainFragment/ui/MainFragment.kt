package com.chelz.foodorganizer.application.mainFragment.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.chelz.foodorganizer.R
import com.chelz.foodorganizer.application.mainFragment.MainScreenValue
import com.chelz.foodorganizer.application.mainFragment.presentation.MainFragmentViewModel
import com.chelz.foodorganizer.databinding.FragmentMainBinding
import com.chelz.foodorganizer.utils.modules.MainNavigatorName.MAIN
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.androidx.AppNavigator
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named

class MainFragment : Fragment() {

	private val navigatorHolder: NavigatorHolder by inject(named(MAIN))
	private val viewModel by viewModel<MainFragmentViewModel>()
	private var _binding: FragmentMainBinding? = null
	private val binding get() = _binding!!

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentMainBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		initCicerone()
	}

	private fun initCicerone() {
		val navigator = AppNavigator(requireActivity(), R.id.host_main, childFragmentManager)
		navigatorHolder.setNavigator(navigator)

		viewModel.mainScreenValueFlow.onEach {
			binding.bottomNavigation.selectedItemId = when (it) {
				MainScreenValue.FoodList  -> R.id.foodList
				MainScreenValue.Recipes   -> R.id.recipes
				MainScreenValue.ToBuyList -> R.id.toBuyList
				MainScreenValue.Profile   -> R.id.profile
			}
		}.launchIn(lifecycleScope)


		binding.bottomNavigation.setOnItemSelectedListener {
			if (binding.bottomNavigation.selectedItemId != it.itemId)
				when (it.itemId) {
					R.id.foodList  -> viewModel.navigateToFoodList()
					R.id.recipes   -> viewModel.navigateToRecipes()
					R.id.toBuyList -> viewModel.navigateToToBuyList()
					R.id.profile   -> viewModel.navigateToProfile()
				}
			true
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
		navigatorHolder.removeNavigator()
	}
}
