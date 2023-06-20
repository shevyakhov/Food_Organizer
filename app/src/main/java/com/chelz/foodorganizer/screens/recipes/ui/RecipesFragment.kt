package com.chelz.foodorganizer.screens.recipes.ui

import android.annotation.SuppressLint
import android.content.res.ColorStateList
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
import com.chelz.foodorganizer.databinding.FragmentRecipesBinding
import com.chelz.foodorganizer.screens.recipes.presentation.RecipesViewModel
import com.chelz.foodorganizer.screens.recipes.ui.recycler.RecipeListItem
import com.chelz.foodorganizer.screens.recipes.ui.recycler.RecipesAdapter
import com.chelz.foodorganizer.utils.chip.createChip
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecipesFragment : Fragment() {

	companion object {

		fun newInstance() = RecipesFragment()
	}

	private var _binding: FragmentRecipesBinding? = null
	private val binding get() = _binding!!

	private val viewModel by viewModel<RecipesViewModel>()
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentRecipesBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		val adapter = RecipesAdapter {
			viewModel.goToRecipeDetailsFragment(it.id)
		}

		binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
		binding.recyclerView.adapter = adapter
		binding.topAppBar.setOnMenuItemClickListener { menuItem ->
			when (menuItem.itemId) {
				R.id.filter -> {
					val builder = MaterialAlertDialogBuilder(requireContext())
					builder.setTitle("Фильтрация")
					builder.setMessage("Как отфильтровать список?")

					builder.setPositiveButton("По времени приготовления") { _, _ ->
						adapter.submitList(adapter.currentList.sortedBy { it.timeToCook })
					}
					builder.setNegativeButton("По названию") { _, _ ->
						adapter.submitList(adapter.currentList.sortedBy { it.name })
					}


					builder.setNeutralButton(R.string.cancel) { _, _ -> }
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
					val newList = viewModel.recipesFlow.value.filter { it.name.contains(p0) }.map { RecipeListItem.fromRecipeEntity(it) }
					adapter.submitList(newList)
				} else {
					adapter.submitList(viewModel.recipesFlow.value.map { RecipeListItem.fromRecipeEntity(it) })
				}
				return false
			}

		})
		binding.buttonAdd.setOnClickListener {
			viewModel.goToAddRecipeFragment()
		}
		updateAdapterList(adapter)
	}

	private fun updateAdapterList(adapter: RecipesAdapter) {
		val job = CoroutineScope(Dispatchers.IO).async {
			viewModel.getAllRecipes()
			viewModel.getAllRecipeTypes()
		}
		CoroutineScope(Dispatchers.Main).launch {
			job.await()
			adapter.submitList(viewModel.recipesFlow.value.map { RecipeListItem.fromRecipeEntity(it) })
			addFilterChips(adapter)
			animateIfEmpty()
		}
	}

	@SuppressLint("UseCompatLoadingForDrawables")
	private fun addFilterChips(adapter: RecipesAdapter) {
		viewModel.recipeTypesFlow.onEach { types ->
			binding.chipGroupRecipesType.removeAllViews()
			for (type in types) {
				val chip = createChip(requireContext(), type.typeName).apply {
					isCheckable = true
					chipBackgroundColor = ColorStateList.valueOf(requireContext().getColor(R.color.light_secondaryContainer))
					setOnCheckedChangeListener { _, isChecked ->
						if (isChecked) {
							val filteredList = viewModel.recipesFlow.value.filter {
								it.typeId == type.id
							}
							adapter.submitList(filteredList.map { RecipeListItem.fromRecipeEntity(it) })
						} else
							checkIfNoneChecked(adapter)
					}
				}
				binding.chipGroupRecipesType.addView(chip)
			}
		}.launchIn(lifecycleScope)
	}

	private fun checkIfNoneChecked(adapter: RecipesAdapter) {
		val count = binding.chipGroupRecipesType.childCount
		var isChecked = false
		for (i in 0 until count) {
			val child = binding.chipGroupRecipesType.getChildAt(i) as Chip
			if (child.isChecked) {
				isChecked = true
			}
		}
		if (!isChecked) {
			adapter.submitList(viewModel.recipesFlow.value.map { RecipeListItem.fromRecipeEntity(it) })
		}
	}

	private fun hideAndStopAnimation() {
		binding.responseImage.pauseAnimation()
		binding.responseLayout.visibility = View.GONE
		binding.SparkButtonAdd.pauseAnimation()
		binding.SparkButtonAdd.visibility = View.GONE
	}

	private fun animateIfEmpty() {
		if (viewModel.recipesFlow.value.isEmpty()) {
			binding.responseLayout.visibility = View.VISIBLE
			binding.responseImage.repeatCount = 0
			binding.responseImage.playAnimation()
			binding.chipGroupRecipesType.visibility = View.GONE

			binding.SparkButtonAdd.visibility = View.VISIBLE
			binding.SparkButtonAdd.repeatMode = LottieDrawable.RESTART
			binding.SparkButtonAdd.repeatCount = LottieDrawable.INFINITE
			binding.SparkButtonAdd.playAnimation()
		} else {
			binding.chipGroupRecipesType.visibility = View.VISIBLE
			hideAndStopAnimation()
		}
	}

}