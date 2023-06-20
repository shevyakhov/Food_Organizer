package com.chelz.foodorganizer.screens.recipes.data.dao

import androidx.room.TypeConverter

class FoodListConverter {

	private val separator = ";"

	@TypeConverter
	fun fromString(value: String?): List<RecipeProduct> {
		val products = mutableListOf<RecipeProduct>()
		if (!value.isNullOrEmpty()) {
			val items = value.split(separator).toTypedArray()
			for (item in items) {
				val product = item.split(",").toTypedArray()
				if (product.size == 2) {
					val name = product[0]
					val quantity = product[1].toFloatOrNull() ?: 0f
					products.add(RecipeProduct(name, quantity))
				}
			}
		}
		return products
	}

	@TypeConverter
	fun toString(products: List<RecipeProduct>?): String? {
		return products?.joinToString(separator) {
			"${it.name},${it.quantity}"
		}
	}
}
