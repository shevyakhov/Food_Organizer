package com.chelz.foodorganizer.screens.foodList.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.chelz.foodorganizer.R
import com.chelz.foodorganizer.databinding.FragmentFoodListBinding
import com.chelz.foodorganizer.screens.foodList.presentation.FoodListViewModel
import com.chelz.foodorganizer.screens.foodList.ui.recycler.FoodListAdapter
import com.chelz.foodorganizer.screens.foodList.ui.recycler.FoodListItem
import com.chelz.foodorganizer.screens.foodList.ui.recycler.ItemCount
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Calendar

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
		val adapter = FoodListAdapter(viewModel, viewLifecycleOwner) {
			viewModel.goToFoodDetailsFragment(it.id)
		}

		binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
		binding.recyclerView.adapter = adapter
		val cal = Calendar.getInstance()
		adapter.submitList(listOf(
			FoodListItem(1, "Молоко", "Холодильник", ItemCount("1", "л"), cal.time, R.drawable.recipes_ic),
			FoodListItem(2, "Яблоки", "Кладовая", ItemCount("3", "шт"), cal.time, R.drawable.recipes_ic),
			FoodListItem(3, "Кальмар", "Холодильник", ItemCount("1", "кг"), cal.time, R.drawable.recipes_ic),
			FoodListItem(4, "Хлеб", "Кладовая", ItemCount("1", "шт"), cal.time, R.drawable.recipes_ic),
			FoodListItem(5, "Сок", "Холодильник", ItemCount("2", "л"), cal.time, R.drawable.recipes_ic),
		))

		binding.buttonAdd.setOnClickListener {
			val builder = AlertDialog.Builder(requireContext())
			builder.setTitle(getString(R.string.addProduct))
			builder.setMessage(getString(R.string.chooseWayToAdd))

			builder.setPositiveButton(R.string.addManually) { dialog, which ->
				viewModel.goToAddFoodFragment()
			}
			builder.setNegativeButton(R.string.addWithScanner) { dialog, which ->
				Toast.makeText(requireContext(), "todo", Toast.LENGTH_SHORT).show()
			}

			builder.setNeutralButton(R.string.cancel) { dialog, which -> }
			builder.show()
		}

	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}