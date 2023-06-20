package com.chelz.foodorganizer.screens.toBuyList.ui.recycler

import com.chelz.foodorganizer.screens.toBuyList.data.dao.Purchase
import com.chelz.foodorganizer.screens.toBuyList.data.dao.ToBuyListEntity

data class ToBuyListItem(
	val id: Int,
	val name: String,
	val date: Long?,
	val items: List<Purchase>,
)

fun ToBuyListEntity.toToBuyListItem(): ToBuyListItem {
	return ToBuyListItem(
		id = this.id,
		name = this.name,
		date = this.date,
		items = this.items
	)
}