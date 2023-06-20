package com.chelz.foodorganizer.screens.foodList.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.chelz.foodorganizer.screens.foodList.data.dao.FoodDao
import com.chelz.foodorganizer.screens.foodList.data.dao.FoodEntity
import com.chelz.foodorganizer.screens.foodList.data.dao.FoodRecipeJunctionEntity
import com.chelz.foodorganizer.screens.foodList.data.dao.PlacementEntity
import com.chelz.foodorganizer.screens.foodList.data.dao.StatisticsDao
import com.chelz.foodorganizer.screens.foodList.data.dao.StatisticsEntity
import com.chelz.foodorganizer.screens.recipes.data.dao.FoodRecipeJunctionDao
import com.chelz.foodorganizer.screens.recipes.data.dao.RecipeEntity
import com.chelz.foodorganizer.screens.recipes.data.dao.RecipeTypeEntity
import com.chelz.foodorganizer.screens.recipes.data.dao.RecipesDao
import com.chelz.foodorganizer.screens.toBuyList.data.dao.ToBuyListDao
import com.chelz.foodorganizer.screens.toBuyList.data.dao.ToBuyListEntity

@Database(entities = [
	FoodEntity::class,
	PlacementEntity::class,
	RecipeEntity::class,
	RecipeTypeEntity::class,
	ToBuyListEntity::class,
	StatisticsEntity::class,
	FoodRecipeJunctionEntity::class],
		  version = 1,
		  exportSchema = false)
abstract class FoodDataBase : RoomDatabase() {

	companion object {

		const val FoodDataBaseName = "FOOD_DB"
	}

	abstract fun foodDao(): FoodDao
	abstract fun recipesDao(): RecipesDao
	abstract fun toBuyListDao(): ToBuyListDao
	abstract fun statisticsDao(): StatisticsDao
	abstract fun foodRecipeJunctionDao(): FoodRecipeJunctionDao
}