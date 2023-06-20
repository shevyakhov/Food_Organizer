package com.chelz.foodorganizer.screens.foodList.ui.recycler

import java.io.Serializable
import java.util.Date

data class FoodListItem(
	val id: Int,
	val name: String,
	val placement: Int,
	val itemCount: String,
	val bestBefore: Long?,
	val imageRes: ByteArray,
) : Serializable {

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as FoodListItem

		if (id != other.id) return false
		if (name != other.name) return false
		if (placement != other.placement) return false
		if (itemCount != other.itemCount) return false
		if (bestBefore != other.bestBefore) return false
		if (!imageRes.contentEquals(other.imageRes)) return false

		return true
	}

	override fun hashCode(): Int {
		var result = id
		result = 31 * result + name.hashCode()
		result = 31 * result + placement.hashCode()
		result = 31 * result + itemCount.hashCode()
		result = 31 * result + bestBefore.hashCode()
		result = 31 * result + imageRes.contentHashCode()
		return result
	}
}

