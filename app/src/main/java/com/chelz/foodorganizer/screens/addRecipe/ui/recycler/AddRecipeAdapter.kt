package com.chelz.foodorganizer.screens.addRecipe.ui.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.chelz.foodorganizer.databinding.AddRecipeItemBinding

class AddRecipeAdapter : ListAdapter<AddRecipeItem, ViewHolder>(FoodListDiffCallback) {

	inner class RecipeListHolder(
		private val binding: AddRecipeItemBinding,
	) : ViewHolder(binding.root) {

		fun bind(item: AddRecipeItem) {
			binding.foodNameTextview.setText(item.name)
			binding.foodNameTextview.doOnTextChanged { text, _, _, _ ->
				currentList[adapterPosition].name = text.toString()
			}
			binding.foodQuantityTextview.setText(item.quantity.toString())
			binding.foodQuantityTextview.doOnTextChanged { text, _, _, _ ->
				if (text?.isNotEmpty() == true) {
					currentList[adapterPosition].quantity = text.toString().toFloat()
				} else {
					currentList[adapterPosition].quantity = text.toString().toFloat()
				}
			}
			binding.removeFoodButton.setOnClickListener {
				removeItem(adapterPosition)
			}
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val binding = AddRecipeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
		return RecipeListHolder(binding)
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		(holder as RecipeListHolder).bind(getItem(position))
	}

	fun onAddItemClick() {
		val newItem = AddRecipeItem()
		val newList = currentList.toMutableList()
		newList.add(0, newItem)
		submitList(newList)
	}

	private fun removeItem(adapterPosition: Int) {
		val newList = currentList.toMutableList().apply {
			removeAt(adapterPosition)
		}
		submitList(newList)
	}

	private object FoodListDiffCallback : DiffUtil.ItemCallback<AddRecipeItem>() {

		override fun areItemsTheSame(oldItem: AddRecipeItem, newItem: AddRecipeItem) = oldItem.name == newItem.name

		override fun areContentsTheSame(oldItem: AddRecipeItem, newItem: AddRecipeItem) = oldItem == newItem
	}
}