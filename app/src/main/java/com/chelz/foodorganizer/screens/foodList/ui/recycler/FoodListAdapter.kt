package com.chelz.foodorganizer.screens.foodList.ui.recycler

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chelz.foodorganizer.databinding.FoodListItemBinding
import com.chelz.foodorganizer.screens.foodList.presentation.FoodListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Duration
import java.util.Calendar

class FoodListAdapter(
	private val viewModel: FoodListViewModel,
	private val foodItemClick: (id: FoodListItem) -> Unit,
) : ListAdapter<FoodListItem, FoodListAdapter.FoodListHolder>(FoodListDiffCallback) {

	inner class FoodListHolder(
		private val binding: FoodListItemBinding,
		private val itemClick: (id: FoodListItem) -> Unit,
	) : RecyclerView.ViewHolder(binding.root) {

		@SuppressLint("SetTextI18n")
		fun bind(viewModel: FoodListViewModel, item: FoodListItem) {
			Glide
				.with(binding.root.context)
				.load(item.imageRes)
				.centerCrop()
				.into(binding.icon)
			binding.name.text = item.name
			CoroutineScope(Dispatchers.Main).launch {
				binding.foodPlacement.text = viewModel.getPlacement(item.placement).placementName
				binding.remainingCount.text = "${item.itemCount} кг"
			}
			val date = item.bestBefore
			if (date != null) {
				Calendar.getInstance().apply { time.time = date }
				val now = System.currentTimeMillis()
				val diff = Duration.ofMillis(date - now).toDays()
				binding.remainingTime.text = when {
					diff > 0   -> {
						"Осталось $diff дней"
					}

					diff == 0L -> {
						"Завтра просрочится!"
					}

					else       -> {
						"Просрочен"
					}
				}
				binding.progressBar.max = 100
				binding.progressBar.progress = (100 - diff * 10).toInt()

				binding.progressBar.setIndicatorColor((if (binding.progressBar.progress > 70) {
					Color.parseColor("#BA1A1A")
				} else {
					Color.parseColor("#415AA9")
				}))
				binding.progressBar.trackColor = (if (binding.progressBar.progress > 70) {
					Color.parseColor("#1ABA1A1A")
				} else {
					Color.parseColor("#1A415AA9")
				})
			}
			binding.root.setOnClickListener {
				itemClick.invoke(item)
			}
		}
	}

	fun updateData(newData: FoodListItem) {
		val newList = currentList.toMutableList()
		newList.add(newData)
		submitList(newList)
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodListHolder {
		val view = LayoutInflater.from(parent.context)
		val binding = FoodListItemBinding.inflate(view, parent, false)
		return FoodListHolder(binding, foodItemClick)
	}

	override fun onBindViewHolder(holder: FoodListHolder, position: Int) {
		holder.bind(viewModel, getItem(position))
	}

	private object FoodListDiffCallback : DiffUtil.ItemCallback<FoodListItem>() {

		override fun areItemsTheSame(oldItem: FoodListItem, newItem: FoodListItem) = oldItem.id == newItem.id

		override fun areContentsTheSame(oldItem: FoodListItem, newItem: FoodListItem) = oldItem == newItem
	}
}