package com.chelz.foodorganizer.screens.recipes.ui.recycler

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chelz.foodorganizer.R
import com.chelz.foodorganizer.databinding.RecipeListItemBinding

class RecipesAdapter(
	private val recipeItemClick: (item: RecipeListItem) -> Unit,

	) : ListAdapter<RecipeListItem, RecipesAdapter.RecipeListHolder>(RecipeListDiffCallback) {

	inner class RecipeListHolder(
		private val binding: RecipeListItemBinding,
		private val itemClick: (id: RecipeListItem) -> Unit,
	) : RecyclerView.ViewHolder(binding.root) {

		@SuppressLint("SetTextI18n")
		fun bind(item: RecipeListItem) {
			Glide
				.with(binding.root.context)
				.load(item.image)
				.centerCrop()
				.into(binding.icon)
			binding.name.text = item.name
			binding.root.setOnClickListener {
				itemClick.invoke(item)
			}
			with(binding) {
				val diffList = listOf(diff1, diff2, diff3, diff4, diff5)
				for (i in 0 until item.difficulty) {
					diffList[i].setBackgroundResource(R.color.red)
				}
				for (i in item.difficulty until diffList.size) {
					diffList[i].setBackgroundResource(R.color.light_errorContainer)
				}
			}
			binding.timeToCook.text = item.timeToCook ?: ""
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeListHolder {
		val view = LayoutInflater.from(parent.context)
		val binding = RecipeListItemBinding.inflate(view, parent, false)
		return RecipeListHolder(binding, recipeItemClick)
	}

	override fun onBindViewHolder(holder: RecipeListHolder, position: Int) {
		holder.bind(getItem(position))
	}

	private object RecipeListDiffCallback : DiffUtil.ItemCallback<RecipeListItem>() {

		override fun areItemsTheSame(oldItem: RecipeListItem, newItem: RecipeListItem) = oldItem.id == newItem.id

		override fun areContentsTheSame(oldItem: RecipeListItem, newItem: RecipeListItem) = oldItem == newItem
	}
}