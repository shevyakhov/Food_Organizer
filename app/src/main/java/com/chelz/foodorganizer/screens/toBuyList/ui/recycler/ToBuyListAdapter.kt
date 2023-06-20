package com.chelz.foodorganizer.screens.toBuyList.ui.recycler

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chelz.foodorganizer.R
import com.chelz.foodorganizer.databinding.ToBuyListItemBinding
import com.chelz.foodorganizer.screens.toBuyList.data.dao.Purchase
import com.chelz.foodorganizer.screens.toBuyList.presentation.ToBuyListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ToBuyListAdapter(
	private val viewModel: ToBuyListViewModel,
	private val itemClick: (id: String) -> Unit,

	) : ListAdapter<ToBuyListItem, ToBuyListAdapter.ToBuyListHolder>(ToBuyListDiffCallback) {

	var dates = arrayListOf<ToBuyListItem>()

	inner class ToBuyListHolder(
		private val binding: ToBuyListItemBinding,
		private val itemClick: (id: String) -> Unit,
	) : RecyclerView.ViewHolder(binding.root) {

		@SuppressLint("SetTextI18n")
		fun bind(viewModel: ToBuyListViewModel, item: ToBuyListItem) {

			val container = binding.contentLayout.apply {
				removeAllViews()
			}
			if (item.id !in dates.map { it.id }) {
				binding.textDate.visibility = View.GONE
			} else {
				binding.textDate.visibility = View.VISIBLE
			}
			binding.textTitle.text = item.name
			for (i in item.items) {
				val view = LayoutInflater.from(viewModel.app).inflate(R.layout.include_layout, container, false)
				val textView = view.findViewById<TextView>(R.id.includeText)
				val checkBox = view.findViewById<CheckBox>(R.id.includeCheck)

				textView.text = i.name
				checkBox.isChecked = i.isTicked

				checkBox.setOnCheckedChangeListener { _, b ->
					CoroutineScope(Dispatchers.IO).launch {
						viewModel.notifyItemChecked(item.id, i.name, b)
					}
					if (b) {
						itemClick.invoke(i.name)
					}
				}
				container.addView(view)
			}
			val date = Date(item.date ?: 0)
			val calendar = Calendar.getInstance().apply {
				time = date
			}
			val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
			val month = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())
			binding.textDate.text = "$dayOfMonth/$month"

		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToBuyListHolder {
		val view = LayoutInflater.from(parent.context)
		val binding = ToBuyListItemBinding.inflate(view, parent, false)
		return ToBuyListHolder(binding, itemClick)
	}

	override fun onBindViewHolder(holder: ToBuyListHolder, position: Int) {
		holder.bind(viewModel, getItem(position))
	}

	fun updateList(list: MutableList<ToBuyListItem>) {
		dates = list.distinctBy { it.date } as ArrayList<ToBuyListItem>
		submitList(list)
	}

	fun getItemIdAt(position: Int): Int {
		notifyOnDate(position)
		return currentList[position].id
	}

	private fun notifyOnDate(position: Int) {
		if (currentList[position] in dates) {
			val date = currentList[position].date
			val list = currentList.filter { it.date == date }
			currentList.onEachIndexed { index, toBuyListItem ->
				if (toBuyListItem in list)
					notifyItemChanged(index)
			}
		}
	}

	private object ToBuyListDiffCallback : DiffUtil.ItemCallback<ToBuyListItem>() {

		override fun areItemsTheSame(oldItem: ToBuyListItem, newItem: ToBuyListItem) = oldItem.id == newItem.id

		override fun areContentsTheSame(oldItem: ToBuyListItem, newItem: ToBuyListItem) = false
	}
}