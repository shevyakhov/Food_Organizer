package com.chelz.foodorganizer.screens.toBuyList.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ToBuyListDao {

	@Query("SELECT * FROM ToBuyList")
	fun getAll(): List<ToBuyListEntity>

	@Query("SELECT * FROM ToBuyList WHERE id = :id")
	fun getById(id: Int): ToBuyListEntity

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	suspend fun insert(toBuyList: ToBuyListEntity)

	@Update(onConflict = OnConflictStrategy.REPLACE)
	suspend fun update(toBuyList: ToBuyListEntity)

	@Delete
	suspend fun delete(toBuyList: ToBuyListEntity)

	@Query("DELETE FROM ToBuyList")
	suspend fun deleteAllItems()
}