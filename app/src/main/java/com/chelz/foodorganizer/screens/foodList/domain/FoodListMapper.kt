package com.chelz.foodorganizer.screens.foodList.domain

import com.chelz.foodorganizer.screens.foodList.data.dao.FoodEntity
import com.chelz.foodorganizer.screens.foodList.ui.recycler.FoodListItem

fun FoodListItem.toEntity() = FoodEntity(
	foodId = id,
	name = name,
	placement = placement,
	image = imageRes,
	itemCount = itemCount,
	bestBefore = bestBefore,
)

fun FoodEntity.toListItem() = FoodListItem(
	id = foodId ?: 0,
	name = name,
	placement = placement,
	imageRes = image,
	itemCount = itemCount,
	bestBefore = bestBefore,
)
