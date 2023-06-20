package com.chelz.foodorganizer.screens.recipes.ui.recycler

import android.annotation.SuppressLint
import com.chelz.foodorganizer.screens.recipes.data.dao.RecipeEntity
import java.util.Calendar
import java.util.Locale

data class RecipeListItem(
	val id: Int,
	val name: String,
	val typeId: Int,
	val difficulty: Int,
	val timeToCook: String?,
	val image: ByteArray,
) {

	companion object {

		@SuppressLint("SimpleDateFormat")
		fun fromRecipeEntity(recipeEntity: RecipeEntity): RecipeListItem {
			return RecipeListItem(
				id = recipeEntity.recipeId ?: 0,
				name = recipeEntity.name,
				image = recipeEntity.image,
				typeId = recipeEntity.typeId,
				difficulty = recipeEntity.difficulty,
				timeToCook = getTimeFromLong(recipeEntity.timeToCook),
			)
		}

		@SuppressLint("SimpleDateFormat")
		private fun getTimeFromLong(time: Long?): String? {
			return if (time != null) {
				val calendar = Calendar.getInstance().apply {
					timeInMillis = time
				}
				val formatter = android.icu.text.SimpleDateFormat("HH:mm", Locale.getDefault())
				formatter.format(calendar.time)
			} else
				null
		}
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as RecipeListItem

		if (id != other.id) return false
		if (name != other.name) return false
		if (typeId != other.typeId) return false
		if (difficulty != other.difficulty) return false
		if (timeToCook != other.timeToCook) return false
		if (!image.contentEquals(other.image)) return false

		return true
	}

	override fun hashCode(): Int {
		var result = id
		result = 31 * result + name.hashCode()
		result = 31 * result + difficulty.hashCode()
		result = 31 * result + typeId.hashCode()
		result = 31 * result + timeToCook.hashCode()
		result = 31 * result + image.contentHashCode()
		return result
	}

}
