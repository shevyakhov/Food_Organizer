package com.chelz.foodorganizer.screens.recipes.data.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "RecipesTable", foreignKeys = [ForeignKey(entity = RecipeTypeEntity::class,
															  parentColumns = ["id"],
															  childColumns = ["typeId"],
															  onDelete = ForeignKey.CASCADE)])
@TypeConverters(FoodListConverter::class)
data class RecipeEntity(
	@PrimaryKey(autoGenerate = true) val recipeId: Int? = null,
	@ColumnInfo(name = "name") val name: String,
	@ColumnInfo(name = "typeId") val typeId: Int,
	@ColumnInfo(name = "difficulty") val difficulty: Int,
	@ColumnInfo(name = "timeToCook") val timeToCook: Long?,
	@ColumnInfo(name = "foodList") val products: List<RecipeProduct>,
	@ColumnInfo(typeAffinity = ColumnInfo.BLOB) val image: ByteArray,
) {

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as RecipeEntity

		if (recipeId != other.recipeId) return false
		if (name != other.name) return false
		if (typeId != other.typeId) return false
		if (difficulty != other.difficulty) return false
		if (timeToCook != other.timeToCook) return false
		if (products != other.products) return false
		if (!image.contentEquals(other.image)) return false

		return true
	}

	override fun hashCode(): Int {
		var result = recipeId ?: 0
		result = 31 * result + name.hashCode()
		result = 31 * result + typeId.hashCode()
		result = 31 * result + difficulty.hashCode()
		result = 31 * result + (timeToCook?.hashCode() ?: 0)
		result = 31 * result + products.hashCode()
		result = 31 * result + image.contentHashCode()
		return result
	}


}



