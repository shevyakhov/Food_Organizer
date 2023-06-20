package com.chelz.foodorganizer.screens.recipes.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.chelz.foodorganizer.screens.foodList.data.dao.FoodRecipeJunctionEntity

@Dao
interface FoodRecipeJunctionDao {

	@Insert
	suspend fun insert(foodRecipeJoin: FoodRecipeJunctionEntity)

	@Delete
	suspend fun delete(foodRecipeJoin: FoodRecipeJunctionEntity)

	@Update
	suspend fun update(foodRecipeJoin: FoodRecipeJunctionEntity)

	@Query("DELETE FROM FoodRecipeJunctionTable WHERE foodId = :foodId")
	suspend fun deleteByFoodId(foodId: Int)

	@Query("DELETE FROM FoodRecipeJunctionTable WHERE recipeId = :recipeId")
	suspend fun deleteByRecipeId(recipeId: Int)
}




