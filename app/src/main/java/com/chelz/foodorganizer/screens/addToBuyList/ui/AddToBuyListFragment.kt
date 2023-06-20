package com.chelz.foodorganizer.screens.addToBuyList.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.chelz.foodorganizer.databinding.FragmentAddToBuyListBinding
import com.chelz.foodorganizer.screens.addToBuyList.presentation.AddToBuyListViewModel
import com.chelz.foodorganizer.screens.addToBuyList.ui.recycler.AddToBuyListAdapter
import com.chelz.foodorganizer.utils.flowBinders.bindFlow
import com.chelz.foodorganizer.utils.textListeners.bindDateListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddToBuyListFragment : Fragment() {
	companion object {

		fun newInstance() = AddToBuyListFragment()
	}

	private var _binding: FragmentAddToBuyListBinding? = null
	private val binding get() = _binding!!
	private val viewModel by viewModel<AddToBuyListViewModel>()

	private lateinit var adapter: AddToBuyListAdapter

	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentAddToBuyListBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		adapter = AddToBuyListAdapter()
		binding.listName.bindFlow(lifecycleScope, viewModel.nameFlow)

		binding.datePicker.apply {
			addTextChangedListener(bindDateListener(this))
		}
		setAdapterBinding()
	}

	private fun setAdapterBinding() {

		binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
		binding.recyclerView.adapter = adapter
		adapter.submitList(listOf())

		binding.addIngredients.setOnClickListener {
			adapter.onAddItemClick()
		}
		binding.saveListButton.setOnClickListener {
			CoroutineScope(Dispatchers.IO).launch {
				viewModel.productsFlow.value = getProducts(adapter)
				viewModel.dateFlow.value = binding.datePicker.text.toString()
				viewModel.addList()
			}
		}
	}

	private fun getProducts(adapter: AddToBuyListAdapter): List<String> =
		adapter.currentList.map { it.name }

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}