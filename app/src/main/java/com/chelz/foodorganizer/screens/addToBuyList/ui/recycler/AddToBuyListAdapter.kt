package com.chelz.foodorganizer.screens.addToBuyList.ui.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chelz.foodorganizer.databinding.AddToBuyItemBinding

class AddToBuyListAdapter : ListAdapter<AddToBuyItem, RecyclerView.ViewHolder>(FoodListDiffCallback) {

	inner class RecipeListHolder(
		private val binding: AddToBuyItemBinding,
	) : RecyclerView.ViewHolder(binding.root) {

		fun bind(item: AddToBuyItem, position: Int) {
			binding.nameTextview.setText(item.name)
			binding.nameTextview.doOnTextChanged { text, _, _, _ ->
				item.name = text.toString()
			}
			binding.removeFoodButton.setOnClickListener {
				removeItem(item)
			}
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		val binding = AddToBuyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		return RecipeListHolder(binding)
	}

	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		(holder as RecipeListHolder).bind(getItem(position), position)
	}

	fun onAddItemClick() {
		val newItem = AddToBuyItem()
		val newList = currentList.toMutableList()
		newList.add(0, newItem)
		submitList(newList)
	}

	private fun removeItem(item: AddToBuyItem) {
		val newList = currentList.toMutableList()
		val index = newList.indexOfFirst { it.name == item.name }
		if (index != -1) {
			newList.removeAt(newList.indexOf(item))
			submitList(newList)
		}
	}

	private object FoodListDiffCallback : DiffUtil.ItemCallback<AddToBuyItem>() {

		override fun areItemsTheSame(oldItem: AddToBuyItem, newItem: AddToBuyItem) = oldItem.name == newItem.name

		override fun areContentsTheSame(oldItem: AddToBuyItem, newItem: AddToBuyItem) = oldItem == newItem
	}
}