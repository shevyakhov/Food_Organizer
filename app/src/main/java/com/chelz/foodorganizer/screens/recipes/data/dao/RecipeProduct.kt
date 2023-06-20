package com.chelz.foodorganizer.screens.recipes.data.dao

data class RecipeProduct(var name: String, var quantity: Float) {

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false
		other as RecipeProduct
		return name == other.name && quantity == other.quantity
	}

	override fun hashCode(): Int {
		var result = name.hashCode()
		result = 31 * result + quantity.hashCode()
		return result
	}
}