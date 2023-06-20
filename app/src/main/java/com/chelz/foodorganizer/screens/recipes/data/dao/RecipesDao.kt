package com.chelz.foodorganizer.screens.recipes.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.chelz.foodorganizer.screens.foodList.data.dao.FoodRecipeJunctionEntity
import com.chelz.foodorganizer.screens.foodList.data.dao.RecipeWithFood

@Dao
interface RecipesDao {

	@Query("SELECT * FROM RecipesTable")
	fun getAllRecipes(): List<RecipeEntity>

	@Query("SELECT * FROM RecipesTable WHERE recipeId = :id")
	fun getRecipeById(id: Int): RecipeEntity?

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertRecipe(recipe: RecipeEntity)

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertRecipes(recipes: List<RecipeEntity>)

	@Update
	fun updateRecipe(recipe: RecipeEntity)

	@Delete
	fun deleteRecipe(recipe: RecipeEntity)

	@Query("DELETE FROM RecipesTable")
	fun deleteAllRecipes()

	@Query("SELECT * FROM RecipeTypeTable")
	fun getAllRecipeTypes(): List<RecipeTypeEntity>

	@Query("SELECT * FROM RecipeTypeTable WHERE id = :id")
	fun getRecipeTypeById(id: Int): RecipeTypeEntity?

	@Insert
	fun insertRecipeType(recipeType: RecipeTypeEntity): Long

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insertRecipeTypes(recipeType: List<RecipeTypeEntity>)

	@Update
	fun updateRecipeType(recipeType: RecipeTypeEntity)

	@Delete
	fun deleteRecipeType(recipeType: RecipeTypeEntity)

	@Query("DELETE FROM RecipeTypeTable")
	fun deleteAllRecipeTypes()

	@Transaction
	@Query("SELECT * FROM RecipesTable WHERE recipeId = :id")
	fun getRecipeWithFood(id: Int): List<RecipeWithFood>

	@Insert(onConflict = OnConflictStrategy.IGNORE)
	fun addFoodWithRecipe(recipeId: FoodRecipeJunctionEntity)

	@Query("DELETE FROM FoodRecipeJunctionTable WHERE recipeId = :recipeId AND foodId = :foodId")
	fun deleteFoodRecipeJunctionEntity(recipeId: Int, foodId: Int)

	@Transaction
	@Query("SELECT * FROM FoodRecipeJunctionTable WHERE recipeId = :id")
	fun getFoodRecipeJunctionEntity(id: Int): List<FoodRecipeJunctionEntity>
}