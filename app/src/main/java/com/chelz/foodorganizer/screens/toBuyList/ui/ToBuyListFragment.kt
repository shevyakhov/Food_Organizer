package com.chelz.foodorganizer.screens.toBuyList.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.airbnb.lottie.LottieDrawable
import com.chelz.foodorganizer.R
import com.chelz.foodorganizer.databinding.FragmentToBuyListBinding
import com.chelz.foodorganizer.screens.toBuyList.presentation.ToBuyListViewModel
import com.chelz.foodorganizer.screens.toBuyList.ui.recycler.SwipeCallback
import com.chelz.foodorganizer.screens.toBuyList.ui.recycler.ToBuyListAdapter
import com.chelz.foodorganizer.screens.toBuyList.ui.recycler.toToBuyListItem
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ToBuyListFragment : Fragment() {

	companion object {

		fun newInstance() = ToBuyListFragment()
	}

	private var _binding: FragmentToBuyListBinding? = null
	private val binding get() = _binding!!

	private val viewModel by viewModel<ToBuyListViewModel>()
	override fun onCreateView(
		inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?,
	): View {
		_binding = FragmentToBuyListBinding.inflate(inflater, container, false)
		return binding.root
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		val adapter = ToBuyListAdapter(viewModel) {
			showAlertDialog(it)
		}

		binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
		(binding.recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
		binding.recyclerView.adapter = adapter

		val swipeHandler =
			object : SwipeCallback(
				requireContext(),
				deleteCallback = { position ->
					val itemId = adapter.getItemIdAt(position)
					val item = viewModel.getItemById(itemId)
					CoroutineScope(Dispatchers.IO).launch {
						viewModel.removeItemFromBuyList(itemId)
					}.invokeOnCompletion {
						Snackbar
							.make(binding.recyclerView, getString(R.string.deleted), Snackbar.LENGTH_LONG)
							.setAction(getString(R.string.undo)) { viewModel.addItem(position, item) }
							.show()
					}
				}
			) {

			}
		val itemTouchHelper = ItemTouchHelper(swipeHandler)
		itemTouchHelper.attachToRecyclerView(binding.recyclerView)

		binding.buttonAdd.setOnClickListener {
			viewModel.navigateToAddToBuyList()
		}

		viewModel.toBuyListFlow.onEach {
			adapter.updateList(viewModel.toBuyListFlow.value.map { it.toToBuyListItem() }.sortedBy { it.date }.toMutableList())
			animateIfEmpty()
		}.launchIn(lifecycleScope)

		updateAdapterList()
	}

	private fun showAlertDialog(it: String) {
		if (viewModel.addDialogFlow.value) {
			val d = CustomAlertDialog(requireContext(), lifecycleScope, it, viewModel)
			d.show()
		}
	}

	private fun updateAdapterList() {
		CoroutineScope(Dispatchers.IO).launch {
			viewModel.getToBuyList()
		}
	}

	private fun hideAndStopAnimation() {
		binding.responseImage.pauseAnimation()
		binding.responseLayout.visibility = View.GONE
		binding.SparkButtonAdd.pauseAnimation()
		binding.SparkButtonAdd.visibility = View.GONE
	}

	private fun animateIfEmpty() {
		if (viewModel.toBuyListFlow.value.isEmpty()) {
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