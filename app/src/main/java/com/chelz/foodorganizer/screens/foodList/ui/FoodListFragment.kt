package com.chelz.foodorganizer.screens.foodList.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.lottie.LottieDrawable
import com.chelz.foodorganizer.R
import com.chelz.foodorganizer.databinding.FragmentFoodListBinding
import com.chelz.foodorganizer.screens.foodList.domain.toListItem
import com.chelz.foodorganizer.screens.foodList.presentation.FoodListViewModel
import com.chelz.foodorganizer.screens.foodList.ui.recycler.FoodListAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.Tab
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FoodListFragment : Fragment() {

	companion object {

		fun newInstance() = FoodListFragment()
	}

	private var _binding: FragmentFoodListBinding? = null
	private val binding get() = _binding!!
	private val viewModel by viewModel<FoodListViewModel>()

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentFoodListBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		val adapter = FoodListAdapter(viewModel) {
			viewModel.goToFoodDetailsFragment(it.id)
		}

		binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
		binding.recyclerView.adapter = adapter
		binding.topAppBar.setOnMenuItemClickListener { menuItem ->
			when (menuItem.itemId) {
				R.id.filter -> {
					val builder = MaterialAlertDialogBuilder(requireContext())
					builder.setTitle("Фильтрация")
					builder.setMessage("Как отфильтровать список?")

					builder.setPositiveButton("По весу") { dialog, which ->
						adapter.submitList(adapter.currentList.sortedBy { it.itemCount.toFloat() })
					}
					builder.setNegativeButton("По названию") { dialog, which ->
						adapter.submitList(adapter.currentList.sortedBy { it.name })
					}

					builder.setNeutralButton(R.string.cancel) { dialog, which -> }
					builder.show()
					true
				}

				else        -> false
			}
		}
		binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
			override fun onQueryTextSubmit(p0: String?): Boolean {
				return false
			}

			override fun onQueryTextChange(p0: String?): Boolean {
				if (!p0.isNullOrEmpty()) {
					val newList = viewModel.foodListFlow.value.filter { it.name.contains(p0) }.map { it.toListItem() }.sortedBy { it.bestBefore }
					adapter.submitList(newList)
				} else adapter.submitList(viewModel.foodListFlow.value.map { it.toListItem() })
				return false
			}

		})
		binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
			override fun onTabSelected(tab: Tab) {
				val text = tab.text.toString()
				adapter.submitList(viewModel.filterByTabText(text).toMutableList().map { it.toListItem() }.sortedBy { it.bestBefore })
			}

			override fun onTabUnselected(tab: Tab?) {

			}

			override fun onTabReselected(tab: Tab) {
				val text = tab.text.toString()
				adapter.submitList(viewModel.filterByTabText(text).toMutableList().map { it.toListItem() }.sortedBy { it.bestBefore })
			}

		})
		binding.buttonAdd.setOnClickListener {
			viewModel.goToAddFoodFragment()
		}
		updateAdapterList(adapter)
	}

	private fun updateAdapterList(adapter: FoodListAdapter) {
		CoroutineScope(Dispatchers.IO).launch {
			viewModel.getFoodListFlow()
		}
		viewModel.foodListFlow.onEach { list ->
			adapter.submitList(list.map { it.toListItem() }.sortedBy { it.bestBefore })
			animateIfEmpty()
		}.launchIn(lifecycleScope)

		viewModel.placementFlow.onEach { list ->
			binding.tabLayout.removeAllTabs()
			val firstTab = binding.tabLayout.newTab()
			firstTab.text = FoodListViewModel.ALL_FOOD_TEXT
			binding.tabLayout.addTab(firstTab, 0, false)
			list.forEach {
				addTabToTabLayout(binding.tabLayout, it.placementName)
			}
		}.launchIn(lifecycleScope)
	}

	private fun addTabToTabLayout(tabLayout: TabLayout, tabTitle: String) {
		val tab = tabLayout.newTab()
		tab.apply {
			tab.text = tabTitle
		}
		tabLayout.addTab(tab)
	}

	private fun hideAndStopAnimation() {
		binding.responseImage.pauseAnimation()
		binding.responseLayout.visibility = View.GONE
		binding.SparkButtonAdd.pauseAnimation()
		binding.SparkButtonAdd.visibility = View.GONE
	}

	private fun animateIfEmpty() {
		if (viewModel.foodListFlow.value.isEmpty()) {
			binding.responseLayout.visibility = View.VISIBLE
			binding.responseImage.repeatCount = 0
			binding.responseImage.playAnimation()

			binding.SparkButtonAdd.visibility = View.VISIBLE
			binding.SparkButtonAdd.repeatMode = LottieDrawable.RESTART
			binding.SparkButtonAdd.repeatCount = LottieDrawable.INFINITE
			binding.SparkButtonAdd.playAnimation()
		} else {
			hideAndStopAnimation()
		}
	}

}