package com.chelz.foodorganizer.screens.foodList.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {

	@Transaction
	@Query("SELECT * FROM FoodTable")
	fun getFoodWithRecipe(): List<FoodWithRecipe>

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertFoods(data: List<FoodEntity>)

	@Transaction
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insertFood(data: FoodEntity): Long

	@Insert
	suspend fun insertPlacement(data: PlacementEntity)

	@Update
	suspend fun updateFood(data: FoodEntity)

	@Update
	suspend fun updatePlacement(data: PlacementEntity)

	@Delete
	suspend fun deleteFood(data: FoodEntity)

	@Delete
	suspend fun deletePlacement(data: PlacementEntity)

	@Query("SELECT * FROM FoodTable")
	suspend fun getFood(): List<FoodEntity>

	@Transaction
	@Query("SELECT * FROM PlacementTable")
	suspend fun getPlacementWithFood(): List<PlacementWithFood>

	@Transaction
	@Query("SELECT * FROM PlacementTable WHERE placementId = :placement")
	suspend fun getPlacementWithFood(placement: Int): List<PlacementWithFood>

	@Query("SELECT * FROM FoodTable WHERE foodId = :id")
	suspend fun getFoodById(id: Int): FoodEntity

	@Query("SELECT * FROM FoodTable WHERE placement = :placementId")
	suspend fun getFoodByPlacement(placementId: Int): List<FoodEntity>

	@Query("SELECT * FROM PlacementTable WHERE placementName = :name")
	suspend fun getPlacement(name: String): PlacementEntity

	@Query("SELECT * FROM PlacementTable")
	suspend fun getPlacements(): List<PlacementEntity>

	@Query("SELECT * FROM FoodTable")
	fun getFoodFlow(): Flow<List<FoodEntity>>

	@Query("DELETE FROM FoodTable")
	suspend fun deleteAllFood()

	@Query("SELECT * FROM PlacementTable WHERE placementId = :id")
	suspend fun getPlacementById(id: Int): PlacementEntity
}