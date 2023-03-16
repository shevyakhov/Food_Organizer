package com.chelz.foodorganizer.screens.foodList.ui.recycler

import java.io.Serializable
import java.util.Date

data class FoodListItem(
	val id: Int,
	val name: String,
	val placement: String,
	val itemCount: ItemCount,
	val bestBefore: Date,
	val imageRes: Int,
) : Serializable

data class ItemCount(val itemCount: String, val prefix: String): Serializable
