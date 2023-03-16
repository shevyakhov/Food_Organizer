package com.chelz.foodorganizer.screens.foodList.ui.recycler

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chelz.foodorganizer.databinding.FoodListItemBinding
import com.chelz.foodorganizer.screens.foodList.presentation.FoodListViewModel
import java.sql.Timestamp
import java.util.Calendar

class FoodListAdapter(
	private val viewModel: FoodListViewModel,
	private val lifecycleOwner: LifecycleOwner,
	private val foodItemClick: (id: FoodListItem) -> Unit,

	) : ListAdapter<FoodListItem, FoodListAdapter.FoodListHolder>(FoodListDiffCallback) {

	inner class FoodListHolder(
		private val binding: FoodListItemBinding,
		private val itemClick: (id: FoodListItem) -> Unit,
	) : RecyclerView.ViewHolder(binding.root) {

		@SuppressLint("SetTextI18n")
		fun bind(viewModel: FoodListViewModel, viewLifecycleOwner: LifecycleCoroutineScope, item: FoodListItem) {
			when (item.id) {
				1 -> {
					Glide
						.with(viewModel.app)
						.load("https://ferma-m2.ru/images/news/news_list_big/crop_moloko.jpg")
						.centerCrop()
						.into(binding.icon)
				}

				2 -> {
					Glide
						.with(viewModel.app)
						.load("https://bonduelle.ru/upload/iblock/4f0/4f0d9d57952b8d2ef430a59d2a580547.jpg")
						.centerCrop()
						.into(binding.icon)
				}

				3 -> {
					Glide
						.with(viewModel.app)
						.load("https://moreodor.ru/wp-content/uploads/2020/07/calmar-loligo-800.png")
						.centerCrop()
						.into(binding.icon)
				}

				4 -> {
					Glide
						.with(viewModel.app)
						.load("https://ir.ozone.ru/s3/club-storage/images/article_image_1632x1000/313/8b2c09bc-abdc-4889-b4ff-bd8225dcd1ec.jpeg")
						.centerCrop()
						.into(binding.icon)
				}

				5 -> {
					Glide
						.with(viewModel.app)
						.load("https://upload.wikimedia.org/wikipedia/commons/5/5a/Oranges_and_orange_juice.jpg")
						.centerCrop()
						.into(binding.icon)
				}
			}


			binding.name.text = item.name
			binding.foodPlacement.text = item.placement
			binding.progressBar.max = 100
			binding.progressBar.progress = kotlin.random.Random.nextInt(0, 100)
			binding.progressBar.setIndicatorColor((if (binding.progressBar.progress > 70) Color.RED else Color.BLUE))
			val date = item.bestBefore
			Timestamp(item.bestBefore.time)
			val cal = Calendar.getInstance().apply {
				time = date
			}
			binding.remainingCount.text = "${item.itemCount.itemCount} ${item.itemCount.prefix}"
			binding.remainingTime.text = "до " + cal.get(Calendar.DAY_OF_MONTH).toString() + "/" + cal.get(Calendar.MONTH)

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
		holder.bind(viewModel, lifecycleOwner.lifecycleScope, getItem(position))
	}

	private object FoodListDiffCallback : DiffUtil.ItemCallback<FoodListItem>() {

		override fun areItemsTheSame(oldItem: FoodListItem, newItem: FoodListItem) = oldItem.id == newItem.id

		override fun areContentsTheSame(oldItem: FoodListItem, newItem: FoodListItem) = oldItem == newItem
	}
}